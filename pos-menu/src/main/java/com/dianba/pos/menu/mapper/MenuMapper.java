package com.dianba.pos.menu.mapper;

import com.dianba.pos.menu.vo.MenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {


    /***
     * 获取增值服务商品列表
     * @param merchantId
     * @param phone
     * @param type
     * @return
     */
    List<MenuDto> getMenuListByPhoneAndType(
            @Param("merchantId") Long merchantId, @Param("phone") Long phone, @Param("type") int type);
}

