package com.wm.service.impl.merchant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.enums.AppTypeConstants;
import com.courier_mana.common.Constants;
import com.jpush.SoundFile;
import com.sms.util.SmsUtil;
import com.team.wechat.util.MapUtil;
import com.wm.controller.takeout.dto.WXHomeDTO;
import com.wm.controller.takeout.vo.MerchantSimpleVo;
import com.wm.controller.takeout.vo.OrderSimpleVo;
import com.wm.dao.address.AddressDao;
import com.wm.dao.category.CategoryDao;
import com.wm.dao.comment.CommentDao;
import com.wm.dao.menu.MenuDao;
import com.wm.dao.merchant.MerchantDao;
import com.wm.entity.bank.BankcardEntity;
import com.wm.entity.category.CategoryEntity;
import com.wm.entity.flow.FlowEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.MerchantStatisticsMenuVo;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.note.NoteEntity;
import com.wm.entity.note.NoteRecordEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.entity.withdrawals.WithdrawalsEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.util.AliOcs;
import com.wm.util.PageList;
import com.wm.util.StringUtil;

@Service("merchantService")
@Transactional
public class MerchantServiceImpl extends CommonServiceImpl implements MerchantServiceI {
	
	private final static Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
	@Autowired
	private SystemService systemService;
	@Autowired
	private MerchantDao merchantDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private FlowServiceI flowService;
	@Autowired
    private JpushServiceI jpushService;
	
	
	
	@Override
	public AjaxJson MerchantTransferAccounts(String userName, double money, int merchantId) {
		AjaxJson j = new AjaxJson();
		WUserEntity user = this.findUniqueByProperty(WUserEntity.class, "username", userName);
		if (user == null) {
			j.setMsg("转账用户不存在");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}
		MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);
		if (merchant == null) {
			j.setMsg("商家不存在");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}

		if (merchant.getWuser().getMoney() < money) {
			j.setMsg("商家金额不足");
			j.setStateCode("01");
			j.setSuccess(false);
			return j;
		}

		// 商家扣钱
		double merchantMoney = merchant.getWuser().getMoney();

		merchant.getWuser().setMoney(merchantMoney - money);

		this.saveOrUpdate(merchant);
		// 添加商家转账流水记录
		FlowEntity merchantFlow = new FlowEntity();
		merchantFlow.setAction("transfer");
		merchantFlow.setCreateTime(DateUtils.getSeconds());
		merchantFlow.setDetail("[商家转账：给" + user.getUsername() + "]");
		merchantFlow.setDetailId(0);
		merchantFlow.setMoney(money);
		merchantFlow.setType("pay");
		merchantFlow.setUserId(merchant.getWuser().getId());

		// 转账用户
		user.setMoney(user.getMoney() + money);
		this.saveOrUpdate(user);
		this.saveOrUpdate(merchantFlow);
		// 添加用户转账收入流水记录
		merchantFlow = new FlowEntity();
		merchantFlow.setAction("transferIncome");
		merchantFlow.setCreateTime(DateUtils.getSeconds());
		merchantFlow.setDetail("[商家:" + merchant.getWuser().getUsername()
				+ "转账," + user.getUsername() + "收入]");
		merchantFlow.setDetailId(0);
		merchantFlow.setMoney(money);
		merchantFlow.setType("income");
		merchantFlow.setUserId(user.getId());
		this.saveOrUpdate(merchantFlow);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}

	@Override
	public AjaxJson merchantWithdraw(int merchantId, double money, int cardId) throws Exception {
		AjaxJson j = new AjaxJson();
		try {
			MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);
			if (merchant == null) {
				j.setStateCode("01");
				j.setMsg("商家不存在");
				j.setSuccess(false);
				return j;
			}
			if (money <= 0) {
				j.setStateCode("01");
				j.setMsg("提现金额不能小于0");
				j.setSuccess(false);
				return j;
			}
			BankcardEntity bandCard = this.get(BankcardEntity.class, cardId);
			if (bandCard == null) {
				j.setStateCode("01");
				j.setMsg("请检查银行卡是否有效");
				j.setSuccess(false);
				return j;
			}
			WUserEntity user = merchant.getWuser();
			if (user.getMoney() < money) {
				j.setStateCode("01");
				j.setMsg("提现金额不能大于余额");
				j.setSuccess(false);
				return j;
			}
			Integer userId = user.getId();
			String sql = "SELECT count(user_id) times FROM withdrawals WHERE DATE(FROM_UNIXTIME(submit_time))=DATE(NOW()) AND user_id=?";
			Map<String, Object> map = this.findOneForJdbc(sql, userId);
			Integer times = Integer.parseInt(map.get("times").toString());
			if(times>0){
				j.setStateCode("01");
				j.setMsg("一天只能提现一次");
				j.setSuccess(false);
				return j;
			}
			
			// 添加申请记录
			WithdrawalsEntity withdrawals = new WithdrawalsEntity();
			withdrawals.setMoney(money);
			withdrawals.setState("apply");
			withdrawals.setSubmitTime(DateUtils.getSeconds());
			withdrawals.setCompleteTime(0);
			withdrawals.setCancelTime(0);
			withdrawals.setWuser(merchant.getWuser());
			withdrawals.setBankcardId(bandCard.getId());
			withdrawals.setUserType(user.getUserType());
			this.save(withdrawals);
			
			flowService.merchantWithdraw(userId, money, withdrawals.getId());
		} catch (Exception e) {
			// 抛出RuntimeException以便正确的回滚数据
			throw new RuntimeException("申请提现流水失败", e);
		}
		j.setStateCode("00");
		j.setMsg("操作成功");
		j.setSuccess(true);
		return j;
	}

	@Override
	public void merchanTmassTexting(int merchantId, String title,
			String content, HttpServletRequest request) {

		String phone = "123456";

		// String randomNum = "1234";

		String sql = "select o.mobile from `order` o  where o.merchant_id=?  GROUP BY o.mobile";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = this.findForJdbc(sql, merchantId);
		if (list.size() > 0) {
			NoteEntity note = new NoteEntity();
			note.setCeaterTime(new Date());
			note.setConten(content);
			note.setTitle(title);
			note.setMerchantid(merchantId);
			note.setState("audit");
			this.saveOrUpdate(note);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (map.get("mobile") != null
						&& !"".equals(map.get("mobile").toString())) {
					phone = map.get("mobile").toString();
					NoteRecordEntity noteRecord = new NoteRecordEntity();
					noteRecord.setCreationTime(new Date());
					noteRecord.setNoteId(note.getId());
					noteRecord.setPhone(phone);
					this.saveOrUpdate(noteRecord);
				}
			}
		}

	}

	@Override
	public AjaxJson passTmassTexting(int id) {
		AjaxJson j = new AjaxJson();
		try {

			NoteEntity note = this.get(NoteEntity.class, id);
			note.setState("pass");
			this.saveOrUpdate(note);
			String message = "添加短信成功";
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
			String sql = "select * from note_record n where n.note_id="
					+ note.getId();
			List<Map<String, Object>> list = this.findForJdbc(sql);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String phone = map.get("phone").toString();
					SmsUtil.sendMsg(note.getConten(), phone);
				}
			}
			j.setSuccess(true);
			j.setStateCode("00");
			j.setMsg("发送成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("发送失败");
		}
		return j;
	}

	@Override
	public void noPassTmassTextting(int id) {
		NoteEntity note = this.get(NoteEntity.class, id);
		note.setState("nopass");
		this.updateEntitie(note);

	}
	
	@Override
	public List<MerchantSimpleVo> findByLocation(WXHomeDTO wx) {
		//step 1 查询当前城市
		CategoryEntity category = categoryDao.findByZoneAndName("city", wx.getCity());
		
		if(null == category)
			return new ArrayList<MerchantSimpleVo>(0);
		
		//step 2 查询商铺
		List<MerchantSimpleVo> vos = merchantDao.findByLocation(category.getId(), wx);
		
		//step 3 查询商铺的销售和评分和距离
		for (MerchantSimpleVo vo : vos) {
			vo.setBuyCount(menuDao.findBuyCount(vo.getMerchantId()));
			vo.setScore(commentDao.queryCommentScore(1, vo.getMerchantId()).getScore());
			vo.setDistance((int)MapUtil.GetShortDistance(wx.getLng(), wx.getLat(), vo.getLng(), vo.getLat()));
		}
		
		//step 4 排序
		switch (wx.getSort()) {
		//计算店铺与当前用户距离
			case 0:
			case 1:
				Collections.sort(vos, new Comparator<MerchantSimpleVo>() {
					@Override
					public int compare(MerchantSimpleVo o1, MerchantSimpleVo o2) {
						return o1.getDistance() - o2.getDistance();
					}
				});
				break;
			//销量最高
			case 2:
				Collections.sort(vos, new Comparator<MerchantSimpleVo>() {
					@Override
					public int compare(MerchantSimpleVo o1, MerchantSimpleVo o2) {
						return o2.getBuyCount() - o1.getBuyCount();
					}
				});
				break;
			//评价最高
			case 3:
				Collections.sort(vos, new Comparator<MerchantSimpleVo>() {
					@Override
					public int compare(MerchantSimpleVo o1, MerchantSimpleVo o2) {
						return Double.compare(o2.getScore(), o1.getScore());
					}
				});
				break;
			//起送价最低
			case 4:
				Collections.sort(vos, new Comparator<MerchantSimpleVo>() {
					@Override
					public int compare(MerchantSimpleVo o1, MerchantSimpleVo o2) {
						return Double.compare(o1.getDeliveryPrice(), o2.getDeliveryPrice());
					}
				});
				break;
			default:
				break;
		}
		
		//营业排序，已打烊的往后排
		Collections.sort(vos, new Comparator<MerchantSimpleVo>() {

			@Override
			public int compare(MerchantSimpleVo o1, MerchantSimpleVo o2) {
				int b1 = o1.isOpening()?0:1;
				int b2 = o2.isOpening()?0:1;
				return b1-b2;
			}
		});
		
		return vos;
	}

	@Override
	public OrderSimpleVo lastedOrder(Integer userId) {
		OrderSimpleVo vo = merchantDao.lasetOrder(userId);
		if(null != vo)
			vo.setAddress(addressDao.queryAddressDetailById(vo.getAddressId()));
		return vo;
	}

	@Override
	public List<MerchantSimpleVo> queryUserFavMerchantByUserId(Integer userId, double lng, double lat, int page, int rows) {
		List<MerchantSimpleVo> vos = merchantDao.queryUserFavMerchantByUserId(userId, page, rows);
		// step 3 查询商铺的销售和评分和距离
		for (MerchantSimpleVo vo : vos) {
			vo.setBuyCount(menuDao.findBuyCount(vo.getMerchantId()));
			vo.setScore(commentDao.queryCommentScore(1, vo.getMerchantId()).getScore());
			vo.setDistance((int) MapUtil.GetShortDistance(lng, lat, vo.getLng(), vo.getLat()));
		}
		return vos;
	}
	
	@Override
	public MerchantEntity queryByMenuId(Integer menuId) {
		return merchantDao.queryByMenuId(menuId);
	}
	
	@Override
	public PageList<MerchantStatisticsMenuVo> statisticsMenu(String title,String startTime, String endTime, Integer page, Integer rows){
		return merchantDao.statisticsMenu(title,startTime,endTime,page,rows);
	}

	@Override
	public void pushOrder(Integer orderId) {
		String sql = "select m.user_id, o.from_type from `order` o, merchant m where o.merchant_id=m.id AND o.id=?";
		Map<String, Object> orderInfo = findOneForJdbc(sql, orderId);
		// 判断众包订单不推送商家语音
		if (orderInfo != null && !"crowdsourcing".equals(orderInfo.get("from_type").toString())) {
			if (orderInfo.get("user_id") == null) {
				logger.warn("订单{}对应的商家为空,无法给商家推送消息", orderId);
				return;
			}
			Integer merchantUserId = Integer.parseInt(orderInfo.get("user_id").toString());

			Map<String, String> pushMap = new HashMap<String, String>();
			pushMap.put("appType", AppTypeConstants.APP_TYPE_MERCHANT);
			pushMap.put("orderId", orderId.toString());
			String title = "您有一条新的订单";
			pushMap.put("title", title);
			pushMap.put("content", title);
			pushMap.put("voiceFile", SoundFile.SOUND_NEW_ORDER);
			jpushService.push(merchantUserId, pushMap);
		}
	}
	
	@Override
	public Map<String, Object> getMyMerchant(Long merchantId){	
		String sql = "SELECT a.id,a.display, a.notice,a.notice_time noticeTime,a.delivery_begin deliveryBegin,a.mobile,a.address,b.delivery_scope  deliveryScope,a.print_code printCode, a.title merchantName"
				+" from merchant a,0085_merchant_info b"
				+" where a.id = b.merchant_id and a.id=?";
		
		Map<String, Object> map = this.findOneForJdbc(sql, merchantId);
		Integer mId = Integer.parseInt(merchantId.toString());
		MerchantInfoEntity merchantInfo = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", mId);
		Integer merchantSource = merchantInfo.getMerchantSource();
		
		//判断商家是否显示会员中心：商家来源为代购和我要洗衣则不显示  true : 显示      false：不显示
		boolean isShowMemberCenter = true;
		if(merchantSource==3 || merchantSource==4){
			isShowMemberCenter = false;
		}
		
		List<Map<String, Object>> merchantOrgList = this.findForJdbc("select * from 0085_merchant_org where merchant_id=?", merchantId);
		if (CollectionUtils.isNotEmpty(merchantOrgList)) {
			Object orgId = merchantOrgList.get(0).get("org_id");
			map.put("orgId", orgId!=null?orgId.toString():"0");
		}
		
		map.put("isShowMemberCenter", isShowMemberCenter);
		return map;
	}
	
	
	@Override
	public List<Map<String, Object>>  getMerchantOpenTime(Long merchantId){	
		String sql = "SELECT id,merchant_id merchantId,start_time startTime , end_time endTime from 0085_merchant_open_time  WHERE merchant_id=? ";
		List<Map<String, Object>> list = this.findForJdbc(sql, merchantId);
		return list;
	}
	
	@Override
	public AjaxJson updateOrSaveMerchantOpenTime(String params){	
		AjaxJson j = new AjaxJson();
		try {
			List<Map<String, Object>> list = JSONHelper.toList(params);
			Map<String, Object> map = new HashMap<String, Object>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					int start=0;
					int end=0;
					if (!StringUtil.isEmpty(map.get("startTime"))) {
						String[] split = map.get("startTime").toString().split(":");
					  start=(Integer.parseInt(split[0]))*3600+(Integer.parseInt(split[1]))*60;
					}
					if (!StringUtil.isEmpty(map.get("endTime"))) {
						String[] split = map.get("endTime").toString().split(":");
						end=(Integer.parseInt(split[0]))*3600+(Integer.parseInt(split[1]))*60;
					}
					String sql = "UPDATE merchant set start_time=? ,end_time=? where id=?";
					this.executeSql(sql, start, end, map.get("merchantId"));
					
					String updateSql = "UPDATE 0085_merchant_open_time SET start_time=? , end_time=? WHERE id=?";
					this.executeSql(updateSql, map.get("startTime"),
							map.get("endTime"), map.get("id"));
				}
			}
		} catch (Exception e) {
			j.setStateCode("01");
			 j.setSuccess(false);
			 j.setMsg("营业时间修改异常");
			 return j; 
		}

		return j;
	}
	
	
	@Override
	public void updateMerchantDeliveryScope(int merchantId,BigDecimal deliveryScope){	
		String sql = "UPDATE 0085_merchant_info set delivery_scope=?,delivery_scope_time=now() where merchant_id=?";
		this.executeSql(sql,deliveryScope,merchantId);
	}
	
	
	public String getMerchantPlatformType(int merchantId){	
		String sql = "SELECT platform_type from 0085_merchant_info where merchant_id=?";
//		Map<String, Object> map = this.findOneForJdbc(sql, merchantId);
//		return map;
		return findOneForJdbc(sql, String.class, merchantId);
	}
	
	@Override
	public AjaxJson deleteOpentime(Integer id, Integer merchantId) {
		AjaxJson aj = new AjaxJson();
		String sqlLen = "select count(1) total from 0085_merchant_open_time where merchant_id=?";
		Map<String, Object> map = this.findOneForJdbc(sqlLen, merchantId);
		Integer total = Integer.parseInt(map.get("total").toString());
		if(total>1){
			String sql = "delete  from 0085_merchant_open_time where id=? and merchant_id=?";
			this.executeSql(sql, id, merchantId);
			aj.setMsg("删除成功");
			aj.setStateCode("00");
			aj.setSuccess(true);
			return aj;
		}else{
			aj.setMsg("营业时间不得少于一个");
			aj.setStateCode("01");
			aj.setSuccess(false);
			return aj;
		}
	}
	
	/**
	 * 商家设备
	 */
	public List<Map<String, Object>> merchantDeviceList(String codeValue) throws Exception{
		List<Map<String, Object>> list = null;
		List<Map<String, Object>> codeList =null;
		
		String sql1 = "SELECT id from sys_code s  where s.`code`='merchant_source' AND code_value=? ";
		codeList = this.findForJdbc(sql1,codeValue);
		
		if (codeList!=null && codeList.size()>0) {
			StringBuilder sb=new StringBuilder();
			sb.append(" and code_id in (");
			String sql="";
			for (int i = 0; i < codeList.size(); i++) {
				sql += codeList.get(i).get("id")+",";
			}
			//截取字符串最后一位
			if (sql.substring(sql.length()-1,sql.length()).equals(",")) {
				sb.append(sql.substring(0,sql.length()-1));
			}
			sb.append(")");
			

			String sqlDevice="SELECT d.value,d.name from tpm_login_type_merchant m LEFT JOIN merchant_device d on m.merchant_device_id=d.id where 1=1 "+sb.toString();
			list = this.findForJdbc(sqlDevice);
			
		}
		
		return list;
	}

	@Override
	public List<Map<String, Object>> findByDelState(Integer isDetele) {
		String sql = "select id from merchant ";
		if(isDetele == null){
			return this.findForJdbc(sql);
		}  else {
			sql += " where is_delete=?";
			return this.findForJdbc(sql, isDetele);
		}
	}

	@Override
	public String getMerchantSource(Integer merchantId) {
		String source = AliOcs.get(Constants.MERCHANT_SOURCE_KEY + merchantId);
		if(StringUtils.isNotEmpty(source)){
			return source;
		} else {
			String sql = "select merchant_source from 0085_merchant_info where merchant_id = ?";
			Map<String,Object> map = this.findOneForJdbc(sql, merchantId);
			return map.get("merchant_source").toString();
		}
	}

	/**
	 * 分店(0)、总店(1)、普通店铺(2)、供应链(3)、合作商商家(4)
	 */
	@Override
	public int getStoreType(Integer merchantId) {
		MerchantInfoEntity merchantInfo = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchantId);
		if(merchantInfo!=null){
			if(merchantInfo.getPlatformType()==3){
				return 3;
			}else if(merchantInfo.getPlatformType()==2){
				return 4;
			}
		}

        //判断商家店铺是否是分店
        Map<String, Object> bmap = this.findOneForJdbc(
        		"SELECT COUNT(branchstore_id) count FROM 0085_merchant_multiaccount WHERE branchstore_id=?", merchantId);
		int isBranch = Integer.valueOf(bmap.get("count").toString());
		if(isBranch==0){
			Map<String, Object> mmap = this.findOneForJdbc(
	        		"SELECT COUNT(mainstore_id) count FROM 0085_merchant_multiaccount WHERE mainstore_id=?", merchantId);
			isBranch = Integer.valueOf(mmap.get("count").toString());
			if(isBranch>0){
				return 1;
			}else{
				return 2;
			}
		}else{
			return 0;
		}
	}

	@Override
	public int getMerchantUserId(int merchantId){
		String sql = "SELECT user_id FROM merchant WHERE id = ?";
		int userId = this.findOneForJdbc(sql, Integer.class, merchantId);
		return userId;
	}
}