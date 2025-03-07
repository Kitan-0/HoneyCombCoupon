package com.geigeoffer.honeycombcoupon.merchant.admin.common.constant;

/**
 * 优惠券模板公共常量类
 */
public final class CouponTemplateConstant {
    public static final String CREATE_COUPON_TEMPLATE_LOG_CONTENT = "{CURRENT_USER{''}} 用户创建优惠券：{{#requestParam.name}}，" +
            "优惠对象：{COMMON_ENUM_PARSE{'DiscountTargetEnum' + '_' + #requestParam.target}}，" +
            "优惠类型：{COMMON_ENUM_PARSE{'DiscountTypeEnum' + '_' + #requestParam.type}}，" +
            "库存数量：{{#requestParam.stock}}，" +
            "优惠商品编码：{{#requestParam.goods}}，" +
            "有效期开始时间：{{#requestParam.validStartTime}}，" +
            "有效期结束时间：{{#requestParam.validEndTime}}，" +
            "领取规则：{{#requestParam.receiveRule}}，" +
            "消耗规则：{{#requestParam.consumeRule}};";
    public static final String TERMINATE_COUPON_TEMPLATE_LOG_CONTENT = "{CURRENT_USER{''}} 结束优惠券";
    public static final String INCREASE_NUMBER_COUPON_TEMPLATE_LOG_CONTENT = "{CURRENT_USER{''}} 增加发行量：{{#requestParam.number}}";
}
