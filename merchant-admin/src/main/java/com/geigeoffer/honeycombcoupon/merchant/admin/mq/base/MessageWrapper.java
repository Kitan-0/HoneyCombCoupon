package com.geigeoffer.honeycombcoupon.merchant.admin.mq.base;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class MessageWrapper<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 消息发送keys
     */
    private String keys;
    /**
     * 消息体
     */
    private T message;
    /**
     * 消息发送时间
     */
    private Long timestamp = System.currentTimeMillis();
}
