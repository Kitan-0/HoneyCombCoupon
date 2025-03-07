package com.geigeoffer.honeycombcoupon.merchant.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_coupon_template")
public class CouponTemplateDO {
    /**
     * id
     */
    private Long id;
    /**
     * 店铺编号
     */
    private Long shopNumber;
    /**
     * 优惠券名称
     */
    private String name;
    /**
     * 优惠券来源(0店铺券，1平台券)
     */
    private Integer source;
    /**
     * 优惠券对象(0商品专属，1全店通用)
     */
    private Integer target;
    /**
     * 优惠商品编码
     */
    private String goods;
    /**
     * 优惠券类型：0立减券1：满减券2:折扣券
     */
    private Integer type;
    /**
     * 有效期开始时间
     */
    private Date validStartTime;
    /**
     * 有效期结束时间
     */
    private Date validEndTime;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 领取规则
     */
    private String receiveRule;
    /**
     * 消耗规则
     */
    private String consumeRule;
    /**
     * 优惠券状态：0生效，1过期
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 删除标识：0未删除 1已删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
}
