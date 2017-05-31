package com.dianba.pos.menu.po;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by zhangyong on 2017/5/26.
 */
@Entity
@Table(name = "pos_type",schema = "lefe_pos")
public class PosType implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "item_type_id")
    private Long itemTypeId;

    @Column(name = "item_type_title")
    private String itemTypeTitle;
    public Long getId() {
        return id;
    }

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public Long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemTypeTitle() {
        return itemTypeTitle;
    }

    public void setItemTypeTitle(String itemTypeTitle) {
        this.itemTypeTitle = itemTypeTitle;
    }
}
