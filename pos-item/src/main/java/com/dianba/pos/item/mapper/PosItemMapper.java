package com.dianba.pos.item.mapper;

import com.dianba.pos.item.po.PosItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PosItemMapper {

    List<PosItem> findWarningRepertoryItemsByExclude(@Param("passportId") Long passportId
            , @Param("itemTemplateIds") List<Long> itemTemplateIds);
}
