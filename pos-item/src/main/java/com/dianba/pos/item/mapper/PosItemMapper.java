package com.dianba.pos.item.mapper;

import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.vo.PosItemVo;
import com.dianba.pos.item.vo.PosTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PosItemMapper {

    List<PosItem> findWarningRepertoryItemsByExclude(@Param("passportId") Long passportId
            , @Param("itemTemplateIds") List<Long> itemTemplateIds);

    List<PosTypeVo> getItemUnitAndType(Long passportId);

    List<PosItemVo> getListBySearchText(@Param("searchText") String searchText,@Param("passportId") Long passportId);

    Integer getCountByItemType(@Param("passportId") Long passportId,@Param("itemTypeId") Long itemTypeId);
}
