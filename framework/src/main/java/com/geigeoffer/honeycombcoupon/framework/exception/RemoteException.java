package com.geigeoffer.honeycombcoupon.framework.exception;

import com.geigeoffer.honeycombcoupon.framework.errorcode.BaseErrorCode;
import com.geigeoffer.honeycombcoupon.framework.errorcode.IErrorCode;

/**
 * 全局异常拦截类-远程调用异常
 */
public class RemoteException extends AbstractException{
    public RemoteException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }
    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "," +
                '}';
    }
}
