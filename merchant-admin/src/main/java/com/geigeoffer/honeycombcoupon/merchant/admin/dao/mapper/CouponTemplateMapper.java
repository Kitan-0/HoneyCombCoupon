package com.geigeoffer.honeycombcoupon.merchant.admin.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity.CouponTemplateDO;
import org.apache.ibatis.annotations.Param;

public interface CouponTemplateMapper extends BaseMapper<CouponTemplateDO> {
    /**
     * 增加优惠券模板发行量
     * @param shopNumber 店铺编号
     * @param couponTemplateId 增加的优惠券模板ID
     * @param number 增加的发行数量
     * @return
     */
    int increaseCouponTemplate(@Param("shopNumber") Long shopNumber, @Param("couponTemplateId") String couponTemplateId,
                               @Param("number") Integer number);
}
