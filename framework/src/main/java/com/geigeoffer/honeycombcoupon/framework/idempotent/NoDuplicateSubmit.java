package com.geigeoffer.honeycombcoupon.framework.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDuplicateSubmit {
    /**
     * 触发幂等失败逻辑，返货错误提示信息
     */
    String message() default "您操作太快了, 请稍后再试";
}
