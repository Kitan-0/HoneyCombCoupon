package com.geigeoffer.honeycombcoupon.merchant.admin.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum CouponTemplateStatusEnum {
    ACTIVE(0),
    ENDED(1);
    @Getter
    private final int status;
}
