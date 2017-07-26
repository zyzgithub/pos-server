package com.dianba.pos.box.service.impl;

import com.dianba.pos.box.service.BoxItemManager;
import com.dianba.pos.box.vo.BoxItemSearchVo;
import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.repository.PosItemJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class DefaultBoxItemManager implements BoxItemManager {

    @Autowired
    private PosItemJpaRepository posItemJpaRepository;

    @Override
    public List<BoxItemSearchVo> getItemsByCodeOrName(Long passportId, String codeOrName) {
        List<PosItem> posItems;
        if (Pattern.compile("[\u4e00-\u9fa5]").matcher(codeOrName).find()) {
            posItems = posItemJpaRepository.findAllByItemNameLikeAndPassportId(codeOrName, passportId);
        } else {
            codeOrName = "%" + codeOrName + "%";
            posItems = posItemJpaRepository.findAllByBarcodeLikeAndPassportId(codeOrName, passportId);
        }
        List<BoxItemSearchVo> boxItemSearchVos = new ArrayList<>();
        for (PosItem posItem : posItems) {
            BoxItemSearchVo boxItemSearchVo = new BoxItemSearchVo();
            boxItemSearchVo.setItemId(posItem.getId());
            boxItemSearchVo.setItemName(posItem.getItemName());
            BigDecimal itemPrice = BigDecimal.valueOf(posItem.getSalesPrice())
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
            boxItemSearchVo.setItemPrice(itemPrice);
            boxItemSearchVos.add(boxItemSearchVo);
        }
        return boxItemSearchVos;
    }
}
