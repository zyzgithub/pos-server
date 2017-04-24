package jeecg.system.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.pojo.base.TSFunction;
import jeecg.system.pojo.base.TSType;
import jeecg.system.pojo.base.TSTypegroup;
import jeecg.system.pojo.base.TSUser;
import net.sf.json.JSONObject;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.CommonService;

import com.base.VO.UploadFileVO;
import com.base.exception.RollbackException;
import com.session.SessionVO;
import com.wm.entity.user.WUserEntity;



public interface SystemService extends CommonService{
	/**
	 * 登陆用户检查
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public TSUser checkUserExits(TSUser user) throws Exception;
	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param loglevel 级别
	 * @param operatetype 类型
	 * @param TUser 操作人
	 */
	public void addLog(String LogContent, Short loglevel,Short operatetype);
	/**
	 * 根据类型编码和类型名称获取Type,如果为空则创建一个
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public TSType getType(String typecode,String typename,TSTypegroup tsTypegroup);
	/**
	 * 根据类型分组编码和名称获取TypeGroup,如果为空则创建一个
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public TSTypegroup getTypeGroup(String typegroupcode,String typgroupename);
	/**
	 * 根据用户ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public  Set<String> getOperationCodesByUserIdAndFunctionId(String userId,String functionId);
	/**
	 * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public  Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId,String functionId);
	/**
	 * 根据编码获取字典组
	 * 
	 * @param typegroupCode
	 * @return
	 */
	public TSTypegroup getTypeGroupByCode(String typegroupCode);
	/**
	 * 对数据字典进行缓存
	 */
	public void initAllTypeGroups();
	
	/**
	 * 刷新字典缓存
	 * @param type
	 */
	public void refleshTypesCach(TSType type);
	/**
	 * 刷新字典分组缓存
	 */
	public void refleshTypeGroupCach();
	/**
	 * 刷新菜单
	 * 
	 * @param id
	 */
	public void flushRoleFunciton(String id, TSFunction newFunciton);
	
	//根据用户名称来获取用户的id
	public TSUser getUserByName(String name);
	
	public AjaxJson execute(String ids, HttpServletRequest request, String params,List<UploadFileVO> files,String typeId, SessionVO sessionVO) throws RollbackException;
	
	//检查code是否重复
	public boolean checkCode(String code);
	
	//测试页面的参数测试 
	public AjaxJson execute(String ids, HttpServletRequest request, String params,List<UploadFileVO> files,String typeId) throws RollbackException;
	
	//获取请求的参数
	public JSONObject getParams(String params,HttpServletRequest request);
	
	// 保存操作日志
	public boolean saveOperateLog(String exeid,String params,AjaxJson j,String userid,String operateType);
	
	//获取表字段
	@SuppressWarnings("rawtypes")
	public List getTableField(String tableName);
	
	public String getSystemConfigValue(String code);
	
	//判断用户是否已绑定手机
	public boolean checkMobile(WUserEntity user);
	
	/**
	 * 清除历史数据
	 */
	public void clearLogPerWeek();
	
	/**
	 * 备份历史订单
	 */
	public void backupOrderData();
}
