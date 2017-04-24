package com.wm.service.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.base.VO.UploadFileVO;
import com.wm.entity.user.NewAndOldUserVo;
import com.wm.entity.user.WUserEntity;

public interface WUserServiceI extends CommonService {

	/**
	 * 用户头像上传
	 * 
	 * @param userId
	 *            用户id
	 * @param file
	 *            用户图片文件
	 * @param rootPath
	 *            服务器根目录
	 */
	public String uploadUserPhoto(int userId, UploadFileVO file, String rootPath);

	/**
	 * 用户登录
	 * 
	 * @param username
	 *            用户名或手机号
	 * @param password
	 *            密码
	 * @param type
	 *            类型
	 * @return
	 */
	public WUserEntity userLogin(String username, String password, String type);
	
	/**
	 * 获取快递员状态
	 * @param userId
	 * @return
	 */
	public Integer getUserState(Integer userId);
	
	/**
	 * 验证用户是否存在中文名与密码，存在多条记录重复的情况
	 * @param username
	 * @param password
	 * @return
	 */
	public Long getCountByNameAndPwd(String username, String password);
	public Long getCourierPosition(Integer userId);
	
	/**
	 * 授权登录
	 * 
	 * @param sns
	 * @return
	 */
	public WUserEntity authLogin(String sns);

	/**
	 * 修改用户名
	 * 
	 * @param userId
	 *            用户id
	 * @param username
	 *            用户名
	 * @return
	 */
	public boolean updateUsername(int userId, String username);

	/**
	 * 授权注册
	 * 
	 * @param request
	 * @param username
	 * @param sns
	 * @param gender
	 * @param password
	 * @return
	 */

	public AjaxJson AuthRegister(HttpServletRequest request, String username,
			String sns, String gender, String password);

	/**
	 * 用户注册
	 * 
	 * @param request
	 * @param username
	 * @param sns
	 * @param gender
	 * @param password
	 * @return
	 */
	public AjaxJson userRegister(HttpServletRequest request, String username,
			String mobile, String password);

	/**
	 * 商家手机登录
	 * 
	 * @param phone
	 * @return
	 */
	public WUserEntity merchantLogin(String username);

	/**
	 * 根据用户帐号或手机号和用户类型获取用户信息
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param userNameOrMobile
	 *            用户手机号
	 * @param userType
	 *            用户类型：'courier' 快递员,'merchant' 商家,'user' 普通用户,'manage' 后台管理员
	 * @return
	 */
	public WUserEntity getUserByUserNameOrMobile(String userNameOrMobile,
			String userType);

	/**
	 * 根据手机号和用户类型获取用户信息
	 * 
	 * @author lfq
	 * @email 545987886@qq.com
	 * @param mobile
	 *            用户手机号
	 * @param userType
	 *            用户类型：'courier' 快递员,'merchant' 商家,'user' 普通用户,'manage' 后台管理员
	 * @return
	 */
	public WUserEntity getUserByMobile(String mobile, String userType);

	/**
	 * 根据用户类型查找
	 * 
	 * @param userType
	 * @return
	 */
	public List<WUserEntity> findUserByType(String userType);

	/**
	 * 根据openId和用户类型更新用户unionId
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @param openId
	 * @param unionId
	 * @param userType
	 *            用户类型:courier快递员,merchant商家,user普通用户,manage管理员
	 * @return
	 */
	public Integer updateUnionIdByOpenId(String openId, String unionId,
			String userType);

	/**
	 * 根据unionId和用户类型更新用户openId
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @param unionId
	 * @param openId
	 * @param userType
	 *            用户类型:courier快递员,merchant商家,user普通用户,manage管理员
	 * @return
	 */
	public Integer updateOpenIdByUnionId(String unionId, String openId,
			String userType);

	/**
	 * 判断该openId是否已注册并且unionid是否为空
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @param unionId
	 * @return openI已注册并且unionId为空时返回true
	 */
	public Boolean checkedUnion(String openId);

	/**
	 * 获取unionId为空并且openid不为空的用户并且 openid like 'ow1%'的openid
	 * 
	 * @author jusnli
	 * @email 545987886@qq.com
	 * @return
	 */
	List<String> findNullUniondWhenNotNullOpenId();

	/**
	 * 根据用户标识查找用户对应的组织架构（职称）
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getUserOrg(Integer userId);

	/**
	 * 根据用户标识查找用户对应的职位
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getUserPosition(Integer userId);
	
	/**
	 * 获取用户账户余额
	 * 
	 * @param userId
	 * @return
	 */
	public Double getBalance(Integer userId);

	/**
	 * 快递员成绩排名（按时间、区域、接单数排名）
	 * 
	 * @param courierId
	 *            快递员ID，必填
	 * @param startDate
	 *            统计起始时间，可以为空
	 * @param endDate
	 *            统计截止时间，可以为空
	 * @param isRankByArea
	 *            是否按区域排名，true：按区域排名，返回快递员所属分区的所有快递员订单数排名；false：返回当前快递员总排名
	 * @return
	 */
	public List<Map<String, Object>> getCourierRank(Integer courierId,
			String startDate, String endDate, Boolean isRankByArea,
			String start, String num);

	/**
	 * 我的提成收入
	 * 
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String, Object>> getCourierDeduct(Integer courierId,
			String startDate, String endDate);

	/**
	 * 根据ID获取快递员信息
	 * @param courierId
	 * @return
	 */
	public Map<String, Object> getCourierInfo(Integer courierId);
	
	/**
	 * 修改用户基本信息
	 * @param userId
	 * @param nickname
	 * @param photoUrl
	 * @param mobile
	 * @return
	 */
	public boolean updateUserInfo(int userId,String nickname, String photoUrl,String mobile);
	
	/**
	 * 修改用户头像
	 * 
	 * @param userId
	 *            用户id
	 * @param photoUrl
	 *            头像路径
	 * @return
	 */
	public boolean updatePhotoUrl(int userId, String photoUrl);

	/**
	 * 用户修改密码
	 * 
	 * @param userId
	 * @param password
	 * @param newPassword
	 * @return
	 */
	public boolean updatePassword(int userId, String password,
			String newPassword);
	
	/**
	 * 用户重置密码
	 * 
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public boolean resetPassword(int userId, String newPassword);
	
	/**
	 * 获取客户类型
	 * @param userId
	 * @return
	 */
	public String getCustType(Integer userId);
	
	/**
	 * 校验快递员的身份证是否有注册
	 * @param idcard 身份证号码
	 * @return true,如果身份证还没有被注册
	 */
	boolean checkIdCard(String idcard);

	public WUserEntity findByOpenId(String openId);
	
	/**
	 * 新老用户统计
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public NewAndOldUserVo statisticsNewAndOldUser(String startTime, String endTime);
	
	/**
	 * 对账
	 * @param userId
	 * @param startDate 起始日期 yyyy-MM-dd
	 * @param endDate 截止日期 yyyy-MM-dd
	 * @return
	 */
	public AjaxJson reconciliation(Integer userId, String startDate, String endDate);
	
	/**
	 * 获取所有在职的快递员
	 * @return
	 */
	public List<WUserEntity> getAllServingCouriers();
	
	/**
	 * 根据openId获取用户
	 * @param openId
	 * @return
	 */
	public WUserEntity  getUserByOpenId(String openId);
	
	/**
	 * 根据快递员id获取总的提成收入金额
	 * @param courierId
	 * @return
	 */
	public Double getSumDeduct(Integer courierId);
	
	/**
	 * 根据快递员id获取总的提现金额
	 * @param courierId
	 * @return
	 */
	public Double getSumWithdrawals(Integer courierId);
	
	/**
	 * 根据用户id获取已读和未读信息的数量
	 * @param userId
	 * @return
	 */
	public Map<String, Object> countNotice(Integer userId);
	
	/**
	 * 根据用户id获取未读信息
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getUnreadNotice(Integer userId, int page, int rows);
	/**
	 * 根据用户id获取所有具体信息
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String, Object>> getNotice(Integer userId, int page, int rows);
	
	/**
	 * 根据用户id、信息id改变信息状态
	 * @param userId
	 * @param noticeId
	 * @return
	 */
	public Integer readNotice(Integer userId, String noticeIds, Date readTime);	
	
	/**
	 * 获取条例名称
	 * @return
	 */
	public List<Map<String, Object>> getRegulationName();
	
	/**
	 * 获取条例内容
	 * @param id	 
	 * @return
	 */
	public Map<String, Object> getRegulationContent(Integer id);
	
	/**
	 * 获取快递员排名列表
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @param level
	 * @param start
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> getCourierRankNew(Integer courierId,
			String startDate, String endDate, Integer level,
			String start, String num);
	
	/**
	 * 获取快递员的排名
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @param level
	 * @return
	 */
	public Integer getRank(Integer courierId, String startDate, String endDate, Integer level);
	
	/**
	 * 获取快递员的订单数量
	 * @param courierId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer getMyTotalOrders(Integer courierId, String startDate, String endDate);
	/**
	 * 根据支付宝账号查询用户
	 * @param aliAcNo
	 * @param string
	 * @return
	 */
	public WUserEntity getUserByAliAcNo(String aliAcNo, String string);

	/**
	 * 判断用户是否为首单用户
	 * @param userId
	 * @return 
	 */
	public boolean isFirstOrder(Integer userId);
	
	/**
	 * 根据用户ID获取用户信息
	 * @author hyj
	 * @param userId	用户ID
	 */
	public abstract Map<String, Object> getUserInfo(Integer userId);
}
