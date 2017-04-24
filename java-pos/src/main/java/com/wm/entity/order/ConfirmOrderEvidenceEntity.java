package com.wm.entity.order;

import javax.persistence.*;

/**
 * @author zhoucong
 * @version V1.0
 * @Title: Entity
 * @Description: order
 * @date 2016-08-27 14:46:03
 */
@Entity
@Table(name = "`confirm_order_evidence`", schema = "")
@SuppressWarnings("serial")
public class ConfirmOrderEvidenceEntity implements java.io.Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 支付 ID
     */
    private String payId;
    /**
     * 订单 ID
     */
    private Integer orderId;
    /**
     * 文本
     */
    private String textarea;
    /**
     * 图片
     */
    private String images;
    /**
     * 用户 ID
     */
    private Integer userId;
    /**
     * 0:未处理,1:处理中,2:通过,3:不通过
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Long createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, precision = 20, scale = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "pay_id", nullable = false)
    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    @Column(name = "textarea", nullable = false)
    public String getTextarea() {
        return textarea;
    }

    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }

    @Column(name = "images", nullable = false)
    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Column(name = "user_id", nullable = false)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "state", nullable = false)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "create_time", nullable = false)
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Column(name = "order_id", nullable = false)

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "ConfirmOrderEvidenceEntity{" +
                "id=" + id +
                ", payId='" + payId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", textarea='" + textarea + '\'' +
                ", images='" + images + '\'' +
                ", userId=" + userId +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
