package com.dianba.pos.payment.po;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "life_pos.pos_merchant_quota")
@DynamicUpdate
public class PosMerchantQuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "passport_id")
    private Long passportId;
    @Column(name = "period")
    private Integer period;
    @Column(name = "reward_type")
    private Integer rewardType;
    @Column(name = "current_reward")
    private BigDecimal currentReward;
    @Column(name = "remaining_reward")
    private BigDecimal remainingReward;
    @Column(name = "total_reward_quota")
    private BigDecimal totalRewardQuota;
    @Column(name = "create_time")
    private Date createTime=new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public BigDecimal getCurrentReward() {
        return currentReward;
    }

    public void setCurrentReward(BigDecimal currentReward) {
        this.currentReward = currentReward;
    }

    public BigDecimal getRemainingReward() {
        return remainingReward;
    }

    public void setRemainingReward(BigDecimal remainingReward) {
        this.remainingReward = remainingReward;
    }

    public BigDecimal getTotalRewardQuota() {
        return totalRewardQuota;
    }

    public void setTotalRewardQuota(BigDecimal totalRewardQuota) {
        this.totalRewardQuota = totalRewardQuota;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
