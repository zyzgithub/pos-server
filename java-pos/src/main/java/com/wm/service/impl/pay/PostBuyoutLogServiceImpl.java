package com.wm.service.impl.pay;

import com.wm.dto.post.BuyoutLogDto;
import com.wm.entity.pay.PostBuyoutEntity;
import com.wm.entity.pay.PostBuyoutEntityLog;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.impl.flow.FlowVo;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchantinfo.MerchantInfoServiceI;
import com.wm.service.pay.PostBuyoutLogService;
import com.wm.util.DefaultDecimalFormat;
import jeecg.system.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mjorcen on 16/8/8.
 */
@Service("postBuyoutLogServiceImpl")
@Transactional
public class PostBuyoutLogServiceImpl extends CommonServiceImpl implements PostBuyoutLogService {

    @Autowired
    private UserService userService;
    @Autowired
    private MerchantServiceI merchantServiceI;
    @Autowired
    private MerchantInfoServiceI merchantInfoServiceI;
    @Autowired
    private FlowServiceI flowService;


    @Override
    public void payBuyoutLogs() throws Exception {
        List<PostBuyoutEntityLog> logs = getUnpayPostBuyoutEntityLogs(null);
        if (!CollectionUtils.isEmpty(logs)) {
            for (PostBuyoutEntityLog log : logs) {
                FlowVo f = flowService.payPOSBuyout(log.getUserId().intValue(), log.getPaymoney().doubleValue(), log.getId().intValue(), log.getPayIndex());
                if (f != null) {
                    payBuyoutLog(log);
                }
            }

        }

    }

    @Override
    public List<PostBuyoutEntityLog> getUnpayPostBuyoutEntityLogs(Long userId) {
        String hql = "from " + PostBuyoutEntityLog.class.getName() + " where needPayTime <= UNIX_TIMESTAMP(now()) and payState = 0  ";
        if (userId == null) {
            return findHql(hql);
        } else {
            hql = hql + "and userId = ? ";
            return findHql(hql, userId);
        }
    }

    @Override
    public void payBuyoutLog(PostBuyoutEntityLog log) {
        log.setRealPayTime(System.currentTimeMillis() / 1000);
        log.setPayState(1);
        this.commonDao.updateEntitie(log);
        PostBuyoutEntity entity = getEntity(PostBuyoutEntity.class, log.getPostBuyoutId());
        entity.setPaymoney(entity.getPaymoney().add(log.getPaymoney()));

        Long v = entity.getValidity() * 1000;
        Date date = new Date(v);
        date = DateUtils.addDays(date, log.getValidityMonth() * 30);
        entity.setValidity(date.getTime() / 1000);
        entity.setPayNum(entity.getPayNum() + 1);

        this.commonDao.updateEntitie(entity);
    }


    @Override
    public void savePostBuyoutLog(Long userId, Integer payType, Long postBuyoutId, BigDecimal money, Integer itemId) {
        Date date = new Date();

        Long now = System.currentTimeMillis() / 1000;
        for (int i = 0; i < payType; i++) {
            Long needpayTime = DateUtils.addDays(date, i * 30).getTime() / 1000;

//            if (firstDiscount && i == 0) {
//                needpayTime = DateUtils.addDays(date, 5).getTime() / 1000;
//            }

            PostBuyoutEntityLog log = new PostBuyoutEntityLog();
            log.setUserId(userId);
            log.setPayState(0);
            log.setCreateTime(now);
            log.setNeedPayTime(needpayTime);
            log.setPayIndex(i + 1 + "/" + payType);
            log.setPaymoney(money);
            log.setPostBuyoutId(postBuyoutId);

            // 如果是硬件, 购买成功后就是永久的了. 所有设置100年,
            if (itemId == 1 && i == 11) {
                log.setValidityMonth(12 * 100);
            } else {
                log.setValidityMonth(1);
            }
            commonDao.save(log);
        }

    }

    @Override
    public List<BuyoutLogDto> getBuyoutLogDtos(Long userId, Integer state) {
        String hql = "from " + PostBuyoutEntityLog.class.getName() + " where userId = ? ";
        List<PostBuyoutEntityLog> logs;
        if (state == null) {
            logs = findHql(hql, userId);
        } else {
            hql = hql + " and pay_state = ? ";
            logs = findHql(hql, userId, state);
        }
        return turn(logs);
    }

    private List<BuyoutLogDto> turn(List<PostBuyoutEntityLog> logs) {
        List<BuyoutLogDto> vos = new LinkedList<>();

        for (PostBuyoutEntityLog log : logs) {
            vos.add(turn(log));
        }
        return vos;
    }

    private BuyoutLogDto turn(PostBuyoutEntityLog log) {
        BuyoutLogDto vo = new BuyoutLogDto();
        vo.setNeedPayTime(log.getNeedPayTime());
        vo.setState(log.getPayState());
        vo.setRealPayTime(log.getRealPayTime());
        vo.setNumber("-"+DefaultDecimalFormat.formatHalfUp(log.getPaymoney()));

        String[] mutilLine = log.getPayIndex().split("/");
        vo.setDescLine(mutilLine[0] + "期");

        if (Integer.parseInt(mutilLine[1]) == 1) {
            vo.setTitle("POS购买（全额购买) ");
        } else {
            vo.setTitle("POS购买（分期购买）");
        }
        return vo;
    }

}
