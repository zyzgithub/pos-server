package com.dianba.pos.scheduler.vo;

/**
 * Created by zhangyong on 2017/7/5.
 */
public class ScalpListByPassportVo {

    private Long passportId;

    private Integer seconds;

    private String sequenceNumber;
    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
