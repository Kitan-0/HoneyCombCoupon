package com.geigeoffer.honeycombcoupon.framework.exception;

import com.geigeoffer.honeycombcoupon.framework.errorcode.BaseErrorCode;
import com.geigeoffer.honeycombcoupon.framework.errorcode.IErrorCode;

/**
 * 全局异常拦截器-客户端异常类
 */
public class ClientException extends AbstractException{
    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);

    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code = " + errorCode + "," +
                "message = " + errorMessage + "," +
                "}";
    }

}
