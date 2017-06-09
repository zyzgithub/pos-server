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
    private Long sumCostMoney;

    /**
     * 商品销售总金额
     **/
    private Long sumTotalMoney;





    private BigDecimal a = new BigDecimal(100);


    public Double getCostMoney() {
        BigDecimal sumMoneyBd = new BigDecimal(sumCostMoney);
        BigDecimal sumTotalMoneyBd = new BigDecimal(sumTotalMoney);
        return sumMoneyBd.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
    }


    public Double getTotalMoney() {
        BigDecimal sumMoneyBd = new BigDecimal(sumCostMoney);
        BigDecimal sumTotalMoneyBd = new BigDecimal(sumTotalMoney);
        return sumTotalMoneyBd.divide(a, 2, BigDecimal.ROUND_UP).doubleValue();
    }


    public Double getMarginMoney(){

        BigDecimal sumTotalMoneyBd=new BigDecimal(sumTotalMoney);
        BigDecimal b=new BigDecimal(sumCostMoney);

        return sumTotalMoneyBd.subtract(b).divide(a,2,BigDecimal.ROUND_UP).doubleValue();
    }

    public String getGrossMargin() {
        BigDecimal sumMoneyBd = new BigDecimal(sumCostMoney);
        BigDecimal sumTotalMoneyBd = new BigDecimal(sumTotalMoney);
        Double c = new BigDecimal(sumTotalMoney - sumCostMoney)
                .divide(sumTotalMoneyBd, 2, BigDecimal.ROUND_UP)
                .multiply(a)
                .doubleValue();

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

    public Long getSumCostMoney() {
        return sumCostMoney;
    }

    public void setSumCostMoney(Long sumCostMoney) {
        this.sumCostMoney = sumCostMoney;
    }

    public Long getSumTotalMoney() {
        return sumTotalMoney;
    }

    public void setSumTotalMoney(Long sumTotalMoney) {
        this.sumTotalMoney = sumTotalMoney;
    }
}
