package com.wm.service.pay;

import com.wm.dto.post.BuyoutLogDto;
import com.wm.entity.pay.PostBuyoutEntityLog;
import org.jeecgframework.core.common.service.CommonService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mjorcen on 16/8/8.
 */
public interface PostBuyoutLogService extends CommonService {

    void payBuyoutLogs() throws Exception;

    List<PostBuyoutEntityLog> getUnpayPostBuyoutEntityLogs(Long userId);

    void payBuyoutLog(PostBuyoutEntityLog log);

    void savePostBuyoutLog(Long userId, Integer payType, Long postBuyoutId, BigDecimal money, Integer itemId);

    List<BuyoutLogDto> getBuyoutLogDtos(Long userId, Integer state);
}
