package com.wm.service.impl.pay;

import com.wm.dto.post.BuyoutDetailDto;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.pay.PostBuyoutEntity;
import com.wm.exception.ApplicationRuntimeException;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchantinfo.MerchantInfoServiceI;
import com.wm.service.pay.PostBuyoutLogService;
import com.wm.service.pay.PostBuyoutService;
import jeecg.system.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by mjorcen on 16/8/8.
 */
@Service("postBuyoutPayService")
@Transactional
public class PostBuyoutServiceImpl extends CommonServiceImpl implements PostBuyoutService {

    @Autowired
    private UserService userService;
    @Autowired
    private MerchantServiceI merchantServiceI;
    @Autowired
    private MerchantInfoServiceI merchantInfoServiceI;
    @Autowired
    private FlowServiceI flowService;

    @Autowired
    private PostBuyoutLogService buyoutLogService;


    @Override
    public Long getBuyoutValidity(Long userId, Integer item) {
        Long now = System.currentTimeMillis() / 1000;
        List<PostBuyoutEntity> postBuyoutEntityList = commonDao.findHql("from " + PostBuyoutEntity.class.getName() + " where userId = ? " + " and item = ?  and  end_time >= ? and start_time <= ? order by  end_time desc  ", userId, item, now, now);

        if (CollectionUtils.isEmpty(postBuyoutEntityList)) {
            return 0L;
        }
        Long v = postBuyoutEntityList.get(0).getValidity();
        return v;
    }

    @Override
    public List<Map<String, String>> buyout(Long userId, List<BuyoutDetailDto> detailDtoList) {
        List<Map<String, String>> list = new LinkedList<>();
        for (BuyoutDetailDto detailDto : detailDtoList) {
            PostBuyoutEntity postBuyoutEntity;
            if (detailDto != null && detailDto.getItemId() == 1) {
                postBuyoutEntity = hardwareBuyout(userId, detailDto.getPayType());
            } else {
                postBuyoutEntity = softwareBuyout(userId, detailDto.getPayType());
            }

            if (postBuyoutEntity != null) {
                Map<String, String> map = formatResult(postBuyoutEntity);
                list.add(map);
            }
        }
        return list;
    }

    private Map<String, String> formatResult(PostBuyoutEntity postBuyoutEntity) {
        BigDecimal totalMoney = postBuyoutEntity.getTotalmoney().setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, String> map = new HashMap<>();
        map.put("totalMoney", "¥" + totalMoney.toString());
        map.put("itemId", postBuyoutEntity.getItem() + "");
        String subMoney = totalMoney.divide(BigDecimal.valueOf(postBuyoutEntity.getPayType()), 2, BigDecimal.ROUND_HALF_UP).toString();
        if (postBuyoutEntity.getPayType() == 1) {
            map.put("payTypeTitle", "全额购买 (含手续费）");
        } else {
            map.put("payTypeTitle", "<font color='#ff8908'>¥" + subMoney + "</font> x" + postBuyoutEntity.getPayType() + "期 (含手续费）");
        }
        return map;
    }


    @Override
    public Map<Integer, BuyoutDetailDto> gethardwareData() {
        Map<Integer, BuyoutDetailDto> map = new LinkedHashMap<>();
        BuyoutDetailDto detailDto = new BuyoutDetailDto();
        detailDto.setPayType(0);
        detailDto.setMoney("3980");
        detailDto.setTopic("");
        detailDto.setPayName("原价");
        detailDto.setUnit("元");
        detailDto.setItemId(1);
        detailDto.setImage("http://qidianimg.oss-cn-shenzhen.aliyuncs.com/merchants/pos/POSBuyoutDetailTopic.png");
        map.put(0, detailDto);


        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(1);
        detailDto.setMoney("2980");
        detailDto.setTopic("限时优惠");
        detailDto.setPayName("一次性购买");
        detailDto.setUnit("元");
        detailDto.setItemId(1);
        map.put(1, detailDto);

        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(12);
        detailDto.setMoney("331.67");
        detailDto.setTopic("");
        detailDto.setPayName("分期购买 (分12期)");
        detailDto.setUnit("元/月");
        detailDto.setItemId(1);
        map.put(12, detailDto);

        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(24);
        detailDto.setMoney("191.25");
        detailDto.setTopic("推荐");
        detailDto.setPayName("分期购买 (分24期)");
        detailDto.setUnit("元/月");
        detailDto.setItemId(1);
        map.put(24, detailDto);

        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(36);
        detailDto.setMoney("138.33");
        detailDto.setTopic("");
        detailDto.setPayName("分期购买 (分36期)");
        detailDto.setUnit("元/月");
        detailDto.setItemId(1);
        map.put(36, detailDto);
        return map;
    }


    @Override
    public Map<Integer, BuyoutDetailDto> getSoftwareData() {
        Map<Integer, BuyoutDetailDto> map = new LinkedHashMap<>();
        BuyoutDetailDto detailDto = new BuyoutDetailDto();
        detailDto.setPayType(0);
        detailDto.setMoney("2680");
        detailDto.setTopic("");
        detailDto.setPayName("原价");
        detailDto.setUnit("元/年");
        detailDto.setItemId(2);
        detailDto.setImage("http://love.doghouse.com.tw/image/wallpaper/011102/bf1554.jpg");
        map.put(0, detailDto);

        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(1);
        detailDto.setMoney("1680");
        detailDto.setTopic("限时优惠");
        detailDto.setPayName("一次性购买");
        detailDto.setUnit("元/年");
        detailDto.setItemId(2);
        map.put(1, detailDto);

        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(6);
        detailDto.setMoney("223.67");
        detailDto.setTopic("");
        detailDto.setPayName("分期购买 (分6期)");
        detailDto.setUnit("元/月");
        detailDto.setItemId(2);
        map.put(6, detailDto);

        detailDto = new BuyoutDetailDto();
        detailDto.setPayType(12);
        detailDto.setMoney("155.67");
        detailDto.setTopic("");
        detailDto.setPayName("分期购买 (分12期)");
        detailDto.setUnit("元/月");
        detailDto.setItemId(2);
        map.put(12, detailDto);


        return map;
    }

    @Override
    public List<Map<String, String>> buyouted(Long userId) {
        List<PostBuyoutEntity> postBuyoutEntityList = commonDao.findHql("from " + PostBuyoutEntity.class.getName() + " where userId = ?", userId );
        List<Map<String,String>> result = new LinkedList<>();
        if(CollectionUtils.isEmpty(postBuyoutEntityList)){
            return Collections.EMPTY_LIST;
        }

        for (PostBuyoutEntity entity : postBuyoutEntityList ){
            Map<String ,String> map = formatResult(entity);
            result.add(map);
        }
        return result;
    }


    private PostBuyoutEntity hardwareBuyout(Long userId, Integer payType) {
        Integer item = 1;
        BigDecimal totalmoney = getTotalMoney(item, payType);

        MerchantEntity merchantEntity = merchantServiceI.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId.intValue());
        if (merchantEntity == null) {
            return null;
        }

        PostBuyoutEntity postBuyoutEntity = savePostBuyoutAndLog(userId, merchantEntity.getId().longValue(), payType, item, totalmoney);
        return postBuyoutEntity;
    }

    private PostBuyoutEntity softwareBuyout(Long userId, Integer payType) {
        Integer item = 2;
        BigDecimal totalmoney = getTotalMoney(item, payType);

        MerchantEntity merchantEntity = merchantServiceI.findUniqueByProperty(MerchantEntity.class, "wuser.id", userId.intValue());
        if (merchantEntity == null) {
            return null;
        }

        PostBuyoutEntity postBuyoutEntity = savePostBuyoutAndLog(userId, merchantEntity.getId().longValue(), payType, item, totalmoney);
        return postBuyoutEntity;
    }


    private PostBuyoutEntity savePostBuyoutAndLog(Long userId, Long merchantId, Integer payType, Integer item, BigDecimal totalmoney) {
        Date date = new Date();
        PostBuyoutEntity postBuyoutEntity = new PostBuyoutEntity();
        postBuyoutEntity.setItem(item);
        postBuyoutEntity.setCreateTime(date.getTime() / 1000);
        postBuyoutEntity.setMerchantId(merchantId);
//      postBuyoutEntity.setPayState(0);
        postBuyoutEntity.setUserId(userId);
        postBuyoutEntity.setPayNum(0);
        postBuyoutEntity.setPayType(payType);
        postBuyoutEntity.setTotalmoney(totalmoney);
        postBuyoutEntity.setPaymoney(BigDecimal.ZERO);

        postBuyoutEntity.setDescription("post服务购买 , 付款方式 : " + payType);


        List<PostBuyoutEntity> postBuyoutEntityList = commonDao.findHql("from " + PostBuyoutEntity.class.getName() + " where userId = ? " + " and item = ?  and end_time >= ? order by  end_time desc  ", userId, item, date.getTime() / 1000);
        Long startTime = date.getTime() / 1000;

        boolean firstBuyout = CollectionUtils.isEmpty(postBuyoutEntityList);

        if (!firstBuyout) {
            if (item == 1) {
                throw new ApplicationRuntimeException(01, "硬件已购买");
            }

            startTime = postBuyoutEntityList.get(0).getEndTime() + 1;
        }

        // 第一次购买+5天的付费时间
//        postBuyoutEntity.setValidity(CollectionUtils.isEmpty(postBuyoutEntityList) ? DateUtils.addDays(date, 5).getTime() / 1000 : date.getTime() / 1000);
        postBuyoutEntity.setValidity(date.getTime() / 1000);

        postBuyoutEntity.setStartTime(startTime);

        if (item == 2) {
            postBuyoutEntity.setEndTime(DateUtils.addYears(date, 1).getTime() / 1000);
        } else {
            postBuyoutEntity.setEndTime(DateUtils.addYears(date, 500).getTime() / 1000);
        }
        save(postBuyoutEntity);
        buyoutLogService.savePostBuyoutLog(userId, payType, postBuyoutEntity.getId(), new BigDecimal(gethardwareData().get(payType).getMoney()), item);

        return postBuyoutEntity;
    }

    private BigDecimal getTotalMoney(Integer item, Integer payType) {
        if (item == 1) {
            if (gethardwareData().get(payType) == null) {
                throw new ApplicationRuntimeException(02, "没有对应的支付类型.");
            }
            return new BigDecimal(gethardwareData().get(payType).getMoney()).multiply(BigDecimal.valueOf(payType));
        } else {
            if (getSoftwareData().get(payType) == null) {
                throw new ApplicationRuntimeException(02, "没有对应的支付类型.");
            }
            return new BigDecimal(getSoftwareData().get(payType).getMoney()).multiply(BigDecimal.valueOf(payType));
        }
    }

}
