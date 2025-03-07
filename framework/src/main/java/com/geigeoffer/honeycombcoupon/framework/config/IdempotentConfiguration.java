package com.geigeoffer.honeycombcoupon.framework.config;

import com.geigeoffer.honeycombcoupon.framework.idempotent.NoDuplicateSubmitAspect;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

public class IdempotentConfiguration {
    @Bean
    public NoDuplicateSubmitAspect noDuplicateSubmitAspect(RedissonClient redissonClient) {
        return new NoDuplicateSubmitAspect(redissonClient);
    }
}
