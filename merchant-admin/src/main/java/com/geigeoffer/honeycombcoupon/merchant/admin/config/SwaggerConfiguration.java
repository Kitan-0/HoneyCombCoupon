package com.geigeoffer.honeycombcoupon.merchant.admin.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SwaggerConfiguration implements ApplicationRunner {
    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    /**
     * 自定义OpenAPI个性化信息
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title("HomeyCombCoupon-商家后台管理系统")
                .description("创建优惠券、商家查看及管理优惠券、创建优惠券发放批次等")
                .version("v1.0.0")
                .contact(new Contact().name("geigeoffer").email("geigeoffer@163.com"))
                .license(new License().name("给个offer有限公司"));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("API Document: http://127.0.0.1:{}{}/doc.html", serverPort, contextPath);
    }
}
