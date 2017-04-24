package com.wm.dto.post;

/**
 * Created by mjorcen on 16/8/19.
 */
public class BuyoutLogDto {
    /**
     * 需要支付的时间
     */
    private Long needPayTime;
    /**
     * 实际支付的时间
     */
    private Long realPayTime;
    /**
     * 标题 POS购买（分期购买）
     */
    private String title;
    /**
     * 描述, 如 1期
     */
    private String descLine;
    /**
     * 金额 -188.00
     */
    private String number;
    /**
     * 0, 未支付
     * 1, 已支付
     */
    private Integer state;

    public Long getNeedPayTime() {
        return needPayTime;
    }

    public void setNeedPayTime(Long needPayTime) {
        this.needPayTime = needPayTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescLine() {
        return descLine;
    }

    public void setDescLine(String descLine) {
        this.descLine = descLine;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getRealPayTime() {
        return realPayTime;
    }

    public void setRealPayTime(Long realPayTime) {
        this.realPayTime = realPayTime;
    }

    @Override
    public String toString() {
        return "BuyoutLogDto{" +
                "needPayTime=" + needPayTime +
                ", realPayTime=" + realPayTime +
                ", title='" + title + '\'' +
                ", descLine='" + descLine + '\'' +
                ", number='" + number + '\'' +
                ", state=" + state +
                '}';
    }
}
