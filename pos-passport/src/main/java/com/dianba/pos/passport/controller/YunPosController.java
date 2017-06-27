package com.dianba.pos.passport.controller;

import com.dianba.pos.passport.po.PosLog;
import com.dianba.pos.passport.repository.YunPosJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangyong on 2017/6/13.
 */
@Controller
@RequestMapping("/yunpos/")
public class YunPosController {
    @Autowired
    private YunPosJpaRepository yunPosJpaRepository;
    @ResponseBody
    @RequestMapping(params = "addPosLog")
    public void addPosLog(String title,String content){

        PosLog yl=new PosLog();
        yl.setTitle(title);
        yl.setContent(content);
        yunPosJpaRepository.save(yl);
    }
}
