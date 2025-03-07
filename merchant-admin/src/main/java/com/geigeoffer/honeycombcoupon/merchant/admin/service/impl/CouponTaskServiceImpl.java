package com.geigeoffer.honeycombcoupon.merchant.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geigeoffer.honeycombcoupon.framework.exception.ClientException;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.mapper.CouponTaskMapper;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.context.UserContext;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.enums.CouponTaskSendTypeEnum;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.enums.CouponTaskStatusEnum;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity.CouponTaskDo;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTaskCreateReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTemplateQueryRespDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.service.CouponTaskService;
import com.geigeoffer.honeycombcoupon.merchant.admin.service.CouponTemplateService;
import com.geigeoffer.honeycombcoupon.merchant.admin.service.handler.excel.RowCountListener;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class CouponTaskServiceImpl extends ServiceImpl<CouponTaskMapper, CouponTaskDo> implements CouponTaskService {
    private final CouponTemplateService couponTemplateService;
    private final CouponTaskMapper couponTaskMapper;
    private final RedissonClient redissonClient;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() << 1,
            60,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.DiscardPolicy()
    );
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createCouponTask(CouponTaskCreateReqDTO requestParam) {
        CouponTemplateQueryRespDTO  couponTemplateQueryRespDTO = couponTemplateService.findCouponTemplateById(requestParam.getCouponTemplateId());
        if(couponTemplateQueryRespDTO == null) {
            throw new ClientException("优惠券模板不存在，请检查提交信息是否正确");
        }
        //构建优惠券推送任务数据库持久层实体
        CouponTaskDo couponTaskDo = BeanUtil.toBean(requestParam, CouponTaskDo.class);
        couponTaskDo.setBatchId(IdUtil.getSnowflakeNextId());
        couponTaskDo.setOperatorId(Long.parseLong(UserContext.getUserId()));
        couponTaskDo.setStatus(
                Objects.equals(requestParam.getSendType(), CouponTaskSendTypeEnum.IMMEDIATE.getType())
                ? CouponTaskStatusEnum.PROCESSING.getStatus() : CouponTaskStatusEnum.PENDING.getStatus()
        );
        couponTaskMapper.insert(couponTaskDo);
//        //读取Excel文件
//        RowCountListener rowCountListener = new RowCountListener();
//        EasyExcel.read(requestParam.getFileAddress(), rowCountListener).sheet().doRead();
//        int rowCount = rowCountListener.getRowcount();
//        couponTaskDo.setSendNum(rowCount);
        //报错优惠券推送任务到数据库

        JSONObject delayJsonObject = JSONObject.of("fileAddress",requestParam.getFileAddress(), "couponTaskId", couponTaskDo.getId());
        executorService.execute(()->refreshCouponTaskSendNum(delayJsonObject));

        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque("COUPON_TASK_SEND_NUM_DELAY_QUEUE");
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(delayJsonObject,20,TimeUnit.SECONDS);

    }

    private void refreshCouponTaskSendNum(JSONObject delayJsonObject) {
        RowCountListener rowCountListener = new RowCountListener();
        int totalRows = rowCountListener.getRowcount();
        EasyExcel.read(delayJsonObject.getString("fileAddress"), rowCountListener).sheet().doRead();
        CouponTaskDo updateCouponTaskDO = CouponTaskDo.builder()
                .id(delayJsonObject.getLong("couponTaskId"))
                .sendNum(totalRows)
                .build();
        couponTaskMapper.updateById(updateCouponTaskDO);
    }

    @Service
    @RequiredArgsConstructor
    class RefreshCouponTaskDelayQueueRunner implements CommandLineRunner {
        private final CouponTaskMapper couponTaskMapper;
        private final RedissonClient redissonClient;
        @Override
        public void run(String ...args) throws Exception {
            Executors.newSingleThreadExecutor(
                    runable-> {
                        Thread thread = new Thread(runable);
                        thread.setName("delay_coupon-task_send-num_consumer");
                        thread.setDaemon(Boolean.TRUE);
                        return thread;
                    })
                    .execute(()-> {
                        RBlockingDeque<JSONObject> blockingDeque = redissonClient.getBlockingDeque("COUPON_TASK_SEND_NUM_DELAY_QUEUE");
                        for(;;) {
                            try {
                                JSONObject delayJsonObject = blockingDeque.take();
                                if(delayJsonObject != null) {
                                    CouponTaskDo couponTaskDo = couponTaskMapper.selectById(delayJsonObject.getLong("couponTaskId"));
                                    if(couponTaskDo.getSendNum() == null) {
                                        refreshCouponTaskSendNum(delayJsonObject);
                                    }
                                }
                            } catch (Throwable ignored) {

                            }
                        }
                    });
        }
    }

}
