package com.wm.dto.post;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by mjorcen on 16/8/10.
 */
@Document
public class BuyoutAlertLogs {
    @Id
    private Long merchantId;
    private Long createTime;
    private Long lastPushTime;
    private Long lastAccessTime;
    //    // 1 : 需要推送, 2: 不需要推送, 3: 肯能需要推送
//    private Integer state;
    private Long count;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastPushTime() {
        return lastPushTime;
    }

    public void setLastPushTime(Long lastPushTime) {
        this.lastPushTime = lastPushTime;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public String toString() {
        return "BuyoutAlertLogs{" +
                "merchantId=" + merchantId +
                ", createTime=" + createTime +
                ", lastPushTime=" + lastPushTime +
                ", count=" + count +
                '}';
    }
}
