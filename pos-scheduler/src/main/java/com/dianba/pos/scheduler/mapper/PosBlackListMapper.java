package com.dianba.pos.scheduler.mapper;

import com.dianba.pos.scheduler.vo.ScalpListByPassportVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/7/5.
 */
@Mapper
public interface PosBlackListMapper {

    /**
     * 获取活跃白名单用户
     * @return
     */
    List<Map<String,Object>> findWhiteList();

    /**
     * 获取某个商家是否有刷单行为每笔订单
     * @param passportId
     * @return
     */
    List<ScalpListByPassportVo> findScalpListByPassport(Long passportId);
}
