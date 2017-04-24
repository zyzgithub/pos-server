package com.sms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.wm.util.IntegerUtil;
import com.wm.util.StringUtil;
import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.openservices.ons.api.Message;
import com.base.config.EnvConfig;
import com.dianba.mq.api.CommTag;
import com.dianba.mq.api.model.Msg;
import com.dianba.mq.producer.MqProducer;
import com.dianba.mq.util.ObjectAndByte;
import com.sms.service.SmsServiceI;
import com.validate.util.RandomValidateUtil;
import com.wm.controller.user.AccountGenerator;
import com.wm.util.AliOcs;

import java.util.Date;

/**
 * @author wuyong
 * @version V1.0
 * @Title: Controller
 * @Description: 短信
 * @date 2015-01-19 10:09:46
 */
@Controller
@RequestMapping("ci/smsController")
public class SmsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsServiceI smsService;
    @Autowired
    private SystemService systemService;

    /**
     * 添加短信
     *
     * @param phone ,content,type:1-营销短信，2-验证短信，3-团购券通知短信
     * @return
     */
    @RequestMapping(params = "sendSms")
    @ResponseBody
    public AjaxJson sendSms(String phone, String content, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String randomNum = AccountGenerator.getRandomNum(4);

            content = "您好，您的验证码是：" + randomNum;
//

            HttpSession session = request.getSession();
            session.removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
            session.setAttribute(RandomValidateUtil.RANDOMCODEKEY, randomNum);

            // 消息内容，可自定义，由推送方和接收方协商定义
            logger.info("sendSms phone:{}, content:{}", phone, content);
            Msg shortMsg = new Msg(phone, content);
            // MQ消息体
            Message msg = new Message(EnvConfig.mq.TopicMsg, CommTag.MQ_MSG_TAG_SHORTMSG, ObjectAndByte.toByteArray(shortMsg));
            // 推送消息
            MqProducer.send(msg);
            systemService.addLog(phone + content, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
            j.setSuccess(true);
            j.setMsg("发送成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            j.setSuccess(false);
            j.setMsg("发送失败");
        }
        return j;
    }

    /**
     * 发送短信改用memchached不存的时候采用session
     *
     * @param phone   手机
     * @param content 内容
     * @param second  秒（缓存时间）
     * @param length  验证码长度
     * @param request
     * @return
     */
    @RequestMapping(params = "sendSmsValidateCode")
    @ResponseBody
    public AjaxJson sendSmsValidateCode(String phone, String content, @RequestParam(defaultValue = "60") Integer second,
                                        @RequestParam(defaultValue = "4") Integer length, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String randomNum = AccountGenerator.getRandomNum(length);
            content = "您好，您的验证码是：" + randomNum;

//            String sendNum = AliOcs.get("sendSms_"+phone);
//            String mkey = "sendSms_"+phone;
//            if(StringUtil.isEmpty(sendNum)){
//                AliOcs.set(mkey, 1, 24 * 60 * 60);
//            }else{
//                AliOcs.set(mkey, Integer.parseInt(sendNum)+1);
//                if(Integer.parseInt(sendNum)+1>10){
//                    j.setSuccess(false);
//                    j.setMsg("发送次数过多");
//                    return j;
//                }
//            }

            HttpSession session = request.getSession();
            session.removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
            session.setAttribute(RandomValidateUtil.RANDOMCODEKEY, randomNum);
            logger.info("sendSmsValidateCode phone:{}, content:{}", phone, content);
            Msg shortMsg = new Msg(phone, content);
            Message msg = new Message(EnvConfig.mq.TopicMsg, CommTag.MQ_MSG_TAG_SHORTMSG, ObjectAndByte.toByteArray(shortMsg));
            MqProducer.send(msg);
            //为了merchant_center项目校验需要
            AliOcs.set(phone, randomNum, second);
            systemService.addLog(phone + content, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
            j.setObj(randomNum);
            j.setSuccess(true);
            j.setMsg("发送成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            j.setSuccess(false);
            j.setMsg("发送失败");
        }
        return j;
    }

}
