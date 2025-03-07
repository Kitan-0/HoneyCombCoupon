package com.geigeoffer.honeycombcoupon.merchant.admin;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLogRecord(tenant = "MerchantAdmin")
public class MerchantAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MerchantAdminApplication.class, args);
    }
}
