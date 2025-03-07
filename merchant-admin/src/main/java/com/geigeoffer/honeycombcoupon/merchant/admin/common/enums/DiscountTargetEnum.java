package com.geigeoffer.honeycombcoupon.merchant.admin.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiscountTargetEnum {
    /**
     * 商品专属优惠
     */
    PRODUCT_SPECIFIC(0, "商品专属优惠"),
    ALL_STORE_GENERAL(1, "全店通用优惠");
    @Getter
    private final int type;
    @Getter
    private final String value;

}
