package com.geigeoffer.honeycombcoupon.framework.exception;

import com.geigeoffer.honeycombcoupon.framework.errorcode.BaseErrorCode;
import com.geigeoffer.honeycombcoupon.framework.errorcode.IErrorCode;

/**
 * 全局异常拦截器-服务端类
 */
public class ServiceException extends AbstractException{
    public ServiceException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "," +
                '}';
    }
}
