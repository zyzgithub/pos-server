package com.dianba.pos.casher.vo;

/**
 * Created by Administrator on 2017/5/2 0002.
 * 封装商家每天营业数据
 */
public class PosProfitByDayEntity
{

    private Integer id;

    //订单时间
    private String dateTime;

    private Double orderMoney;

    private  Double stockMoney;

    private  Double orderProfit;

    private String orderType;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
