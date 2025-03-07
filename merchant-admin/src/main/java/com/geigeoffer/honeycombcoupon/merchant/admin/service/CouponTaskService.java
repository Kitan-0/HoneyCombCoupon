package com.geigeoffer.honeycombcoupon.merchant.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity.CouponTaskDo;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTaskPageQueryReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTaskPageQueryRespDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTaskCreateReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTaskQueryRespDTO;

public interface CouponTaskService extends IService<CouponTaskDo> {
    /**
     * 商家创建优惠券推送任务
     *
     * @param requestParam 请求参数
     */
    void createCouponTask(CouponTaskCreateReqDTO requestParam);

    /**
     * 分页查询商家优惠券推送任务
     *
     * @param requestParam 请求参数
     * @return 商家优惠券推送任务分页数据
     */
    IPage<CouponTaskPageQueryRespDTO> pageQueryCouponTask(CouponTaskPageQueryReqDTO requestParam);

    /**
     * 查询优惠券推送任务详情
     *
     * @param taskId 推送任务 ID
     * @return 优惠券推送任务详情
     */
    CouponTaskQueryRespDTO findCouponTaskById(String taskId);
}
