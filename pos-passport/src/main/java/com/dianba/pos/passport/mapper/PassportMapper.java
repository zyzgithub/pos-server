package com.dianba.pos.passport.mapper;

import com.dianba.pos.passport.po.Passport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zhangyong on 2017/6/1.
 */
@Mapper
public interface PassportMapper{

    Passport getPassportInfoByCashierId(@Param("cashierId") Long cashierId);


}
