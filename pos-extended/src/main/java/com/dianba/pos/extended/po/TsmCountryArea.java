package com.dianba.pos.extended.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/10 0010.
 */
@Entity
@Table(name = "tsm_country_area")
public class TsmCountryArea implements Serializable {


    @Id
    @GeneratedValue
    @Column(name = "code")
    private Integer code;

    @Column(name = "name")
    private String name;

    @Column(name = "all_pin_yin")
    private String allPinYin;

    @Column(name = "parent_code")
    private Integer parentCode;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllPinYin() {
        return allPinYin;
    }

    public void setAllPinYin(String allPinYin) {
        this.allPinYin = allPinYin;
    }

    public Integer getParentCode() {
        return parentCode;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }
}
