package com.wm.dto.post;

import java.util.List;

/**
 * Created by mjorcen on 16/8/8.
 */
public class BuyoutDetailContextDto {
    private String title = "3980";
    private String image = "http://love.doghouse.com.tw/image/wallpaper/011102/bf1554.jpg";
    private String warnLine = "分期金额将在每月15个工作日从账户余额中扣除。每月月初开始冻结分期金额等同账户余额，如带来不便，敬请见谅。";

    private List<BuyoutDetailDto> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWarnLine() {
        return warnLine;
    }

    public void setWarnLine(String warnLine) {
        this.warnLine = warnLine;
    }

    public List<BuyoutDetailDto> getItems() {
        return items;
    }

    public void setItems(List<BuyoutDetailDto> items) {
        this.items = items;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "BuyoutDetailContextDto{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", warnLine='" + warnLine + '\'' +
                ", items=" + items +
                '}';
    }
}
