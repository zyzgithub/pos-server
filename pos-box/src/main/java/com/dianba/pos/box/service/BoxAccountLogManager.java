package com.dianba.pos.box.service;

public interface BoxAccountLogManager {

    void saveOpenLog(String openId);

    void saveLeaveLog(String openId);
}
