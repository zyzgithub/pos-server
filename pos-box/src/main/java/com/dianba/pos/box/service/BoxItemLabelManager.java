package com.dianba.pos.box.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.box.po.BoxItemLabel;
import com.dianba.pos.box.vo.BoxItemVo;

import java.util.List;

public interface BoxItemLabelManager {

    BasicResult showItemsByRFID(Long passportId, String rfids);

    List<BoxItemVo> getItemsByRFID(Long passportId, String rfids, boolean excludePaid);

    List<BoxItemLabel> getRFIDItems(String rfids);

    List<BoxItemLabel> getItemsToBindingByRFID(String rfids);

    List<BoxItemLabel> updateItemLabelToPaid(String rfids);

    void bindItemLabelToItems(Long itemId, String rfids);

    boolean isAllPaid(String rfids);
}
