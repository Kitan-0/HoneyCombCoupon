package com.geigeoffer.honeycombcoupon.merchant.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.geigeoffer.honeycombcoupon.framework.exception.ClientException;
import com.geigeoffer.honeycombcoupon.framework.exception.ServiceException;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.mapper.CouponTemplateMapper;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTemplatePageQueryReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTemplateSaveReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.service.base.chain.MerchantAdminChainContext;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.constant.MerchantAdminRedisConstant;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.context.UserContext;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.enums.CouponTemplateStatusEnum;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity.CouponTemplateDO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTemplateNumberReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTemplatePageQueryRespDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTemplateQueryRespDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.service.CouponTemplateService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.geigeoffer.honeycombcoupon.merchant.admin.common.constant.CouponTemplateConstant.*;
import static com.geigeoffer.honeycombcoupon.merchant.admin.common.enums.ChainBizMarkNum.MERCHANT_ADMIN_CREATE_COUPON_TEMPLATE_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplateDO> implements CouponTemplateService {
    private final CouponTemplateMapper couponTemplateMapper;
    private final MerchantAdminChainContext merchantAdminChainContext;
    private final StringRedisTemplate stringRedisTemplate;
    private final ConfigurableEnvironment configurableEnvironment;
    private final RocketMQTemplate rocketMQTemplate;
    @LogRecord(
            success = CREATE_COUPON_TEMPLATE_LOG_CONTENT,
            type = "CouponTemplate",
            bizNo = "{{#bizNo}}",
            extra = "{{#requestParam.toString()}}"
    )
    @Override
    public void createCouponTemplate(CouponTemplateSaveReqDTO requestParam) {
        merchantAdminChainContext.handler(MERCHANT_ADMIN_CREATE_COUPON_TEMPLATE_KEY.name(), requestParam);
        CouponTemplateDO couponTemplateDO = BeanUtil.toBean(requestParam, CouponTemplateDO.class);
        couponTemplateDO.setStatus(CouponTemplateStatusEnum.ACTIVE.getStatus());
        couponTemplateDO.setShopNumber(UserContext.getShopNumber());
        couponTemplateMapper.insert(couponTemplateDO);
        //获取自动生成的id
        LogRecordContext.putVariable("bizNo", couponTemplateDO.getId());
        //执行缓存预热
        CouponTemplateQueryRespDTO actualRespDTO = BeanUtil.toBean(couponTemplateDO, CouponTemplateQueryRespDTO.class);
        Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(actualRespDTO, false, true);
        Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() != null ? entry.getValue().toString() : ""
                ));
        String couponTemplateCacheKey = String.format(MerchantAdminRedisConstant.COUPON_TEMPLATE_KEY,couponTemplateDO.getId());
        //LUA脚本设置hash数据及过期时间
        String luaScript = "redis.call('HMSET', KEYS[1], unpack(ARGV, 1, #ARGV - 1))" +
                "redis.call('EXPIREAT', KEYS[1], ARGV[#ARGV])";
        List<String> keys = Collections.singletonList(couponTemplateCacheKey);
        List<String> args = new ArrayList<>(actualCacheTargetMap.size() * 2 + 1);
        actualCacheTargetMap.forEach((key, value) -> {
            args.add(key);
            args.add(value);
        });
        //优惠券活动过期时间转换为秒级别的Unix时间戳
        args.add(String.valueOf(couponTemplateDO.getValidEndTime().getTime()));
        stringRedisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                keys,
                args.toArray()
        );

        //使用rocketmq发送延时消息
        String couponTemplateDelayCloseTopic = "one-coupon_merchant-admin-service_coupon-template-delay_topic${unique-name:}";
        couponTemplateDelayCloseTopic = configurableEnvironment.resolvePlaceholders(couponTemplateDelayCloseTopic);
        //定义消息体
        JSONObject messageBody = new JSONObject();
        messageBody.put("couponTemplateId", couponTemplateDO.getId());
        messageBody.put("shopNumber", UserContext.getShopNumber());
        //设置消息的到达时间
        Long deliverTimeStamp = couponTemplateDO.getValidEndTime().getTime();
        //构建消息体
        String messageKeys = UUID.randomUUID().toString();
        Message<JSONObject> message = MessageBuilder.withPayload(messageBody)
                .setHeader(MessageConst.PROPERTY_KEYS, messageKeys)
                .build();
        SendResult sendResult;
        try {
            sendResult = rocketMQTemplate.syncSendDeliverTimeMills(couponTemplateDelayCloseTopic,message,deliverTimeStamp);
            log.info("[生产者] 优惠券模板延时关闭 - 发送结果：{}，消息ID：{}，消息Keys：{}", sendResult.getSendStatus(), sendResult.getMsgId(), messageKeys);
        } catch (Exception ex) {
            log.error("[生产者] 优惠券模板延时关闭 - 消息发送失败，消息体：{}", couponTemplateDO.getId(), ex);
        }
    }

    @Override
    public IPage<CouponTemplatePageQueryRespDTO>  pageQueryCouponTemplate(CouponTemplatePageQueryReqDTO requestParam) {
        LambdaQueryWrapper<CouponTemplateDO> queryWrapper = Wrappers.lambdaQuery(CouponTemplateDO.class)
                .eq(CouponTemplateDO::getShopNumber, UserContext.getShopNumber())
                .like(StrUtil.isNotBlank(requestParam.getName()), CouponTemplateDO::getName, requestParam.getName())
                .like(StrUtil.isNotBlank(requestParam.getGoods()), CouponTemplateDO::getGoods, requestParam.getGoods())
                .eq(Objects.nonNull(requestParam.getType()), CouponTemplateDO::getType, requestParam.getType())
                .eq(Objects.nonNull(requestParam.getTarget()), CouponTemplateDO::getTarget, requestParam.getTarget());
        IPage<CouponTemplateDO> selectPage = couponTemplateMapper.selectPage(requestParam, queryWrapper);
        return selectPage.convert(each -> BeanUtil.toBean(each, CouponTemplatePageQueryRespDTO.class));
    }

    @Override
    public void terminateCouponTemplate(String couponTemplateId) {
        LambdaQueryWrapper<CouponTemplateDO> lambdaQueryWrapper = Wrappers.lambdaQuery(CouponTemplateDO.class)
                .eq(CouponTemplateDO::getShopNumber, UserContext.getShopNumber())
                .eq(CouponTemplateDO::getId, couponTemplateId);
        CouponTemplateDO couponTemplateDO = couponTemplateMapper.selectOne(lambdaQueryWrapper);
        if(couponTemplateDO == null) {
            throw new ClientException("优惠券模板异常,请检查操作是否正确...");
        }
        // 验证优惠券模板是否正常
        if (ObjectUtil.notEqual(couponTemplateDO.getStatus(), CouponTemplateStatusEnum.ACTIVE.getStatus())) {
            throw new ClientException("优惠券模板已结束");
        }

        // 记录优惠券模板修改前数据
        LogRecordContext.putVariable("originalData", JSON.toJSONString(couponTemplateDO));

        CouponTemplateDO updateCouponTemplateDO = CouponTemplateDO.builder()
                .status(CouponTemplateStatusEnum.ENDED.getStatus())
                .build();
        Wrapper<CouponTemplateDO> updateWrapper = Wrappers.lambdaUpdate(CouponTemplateDO.class)
                .eq(CouponTemplateDO::getId, couponTemplateDO.getId())
                .eq(CouponTemplateDO::getShopNumber, UserContext.getShopNumber());
        couponTemplateMapper.update(updateCouponTemplateDO, updateWrapper);

        //更新缓存
        String couponTemplateCacheKey = String.format(MerchantAdminRedisConstant.COUPON_TEMPLATE_KEY,couponTemplateId);
        stringRedisTemplate.opsForHash().put(couponTemplateCacheKey, "status", String.valueOf(CouponTemplateStatusEnum.ENDED.getStatus()));
    }

    @Override
    public CouponTemplateQueryRespDTO findCouponTemplateById(String couponTemplateId) {
        LambdaQueryWrapper<CouponTemplateDO> lambdaQueryWrapper = Wrappers.lambdaQuery(CouponTemplateDO.class)
                .eq(CouponTemplateDO::getShopNumber, UserContext.getShopNumber())
                .eq(CouponTemplateDO::getId, couponTemplateId);
        CouponTemplateDO couponTemplateDO = couponTemplateMapper.selectOne(lambdaQueryWrapper);
        return BeanUtil.toBean(couponTemplateDO, CouponTemplateQueryRespDTO.class);
    }
    @LogRecord(
            success = INCREASE_NUMBER_COUPON_TEMPLATE_LOG_CONTENT,
            type = "CouponTemplate",
            bizNo = "{{#requestParam.couponTemplateId}}"
    )

    @LogRecord(
            success = TERMINATE_COUPON_TEMPLATE_LOG_CONTENT,
            type = "CouponTemplate",
            bizNo = "{{#couponTemplateId}}"
    )
    @Override
    public void increaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam) {
        LambdaQueryWrapper<CouponTemplateDO> lambdaQueryWrapper = Wrappers.lambdaQuery(CouponTemplateDO.class)
                .eq(CouponTemplateDO::getShopNumber, UserContext.getShopNumber())
                .eq(CouponTemplateDO::getId, requestParam.getCouponTemplateId());
        CouponTemplateDO couponTemplateDO = couponTemplateMapper.selectOne(lambdaQueryWrapper);
        if(couponTemplateDO == null) {
            throw new ClientException("优惠券模板异常，请检查操作是否正确...");
        }
        if(ObjectUtil.notEqual(couponTemplateDO.getStatus(), CouponTemplateStatusEnum.ACTIVE.getStatus())) {
            throw new ClientException("优惠券模板已经结束");
        }
        LogRecordContext.putVariable("originalData", JSON.toJSONString(couponTemplateDO));
        //增加发行量
        int increased = couponTemplateMapper.increaseCouponTemplate(UserContext.getShopNumber(), requestParam.getCouponTemplateId(),requestParam.getNumber());
        if(!SqlHelper.retBool(increased)) {
            throw new ServiceException("优惠券模板增加发行量失败");
        }
        String couponTemplateCacheKey = String.format(MerchantAdminRedisConstant.COUPON_TEMPLATE_KEY, requestParam.getCouponTemplateId());
        stringRedisTemplate.opsForHash().increment(couponTemplateCacheKey,"stock", requestParam.getNumber());
    }

}
