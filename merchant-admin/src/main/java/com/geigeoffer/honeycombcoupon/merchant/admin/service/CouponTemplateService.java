package com.geigeoffer.honeycombcoupon.merchant.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity.CouponTemplateDO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTaskPageQueryReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTemplatePageQueryReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTemplateSaveReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTemplatePageQueryRespDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.req.CouponTemplateNumberReqDTO;
import com.geigeoffer.honeycombcoupon.merchant.admin.dto.resp.CouponTemplateQueryRespDTO;

public interface CouponTemplateService extends IService<CouponTemplateDO> {
    /**
     * 创建商家优惠券模板
     * @param requestParam 优惠券模板请求参数
     */
    void createCouponTemplate(CouponTemplateSaveReqDTO requestParam);

    /**
     * 分页查询商家优惠券模板
     * @param requestParam 请求参数
     * @return 商家优惠券模板分页查询结果
     */
    IPage<CouponTemplatePageQueryReqDTO> pageQueryCouponTemplate(CouponTaskPageQueryReqDTO requestParam);
    CouponTemplateQueryRespDTO findCouponTemplateById(String couponTemplateId);

    /**
     * 结束优惠券模板
     *
     * @param couponTemplateId 优惠券模板 ID
     */
    void terminateCouponTemplate(String couponTemplateId);

    /**
     * 增加优惠券模板发行量
     *
     * @param requestParam 请求参数
     */
    void increaseNumberCouponTemplate(CouponTemplateNumberReqDTO requestParam);

    IPage<CouponTemplatePageQueryRespDTO>  pageQueryRespDTOIPage(CouponTemplatePageQueryReqDTO requestParam);

    IPage<CouponTemplatePageQueryRespDTO>  pageQueryCouponTemplate(CouponTemplatePageQueryReqDTO requestParam);
}
