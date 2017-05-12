package com.dianba.pos.extended.vo;

/**
 * Created by Administrator on 2017/5/10 0010.
 * 话费充值回调
 */

public class ChargeCallBack {

    private String ehfOrderId;
    private String merchantOrderId;

    private String chargeMoney;

    private String finishTime;

    private String chargeStatus;

    private String statusDesc;

    public String getEhfOrderId() {
        return ehfOrderId;
    }

    public void setEhfOrderId(String ehfOrderId) {
        this.ehfOrderId = ehfOrderId;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Override
    public String toString() {
        return "ChargeCallBack{" +
                "ehfOrderId='" + ehfOrderId + '\'' +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                ", chargeMoney='" + chargeMoney + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", chargeStatus='" + chargeStatus + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                '}';
    }

    public String callback() {
        return

                "resultCode=" + chargeStatus + '&' +
                        "resultDesc=" + statusDesc;

    }
}
