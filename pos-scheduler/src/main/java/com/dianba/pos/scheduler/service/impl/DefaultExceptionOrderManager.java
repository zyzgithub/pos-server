package com.dianba.pos.scheduler.service.impl;

import com.dianba.pos.common.util.JiGuangSend;
import com.dianba.pos.passport.po.PosBlackList;
import com.dianba.pos.passport.po.PosCashierAccount;
import com.dianba.pos.passport.repository.PosBlackListJpaRepository;
import com.dianba.pos.passport.service.PosCashierAccountManager;
import com.dianba.pos.scheduler.mapper.PosBlackListMapper;
import com.dianba.pos.scheduler.service.ExceptionOrderManager;
import com.dianba.pos.scheduler.vo.ScalpListByPassportVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/7/5.
 */
@Service
public class DefaultExceptionOrderManager implements ExceptionOrderManager {

    @Autowired
    private PosBlackListMapper posBlackListMapper;

    @Autowired
    private PosBlackListJpaRepository posBlackListJpaRepository;

    @Autowired
    private PosCashierAccountManager posCashierAccountManager;
    private Logger logger = LogManager.getLogger(DefaultExceptionOrderManager.class);
    @Override
    public void checkBlackPassport() {

        //获取白名单商家
        List<Map<String,Object>> longMap=posBlackListMapper.findWhiteList();

        int i=0;
        for(Map<String,Object> map : longMap){

            for (String k : map.keySet()){
                System.out.println(k + " : " + map.get(k));
                List<ScalpListByPassportVo> maps=posBlackListMapper.findScalpListByPassport(
                        Long.parseLong(map.get(k).toString()));
                for (ScalpListByPassportVo sb : maps){
                    if(sb.getSeconds()!=null){
                        logger.info("passportId:"+sb.getPassportId()+"======seconds : " + sb.getSeconds());
                        if(sb.getSeconds() < 13){
                            i++;
                            logger.info("商家正在刷单:"+i);
                            if(i>4){
                                PosBlackList posBlackList=new PosBlackList();
                                posBlackList.setPassportId(sb.getPassportId());
                                posBlackListJpaRepository.save(posBlackList);
                                //推送给商家告诉商家账号被拉黑
                                List<PosCashierAccount> lst=posCashierAccountManager.findAllByMerchantId(
                                        sb.getPassportId());
                                for(PosCashierAccount posCashierAccount : lst){
                                    String result= JiGuangSend.sendPushWithAlias(posCashierAccount.getCashierId()
                                   .toString(),"您的账号存在刷单行为,已被拉黑,如需了解详情,请联系点吧客服了解具体情况");
                                    logger.info("=====账号{0}=====拉黑推送返回:",sb.getPassportId()+result);
                                }
                                i=0;
                                break;
                            }
                        }else if(i>4){
                            logger.info("此商家存在刷单行为");
                            PosBlackList posBlackList=new PosBlackList();
                            posBlackList.setPassportId(sb.getPassportId());
                            posBlackListJpaRepository.save(posBlackList);
                            i=0;
                            break;
                        }else {
                            logger.info("刷单行为停止,当前已刷次数:"+i);
                            i=0;
                        }
                    }

                }
            }
        }
    }
}
