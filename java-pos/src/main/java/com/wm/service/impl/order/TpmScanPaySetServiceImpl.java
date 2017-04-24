package com.wm.service.impl.order;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.service.order.TpmScanPaySetServiceI;
@Service("tpmScanPaySetService")
@Transactional
public class TpmScanPaySetServiceImpl extends CommonServiceImpl implements TpmScanPaySetServiceI{
	private static final Logger logger = LoggerFactory.getLogger(TpmScanPaySetServiceImpl.class);
	@Override
	public void updateEveryDayCount(Integer merchantId) {
		try{
			String sql ="UPDATE tpm_scanpay_set SET everyday_count = everyday_count - 1 WHERE id = ( SELECT a.id FROM ( SELECT tssm.scanpay_id AS id FROM tpm_scanpay_set_merchant AS tssm LEFT JOIN tpm_scanpay_set AS tss ON tssm.scanpay_id = tss.id WHERE merchant_id = ? AND UNIX_TIMESTAMP(NOW()) BETWEEN UNIX_TIMESTAMP(tss.begin_time) AND UNIX_TIMESTAMP(tss.end_time) AND tss.is_delete = '0' ) a )";
			this.executeSql(sql, merchantId);
		}catch(Exception e){
			logger.error("商家扫码设置有问题--错误表 tpm_scanpay_set   商家ID:"+merchantId);
		}
		
	}

}
