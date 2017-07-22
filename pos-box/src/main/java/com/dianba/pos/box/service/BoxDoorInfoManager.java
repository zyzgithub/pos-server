package com.dianba.pos.box.service;

import com.dianba.pos.box.po.BoxDoorInfo;

public interface BoxDoorInfoManager {

    BoxDoorInfo getDoorInfoByPassportId(Long passportId);

    BoxDoorInfo getDoorInfoByAccessSN(String accessSN);
}
