package com.dianba.pos.passport.po;

import javax.persistence.*;

@Entity
@Table(name = "life_pos.pos_black_list")
public class PosBlackList {

    @Id
    @Column(name = "passport_id")
    private Long passportId;

    public Long getPassportId() {
        return passportId;
    }

    public void setPassportId(Long passportId) {
        this.passportId = passportId;
    }
}
