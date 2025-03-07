package com.geigeoffer.honeycombcoupon.merchant.admin.dto.req;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "优惠券模板新增参数")
public class CouponTemplateSaveReqDTO {
    /**
     * 优惠券名称
     */
    @Schema(description = "优惠券名称",
            example = "用户下单满10减3特大优惠券",
            required = true)
    private String couponName;
    /**
     * 优惠券来源
     */
    @Schema(description = "优惠券来源 0：店铺券 1：平台券",
            example = "0",
            required = true)
    private Integer source;
    /**
     * 优惠券对象
     */
    @Schema(description = "优惠对象 0：商品专属 1：全店通用",
            example = "1",
            required = true)
    private Integer target;
    /**
     * 优惠券编码
     */
    @Schema(description = "优惠商品编码")
    private String goods;
    /**
     * 优惠券类型
     */
    @Schema(description = "优惠类型 0：立减券 1：满减券 2：折扣券",
            example = "0",
            required = true)
    private Integer type;
    /**
     * 有效期开始时间
     */
    @Schema(description = "有效期开始时间",
            example = "2024-07-08 12:00:00",
            required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validStartTime;
    /**
     * 有效期结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validEndTime;

    @Schema(description = "库存",
            example = "200",
            required = true)
    private Integer stock;

    /**
     * 领取规则
     */
    @Schema(description = "领取规则",
            example = "{\"limitPerPerson\":1,\"usageInstructions\":\"3\"}",
            required = true)
    private String receiveRule;

    /**
     * 消耗规则
     */
    @Schema(description = "消耗规则",
            example = "{\"termsOfUse\":10,\"maximumDiscountAmount\":3,\"explanationOfUnmetConditions\":\"3\",\"validityPeriod\":\"48\"}",
            required = true)
    private String consumeRule;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
