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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_coupon_task")
public class CouponTaskDo {
    /**
     * id
     */
    private Long id;
    /**
     * 店铺编号
     */
    private Long shopNumber;
    /**
     * 批次id
     */
    private Long batchId;
    /**
     * 优惠券批次任务名称
     */
    private String taskName;
    /**
     * 文件地址
     */
    private String fileAddress;
    /**
     * 发放失败用户文件地址
     */
    private String failFileAddress;
    /**
     * 发放优惠券数量
     */
    private Integer sendNum;
    /**
     * 通知方式，0：站内信 1：弹框推送 2:邮箱 3:短信
     */
    private String notifyType;
    /**
     * 优惠券模板id
     */
    private Long couponTemplateId;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 状态 0：待执行 1：执行中 2：执行失败 3：执行成功 4：取消
     */
    private Integer status;
    /**
     * 完成时间
     */
    private Date completionTime;
    /**
     * 操作人
     */
    private Long operatorId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date UpdateTime;
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

}
