package com.dianba.pos.box.po;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "life_pos.box_door_info")
public class BoxDoorInfo implements Serializable {

    @Id
    @Column(name = "access_sn")
    private String accessSN;
    @Column(name = "passport_id")
    private Long passportId;

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }

    public String getAccessSN() {
        return accessSN;
    }

    public void setAccessSN(String accessSN) {
        this.accessSN = accessSN;
    }
}
