package com.geigeoffer.honeycombcoupon.merchant.admin.mq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTaskDelayEvent {
    /**
     * 推送任务id
     */
    private Long couponTaskId;
    /**
     * 发送状态
     */
    private Integer status;
}
