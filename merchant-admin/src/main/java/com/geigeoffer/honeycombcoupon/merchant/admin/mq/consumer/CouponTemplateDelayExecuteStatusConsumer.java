package com.geigeoffer.honeycombcoupon.merchant.admin.mq.consumer;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.constant.MerchantAdminRocketMQConstant;
import com.geigeoffer.honeycombcoupon.merchant.admin.common.enums.CouponTemplateStatusEnum;
import com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity.CouponTemplateDO;
import com.geigeoffer.honeycombcoupon.merchant.admin.service.CouponTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = MerchantAdminRocketMQConstant.TEMPLATE_DELAY_TOPIC_KEY,
        consumerGroup = MerchantAdminRocketMQConstant.TEMPLATE_DELAY_STATUS_CG_KEY
)
@RequiredArgsConstructor
@Slf4j(topic = "CouponTemplateDelayExecuteStatusConsumer")
public class CouponTemplateDelayExecuteStatusConsumer implements RocketMQListener<JSONObject> {
    private final CouponTemplateService couponTemplateService;
    @Override
    public void onMessage(JSONObject message) {
        log.info("[消费者]优惠券模板定时执行@变更模板表状态 - 执行消费逻辑，消息体：{}", message.toString());
        LambdaUpdateWrapper<CouponTemplateDO> updateWrapper = Wrappers.lambdaUpdate(CouponTemplateDO.class)
                .eq(CouponTemplateDO::getShopNumber, message.getLong("shopNumber"))
                .eq(CouponTemplateDO::getId, message.getLong("couponTemplateId"))
                .set(CouponTemplateDO::getStatus, CouponTemplateStatusEnum.ENDED.getStatus());
        couponTemplateService.update(updateWrapper);
    }
}
