package com.wm.service.pay;

import com.wm.dto.post.BuyoutDetailDto;
import org.jeecgframework.core.common.service.CommonService;

import java.util.List;
import java.util.Map;

/**
 * Created by mjorcen on 16/8/8.
 */
public interface PostBuyoutService extends CommonService {


    Long getBuyoutValidity(Long userId, Integer item);

    List<Map<String, String>> buyout(Long userId, List<BuyoutDetailDto> detailDtoList);

    Map<Integer, BuyoutDetailDto> gethardwareData();

    Map<Integer, BuyoutDetailDto> getSoftwareData();

    List<Map<String,String>> buyouted(Long userId);
}
