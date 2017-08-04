package com.dianba.pos.passport.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangyong on 2017/7/19.
 */
@Entity
@Table(name = "life_pos.pos_black_set")
public class PosBlackSet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "check_time")
    private Integer checkTime;
    @Column(name = "brush_count")
    private Integer brushCount;
    @Column(name = "type")
    private Integer type;
    @Column(name = "create_time")
    private Date createTime=new Date();
    @Column(name = "operator_id")
    private Integer operatorId;
    @Column(name = "state")
    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Integer checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getBrushCount() {
        return brushCount;
    }

    public void setBrushCount(Integer brushCount) {
        this.brushCount = brushCount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
