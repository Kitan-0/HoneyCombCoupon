package com.geigeoffer.honeycombcoupon.framework.exception;

import com.geigeoffer.honeycombcoupon.framework.errorcode.IErrorCode;

import java.util.Optional;

import org.springframework.util.StringUtils;
/**
 * 全局异常拦截器抽象类
 */
public abstract class AbstractException extends RuntimeException{
    public final String errorCode;
    public final String errorMessage;
    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message : null).orElse(errorCode.message());
    }
}
