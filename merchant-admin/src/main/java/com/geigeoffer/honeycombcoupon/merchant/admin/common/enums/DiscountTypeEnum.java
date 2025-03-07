package com.geigeoffer.honeycombcoupon.merchant.admin.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiscountTypeEnum {
    FIXED_DISCOUNT(0, "立减券"),
    THRESHOLD_DISCOUNT(1, "满减券"),
    DISCOUNT_COUPON(2, "折扣券");
    @Getter
    private final int type;
    @Getter
    private final String value;
    public static String findValueByType(int type) {
        for(DiscountTypeEnum target : DiscountTypeEnum.values()) {
            if(target.getType() == type) {
                return target.getValue();
            }
        }
        throw new IllegalArgumentException();
    }
}
