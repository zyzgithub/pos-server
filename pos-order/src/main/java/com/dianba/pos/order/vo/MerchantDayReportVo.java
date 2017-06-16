package com.dianba.pos.order.vo;

import java.math.BigDecimal;

/**
 * Created by zhangyong on 2017/6/8.
 */
public class MerchantDayReportVo {

    /**
     * 商品id
     **/
    private Long piId;

    /**
     * 商品名字
     **/
    private String itemName;

    /**
     * 商品模板id
     **/
    private String itemTemplateId;

    /**
     * 商品分类id
     **/
    private Long itId;
    /**
     * 商品分类名字
     **/
    private String itTitle;

    /**
     * 商品数量
     **/
    private Integer sumCount;


    /**
     * 商品总金额
     **/
    private BigDecimal sumCostMoney;

    /**
     * 商品销售总金额
     **/
    private BigDecimal sumTotalMoney;





    private BigDecimal a = new BigDecimal(100);


    public BigDecimal getCostMoney() {

        return sumCostMoney.divide(a, 2, BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal getTotalMoney() {

        return sumTotalMoney.divide(a, 2, BigDecimal.ROUND_HALF_UP);
    }


    public BigDecimal getMarginMoney(){


        return getCostMoney().subtract(getTotalMoney());
    }

    public String getGrossMargin() {
        BigDecimal c = getMarginMoney().multiply(a).divide(getTotalMoney(), 2, BigDecimal.ROUND_HALF_UP);
        return c+"%";
    }


    public Long getPiId() {
        return piId;
    }

    public void setPiId(Long piId) {
        this.piId = piId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public Long getItId() {
        return itId;
    }

    public void setItId(Long itId) {
        this.itId = itId;
    }

    public String getItTitle() {
        return itTitle;
    }

    public void setItTitle(String itTitle) {
        this.itTitle = itTitle;
    }

    public Integer getSumCount() {
        return sumCount;
    }

    public void setSumCount(Integer sumCount) {
        this.sumCount = sumCount;
    }

    public BigDecimal getSumCostMoney() {
        return sumCostMoney;
    }

    public void setSumCostMoney(BigDecimal sumCostMoney) {
        this.sumCostMoney = sumCostMoney;
    }

    public BigDecimal getSumTotalMoney() {
        return sumTotalMoney;
    }

    public void setSumTotalMoney(BigDecimal sumTotalMoney) {
        this.sumTotalMoney = sumTotalMoney;
    }
}
