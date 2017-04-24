package com.wm.service.courier;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.takeout.vo.CourierOrgVo;
import com.wm.controller.takeout.vo.CourierPositionVo;
import com.wm.entity.courier.CourierLocationEntity;
import com.wm.entity.courierinfo.CourierInfoEntity;
import com.wm.entity.courierposition.CourierPositionEntity;
import com.wm.entity.org.OrgEntity;

/**
 * 快递员服务接口
 *
 */
public interface CourierServiceI extends CommonService {

	/**
	 * 根据快递员ID查询此快递员的派送范围
	 * 
	 * @param courierId
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryDeliveryScopeList(int courierId,
			int page, int rows);

	/**
	 * 根据快递员ID查询此快递员的绑定商户
	 * 
	 * @param courierId
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryBindMerchantList(int courierId,
			int page, int rows);

	/**
	 * 保存用户最新的位置
	 * @param userId 用户ID
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @return
	 */
	public void saveUserLocation(Integer userId, Double longitude, Double latitude);
	
	/**
	 * 保存用户最新的位置
	 * @param userId 用户ID
	 * @return
	 */
	public void cleanUserLocation(Integer userId);
	
	/**
	 * 获取用户最新的位置
	 * @param userId
	 * @return
	 */
	public CourierLocationEntity getUserLocation(Integer userId);

	/**
	 * 根据快递员ID查询组织结构信息
	 * 
	 * @param userId
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<CourierOrgVo> queryOrgByUserId(Integer courierId);
	
	/**
	 * 查找快递员所属组织架构
	 * @param courierId
	 * @return
	 */
	public List<Map<String, Object>> getCourierOrg(Integer courierId);

	/**
	 * 根据快递员ID查询组织结构信息
	 * 
	 * @param userId
	 * @return
	 */
	public CourierOrgVo getOrgByUserId(int userId);

	/**
	 * 根据组织片区名称查询所有属于该片区快递员信息
	 * 
	 * @param orgName
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<CourierOrgVo> queryOrgByOrgName(String orgName, int page,
			int rows);

	/**
	 * 根据组织片区Id查询所有属于该片区快递员信息
	 * 
	 * @param orgId
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<CourierOrgVo> queryOrgByOrgId(int orgId);

	/**
	 * 根据快递员ID查询职称信息
	 * 
	 * @param userId
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<CourierPositionVo> queryPositionByUserId(int userId, int page,
			int rows);

	/**
	 * 根据快递组ID查询快递员列表
	 * 
	 * @param userId
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<CourierOrgVo> queryCouriersByGroupId(int groupId, int page,
			int rows);

	/**
	 * 查询快递员的催单列表，通过区域id查询列表
	 * 
	 * @param orgId
	 *            区域id
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryCourierReminderByOrgId(Integer orgId,
			Integer userId, Integer page, Integer rows);

	/**
	 * 保存调岗调薪信息
	 * 
	 * @param courierId
	 * @param oldSalary
	 * @param newSalary
	 * @param courierInfo
	 * @param oldPositionId
	 * @param newPositionId
	 * @param courierPosition
	 */
	public void saveAdjustPosition(int courierId, int oldSalary, int newSalary,
			CourierInfoEntity courierInfo, int oldPositionId,
			int newPositionId, CourierPositionEntity courierPosition);

	/**
	 * 通过薪酬和职位ID查用户所在的职位的等级，如1级，2级。。。
	 * 
	 * @param salary
	 *            薪酬
	 * @param positionId
	 *            职位id
	 * @return
	 */
	public Integer getCourierLevelBySalaryPosition(Integer salary,
			Integer positionId);

	/**
	 * 通过快递员ID查他所在的小组的成员，如果是片区经理以上的，则查组织结构里所有的下属成员
	 * 
	 * @param courierId
	 *            快递员id
	 * @param orderId
	 *            订单ID，表示这个订单下有哪些可以指派
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryCourierSubCourierByCourierId(
			Integer courierId, Integer orderId, Integer page, Integer rows);

	/**
	 * 指派订单给快递员
	 * 
	 * @param designateId
	 *            指派者 id
	 * @param designeeId
	 *            被指派的快递员id
	 * @param orderId
	 *            订单id
	 * @return true，如果指派成功， false，指派失败
	 */
	public Map<String, Object> designateOrder(Integer designateId,
			Integer designeeId, Integer orderId);

	/**
	 * 查询未抢单列表，通过orgId和快递员id
	 * 
	 * @param orgId
	 *            区域id
	 * @param courierId
	 *            快递员id
	 * @param page
	 *            开始页, 第一页为1
	 * @param rows
	 *            每页行数
	 * @return
	 */
	List<Map<String, Object>> queryNotScrambleOrderByOrgId(Integer orgId,
			Integer courierId, Integer page, Integer rows);

	/**
	 * 查询区域，通过快递员id
	 * 
	 * @param orgId
	 *            区域id
	 * @param courierId
	 *            快递员id
	 * @param page
	 *            开始页, 第一页为1
	 * @param rows
	 *            每页行数
	 * @return
	 */
	List<Map<String, Object>> queryFirstChildrenOrgs(Integer orgId,
			Integer courierId, Integer page, Integer rows);

	/**
	 * 查快递员管理员的功能权限
	 * 
	 * @param courierId
	 *            快递员id
	 * @return
	 */
	List<Map<String, Object>> queryCourierManagerFunction(Integer courierId);

	/**
	 * 统计快递员管理版中的订单监控数，包括未抢单的数，催单数，30分钟，20分钟，10分钟的数
	 * 
	 * @param orgId
	 *            区域id
	 * @param courierId
	 *            快递员id
	 * @return
	 */
	Map<String, Object> countOrderMonitor(Integer orgId, Integer courierId);

	/**
	 * 
	 * @param orgId 区域id
	 * @param courierId 快递员id
	 * @param keyword
	 *            关键字查询，可以搜索的字段为：排号， 电话，订单号，姓名
	 * @param state
	 *            状态为空或空串或search，则查所有，否则填 scramble 抢单 , reminder 催单, 30minute
	 *            距离送餐时间还有30分钟的订单, 20minute 距离送餐时间还有20分钟的订单, 10minute
	 *            距离送餐时间还有10分钟的订单
	 * @param page 页数
	 * @param rows 行数
	 * @return
	 */
	public List<Map<String, Object>> searchCourierOrder(Integer orgId,
			Integer courierId, String keyword, String state, Integer page,
			Integer rows);

	/**
	 * 快递员注册状态查询
	 * @param mobile 手机号
	 * @param userType 'courier''快递员,''merchant''商家,''user''普通用户
	 * @return
	 */
	Map<String, Object> registerState(String mobile, String userType);

	/**
	 * 通过快递员id和org的级别查级别
	 * @param courierId
	 * @param level
	 * @return
	 */
	public List<Map<String, Object>> queryOrgByCourierIdLevel(Integer courierId, Integer level) ;
	
	/**
	 * 根据快递员所在网点的所有商户
	 * 
	 * @param courierId 快递员id
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryOrgMerchantList(int courierId,
			int page, int rows);

	
	/**
	 * 配送范围管理-查询物流组长的快递员成员
	 * 
	 * @param courierId 快递员id
	 * @param page
	 *            开始页, 第一页为1，不能为0
	 * @param rows
	 *            每页行数
	 * @return
	 */
	public List<Map<String, Object>> queryLogisticsLeaderMemberList(int courierId,
			int page, int rows);

	
	/**
	 * 批量保存快递员与商户的关系
	 * @param courierId
	 *            快递员id
	 * @param merchantIds 商家id列表， 使用逗号分隔开，如(1,2,3,4)
	 */
	public void batchSaveCourierMerchant(Integer courierId, String merchantIds);
	
	/**
	 * 根据快递员ID和组织架构ID查找该快递员管辖的所有组织架构节点
	 * @param courierId
	 * @param orgId
	 * @return
	 */
	public List<OrgEntity> findCourierOrgs(Integer courierId, Integer orgId);
	
	/**
	 * 查找x分钟仍没接单的订单数
	 * @param courierId
	 * @param orgId
	 * @param minus 范围0~59
	 * @return
	 */
	public Integer getUnscrabledOrderNumber(Integer courierId, Integer orgId, Integer minus);
	
	/**
	 * 统计快递员的催单数
	 * @param orgId 区域id
	 * @param userId 快递员id
	 * @return
	 */
	public Long countCourierReminder(Integer orgId, Integer userId);

	/**
	 * 根据岗位查找所有快递员
	 * @return
	 */
	public List<Integer> findCourierByPosition(Integer positionId);
	
	/**
	 * 查找快递员的特定岗位的上级领导
	 * @param courierId
	 * @param positionId
	 * @return
	 */
	public Integer getCourierLeader(Integer courierId, Integer positionId);

	/**
	 * 保存快递员抢单日志
	 * @param courierId
	 * @param j
	 */
	public void saveScrambleLog(Integer courierId, AjaxJson j);

	/**
	 * 确认回访
	 * @param ordId
	 * @param userId
	 * @param followTime
	 * @return
	 */
	public Integer confirmFollow(Integer orderId, Integer userId, Date followTime);
	
	/**
	 * 查询快递员回访记录
	 * @param ordId
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getConfirmFollow(Integer orderId, Integer userId);
	
	
	/**
	 * 保存用户ID到redi缓存
	 * @param mLongitude
	 * @param mLatitude
	 * @param userId
	 * @param uLongitude
	 * @param uLatitude
	 */
	public Map<String, Object> saveUserLocationInRedis(Double mLongitude, Double mLatitude, Integer userId, 
			Double uLongitude, Double uLatitude) ;
	
	/**
	 * 根据仓库HandlerId获取旗下快递员数量<br>
	 * 此方法会对warehouseHandlersStr进行null, 空字符串检测
	 * @author hyj
	 * @param warehouseHandlersStr	若干个仓库HandlerId, 用","分隔
	 * @return
	 */
	public int getSupplyChainWarehouseCourierCount(String warehouseHandlersStr);
}
