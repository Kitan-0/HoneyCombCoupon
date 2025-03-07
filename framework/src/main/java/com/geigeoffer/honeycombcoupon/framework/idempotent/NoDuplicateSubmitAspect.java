package com.geigeoffer.honeycombcoupon.framework.idempotent;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.geigeoffer.honeycombcoupon.framework.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@RequiredArgsConstructor
public final class NoDuplicateSubmitAspect {
    private final RedissonClient redissonClient;
    @Around("@annotation(com.geigeoffer.homeycombcoupon.framework.idempotent.NoDuplicateSubmit)")
    public Object noDuplicateSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        NoDuplicateSubmit noDuplicateSubmit = getNoDuplicateSubmitAnnotation(joinPoint);
        String lockKey = String.format("no-duplicate-submit:path:%s:currentUserId:%s:md5:%s", getServletPath(), getCurrentUserId(), calcArgsMD5(joinPoint));
        //获取分布式锁
        RLock lock = redissonClient.getLock(lockKey);
        if(!lock.tryLock()) {
            throw new ClientException(noDuplicateSubmit.message());
        }
        //如果获取锁失败表示重复提交，直接抛出异常
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            lock.unlock();
        }
        return result;
    }


    /**
     * @return 返回自定义防重复提交注解
     */
    public static NoDuplicateSubmit getNoDuplicateSubmitAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),methodSignature.getMethod().getParameterTypes());
        return targetMethod.getAnnotation(NoDuplicateSubmit.class);
    }

    /**
     * @return 获取当前线程的上下文ServletPath
     */
    private String getServletPath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sra.getRequest().getServletPath();
    }

    /**
     * 返回当前操作用户
     * @return
     */
    private String getCurrentUserId() {
        return "1810518709471555585";
    }

    /**
     * @return 返回md5加密的 joinPoint
     */
    private String calcArgsMD5(ProceedingJoinPoint joinPoint) {
        return DigestUtil.md5Hex(JSON.toJSONBytes(joinPoint.getArgs()));
    }
}
