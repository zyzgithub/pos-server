package com.dianba.pos.box.mapper;

import com.dianba.pos.order.po.LifeOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoxOrderMapper {

    LifeOrder findByPassportIdAndRfids(Long passportId, String rfid);
}
