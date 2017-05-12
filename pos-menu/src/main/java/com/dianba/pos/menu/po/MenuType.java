package com.dianba.pos.menu.po;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "menu_type")
public class MenuType implements Serializable {

    /**
     * id
     */
    private Integer id;
    /**
     * 分类名
     */
    private String name;
    /**
     * 排序序号，从小到大排序
     */
    private Integer sortNum;
    /**
     * 商家ID
     */
    private Integer merchantId;
    /**
     * createTime
     */
    private Integer createTime;
    /**
     * 打包费
     */
    private Double costLunchBox;

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, precision = 19, scale = 0)
    public Integer getId() {
        return this.id;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 方法: 取得String
     *
     * @return: String  分类名
     */
    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    /**
     * 方法: 设置String
     *
     * @param: String  分类名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  排序序号，从小到大排序
     */
    @Column(name = "SORT_NUM", nullable = true, precision = 10, scale = 0)
    public Integer getSortNum() {
        return this.sortNum;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  排序序号，从小到大排序
     */
    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  商家ID
     */
    @Column(name = "MERCHANT_ID", nullable = false, precision = 19, scale = 0)
    public Integer getMerchantId() {
        return this.merchantId;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  商家ID
     */
    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * 方法: 取得Integer
     *
     * @return: Integer  createTime
     */
    @Column(name = "CREATE_TIME", nullable = true, precision = 10, scale = 0)
    public Integer getCreateTime() {
        return this.createTime;
    }

    /**
     * 方法: 设置Integer
     *
     * @param: Integer  createTime
     */
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    /**
     * 方法: 取得Double
     *
     * @return: Double  costLunchBox
     */
    @Column(name = "cost_lunch_box", nullable = false, precision = 10, scale = 2)
    public Double getCostLunchBox() {
        return costLunchBox;
    }

    /**
     * 方法: 设置Double
     *
     * @param: Double  costLunchBox
     */
    public void setCostLunchBox(Double costLunchBox) {
        this.costLunchBox = costLunchBox;
    }

    @Override
    public String toString() {
        return "MenutypeEntity [id=" + id + ", name=" + name + ", sortNum=" + sortNum + ", merchantId=" + merchantId
                + ", createTime=" + createTime + ", costLunchBox=" + costLunchBox + "]";
    }

}
