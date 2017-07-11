package com.dianba.pos.box.service;

import com.dianba.pos.box.vo.BoxItemSearchVo;

import java.util.List;

public interface BoxItemManager {

    List<BoxItemSearchVo> getItemsByCodeOrName(Long passportId, String codeOrName);
}
