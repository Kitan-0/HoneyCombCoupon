package com.geigeoffer.honeycombcoupon.merchant.admin.config;

import com.geigeoffer.honeycombcoupon.merchant.admin.common.context.UserContext;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.context.UserInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nullable;

@Configuration
public class UserConfiguration implements WebMvcConfigurer {

    @Bean
    public UserTransmitInterceptor userTransmitInterceptor() {
        return new UserTransmitInterceptor();
    }

    /**
     * 添加拦截器用户处理上下文用户信息
     * @param registry 拦截注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTransmitInterceptor())
                .addPathPatterns("/**");
    }

    static class UserTransmitInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws  Exception {
            //添加上下文用户信息
            UserInfoDTO userInfoDTO = new UserInfoDTO("18212232324343434", "tb232323232", 109800867L);
            UserContext.setUser(userInfoDTO);
            return true;
        }

        @Override
        public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception exception) throws Exception {
            //删除上下文用户信息
            UserContext.removeUser();
        }
    }
}
