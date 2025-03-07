package com.geigeoffer.honeycombcoupon.merchant.admin.mq.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseSendExtendDTO {
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 主题
     */
    private String topic;
    /**
     * 标签
     */
    private String tag;
    /**
     * 业务标识
     */
    private String keys;
    /**
     * 发送消息超出时间
     */
    private Long sendTimeout;
    /**
     * 具体延迟时间
     */
    private Long delayTime;

}
