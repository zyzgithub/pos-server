package com.dianba.pos.casher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bill")
public class BillController {

    @RequestMapping("getbill")
    public String getBill(){
        return "";
    }
}
