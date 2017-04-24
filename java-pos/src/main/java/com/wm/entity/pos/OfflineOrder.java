package com.wm.entity.pos;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

/**
 * Created by zc on 2016/11/23.
 */
@Document(collection = "off_line_order")
public class OfflineOrder {
    /**
     * UUID
     */
    @Id
    private String uuid;
    /**
     * 商家ID
     */
    private long merchantId;
    /**
     * 营业员ID
     */
    private long cashierId;
    /**
     * 商品信息
     */
    private List<OrderMenuParams> params;
    /**
     * 下单时间
     */
    private long createTime;
    /**
     * 版本号
     */
    private long version;
    /**
     * 0 未处理
     * 1 处理中
     * 2 下单处理成功
     * 3 下单处理失败
     * 4 支付成功
     * 5 支付失败
     */
    private int state;
    /**
     * 处理时间,默认0即可
     */
    private int doTime;

    /**
     * 金额
     */
    private String cash;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public long getCashierId() {
        return cashierId;
    }

    public void setCashierId(long cashierId) {
        this.cashierId = cashierId;
    }

    public List<OrderMenuParams> getParams() {
        return params;
    }

    public void setParams(List<OrderMenuParams> params) {
        this.params = params;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDoTime() {
        return doTime;
    }

    public void setDoTime(int doTime) {
        this.doTime = doTime;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "OfflineOrder{" +
                "uuid='" + uuid + '\'' +
                ", merchantId=" + merchantId +
                ", cashierId=" + cashierId +
                ", params=" + params +
                ", createTime=" + createTime +
                ", version=" + version +
                ", state=" + state +
                ", doTime=" + doTime +
                ", cash='" + cash + '\'' +
                '}';
    }
}
