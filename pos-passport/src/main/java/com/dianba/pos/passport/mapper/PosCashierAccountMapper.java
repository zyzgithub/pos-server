package com.dianba.pos.passport.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Mapper
public interface PosCashierAccountMapper {

    void  addPosCashierAccount(@Param("merchantId") Long merchantId,@Param("cashierId") Long cashierId);
}
