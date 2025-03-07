package com.geigeoffer.honeycombcoupon.framework.result;

import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Accessors(chain = true)
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5679018624309023727L;
    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "0";

    /**
     * 状态码
     */
    private String code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;
    /**
     * 请求ID
     */
    private String requestId;
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
    public boolean isFail() {
        return !isSuccess();
    }
}
