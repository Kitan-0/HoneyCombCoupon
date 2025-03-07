package com.geigeoffer.honeycombcoupon.merchant.admin.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CouponTaskStatusEnum {
    /**
     * 待执行
     */
    PENDING(0),
    /**
     * 执行中
     */
    PROCESSING(1),
    /**
     * 执行失败
     */
    FAILED(2),
    /**
     * 执行成功
     */
    SUCCESS(3),
    /**
     * 取消
     */
    CANCEL(4);
    @Getter
    private final int status;
}
