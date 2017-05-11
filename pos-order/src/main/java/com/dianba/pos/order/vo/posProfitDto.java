package com.dianba.pos.order.vo;

/**
 * Created by Administrator on 2017/5/10 0010.
 */
public class posProfitDto {

    private Integer id;

    //订单时间
    private String dateTime;

    private Double sale_price;

    private  Double stock_price;

    private  Double order_profit;

    private String order_type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getSale_price() {
        return sale_price;
    }

    public void setSale_price(Double sale_price) {
        this.sale_price = sale_price;
    }

    public Double getStock_price() {
        return stock_price;
    }

    public void setStock_price(Double stock_price) {
        this.stock_price = stock_price;
    }

    public Double getOrder_profit() {
        return order_profit;
    }

    public void setOrder_profit(Double order_profit) {
        this.order_profit = order_profit;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }
}
