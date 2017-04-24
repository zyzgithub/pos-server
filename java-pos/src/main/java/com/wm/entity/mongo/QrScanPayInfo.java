package com.wm.entity.mongo;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.alibaba.fastjson.JSONObject;

@Document(collection = "qr_scan_pay_info")
public class QrScanPayInfo {
    @Id public String Id;
    
    public JSONObject payInfo;
}
