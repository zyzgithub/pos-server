package com.dianba.pos.box.mapper;

import com.dianba.pos.order.po.LifeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoxOrderMapper {

    LifeOrder findByPassportIdAndRfids(@Param("passportId") Long passportId, @Param("rfid") String rfid);
}
