package com.geigeoffer.honeycombcoupon.merchant.admin.job;

import com.geigeoffer.honeycombcoupon.merchant.admin.dao.mapper.CouponTaskMapper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponTaskHandler extends IJobHandler {
    private final CouponTaskMapper couponTaskMapper;

    @XxlJob(value = "couponTemplateTask")
    public void execute() throws Exception {

    }
}
