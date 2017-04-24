package com.wm.service.impl.bank;



import java.util.List;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.controller.takeout.vo.BankCardVo;
import com.wm.service.bank.BankcardServiceI;

@Service("bankcardService")
@Transactional
public class BankcardServiceImpl extends CommonServiceImpl implements BankcardServiceI {

	@Override
	public List<BankCardVo> queryByDefault(int userId, String defaultStr,int page,int rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select bc.user_id as userId, bc.bank_id as bankId, bc.card_no as cardNo, ")
				.append(" bc.default as defaultStr, bc.name as name, bc.phone as phone, bc.source_bank as sourceBank, bc.bank_img_url as bankImgUrl  ")
				.append(" FROM bank_card as bc").append(" WHERE ")
				.append(" bc.user_id = ? and bc.default = ? ");
		List<BankCardVo> orvList = this.findObjForJdbc(sbsql.toString(), page, rows, BankCardVo.class, userId, defaultStr);
		return orvList;
	}

	@Override
	public BankCardVo getDefaultBankCard(int userId) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select bc.user_id as userId, bc.id as bankId, bc.card_no as cardNo, ")
				.append(" bc.default as defaultStr, bc.name as name, bc.phone as phone, bc.source_bank as sourceBank, bc.bank_img_url as bankImgUrl, b.name as bankName, b.bank_code as bankCode "
						+ "from bank_card as bc, bank b where bc.user_id = ? and bc.default = 'Y' and bc.bank_id = b.id ");
		List<BankCardVo> bankCards = findObjForJdbc(sbsql.toString(), BankCardVo.class, userId);
		return (bankCards == null || bankCards.isEmpty()) ? null : bankCards.get(0);
	}
}