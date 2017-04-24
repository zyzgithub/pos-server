package jeecg.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jeecg.system.pojo.base.TSFunction;
import jeecg.system.pojo.base.TSIcon;
import jeecg.system.pojo.base.TSLog;
import jeecg.system.pojo.base.TSRole;
import jeecg.system.pojo.base.TSRoleFunction;
import jeecg.system.pojo.base.TSRoleUser;
import jeecg.system.pojo.base.TSType;
import jeecg.system.pojo.base.TSTypegroup;
import jeecg.system.pojo.base.TSUser;
import jeecg.system.service.SystemService;
import net.sf.json.JSONObject;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.UUIDGenerator;
import org.jeecgframework.core.util.oConvertUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.VO.UploadFileVO;
import com.base.exception.RollbackException;
import com.base.util.FileUtil;
import com.execontent.entity.ExeContent;
import com.log.entity.OperateLog;
import com.session.SessionVO;
import com.testuser.entity.TestUserResult;
import com.wm.entity.user.WUserEntity;



@Service("systemService")
@Transactional
public class SystemServiceImpl extends CommonServiceImpl implements SystemService {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);
	
	public TSUser checkUserExits(TSUser user) throws Exception {
		return this.commonDao.getUserByUserIdAndUserNameExits(user);
	}

	/**
	 * 添加日志
	 */
	public void addLog(String logcontent, Short loglevel, Short operatetype) {
		HttpServletRequest request=ContextHolderUtils.getRequest();
		String broswer=BrowserUtils.checkBrowse(request);
		TSLog log = new TSLog();
		log.setLogcontent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperatetype(operatetype);
		log.setNote(oConvertUtils.getIp());
		log.setBroswer(broswer);
		log.setOperatetime(DateUtils.gettimestamp());
		log.setTSUser(ResourceUtil.getSessionUserName());
		commonDao.save(log);
	}

	/**
	 * 根据类型编码和类型名称获取Type,如果为空则创建一个
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public TSType getType(String typecode,String typename,TSTypegroup tsTypegroup)
	{
		TSType actType = commonDao.findUniqueByProperty(TSType.class, "typecode",typecode);
		if (actType == null) {
			actType = new TSType();
			actType.setTypecode(typecode);
			actType.setTypename(typename);
			actType.setTSTypegroup(tsTypegroup);
			commonDao.save(actType);
		}
		return actType;
		
	}
	/**
	 * 根据类型分组编码和名称获取TypeGroup,如果为空则创建一个
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public TSTypegroup getTypeGroup(String typegroupcode,String typgroupename)
	{
		TSTypegroup tsTypegroup = commonDao.findUniqueByProperty(TSTypegroup.class, "typegroupcode",typegroupcode);
		if (tsTypegroup == null) {
			tsTypegroup = new TSTypegroup();
			tsTypegroup.setTypegroupcode(typegroupcode);
			tsTypegroup.setTypegroupname(typgroupename);
			commonDao.save(tsTypegroup);
		}
		return tsTypegroup;	
	}
	@Override
	public TSTypegroup getTypeGroupByCode(String typegroupCode) {
		TSTypegroup tsTypegroup = commonDao.findUniqueByProperty(TSTypegroup.class, "typegroupcode",typegroupCode);
		return tsTypegroup;
	}

	@Override
	public void initAllTypeGroups() {
		List<TSTypegroup> typeGroups = this.commonDao.loadAll(TSTypegroup.class);
		for (TSTypegroup tsTypegroup : typeGroups) {
			TSTypegroup.allTypeGroups.put(tsTypegroup.getTypegroupcode().toLowerCase(), tsTypegroup);
			List<TSType> types = this.commonDao.findByProperty(TSType.class, "TSTypegroup.id", tsTypegroup.getId());
			TSTypegroup.allTypes.put(tsTypegroup.getTypegroupcode().toLowerCase(), types);
		}
	}
	@Override
	public void refleshTypesCach(TSType type) {
		TSTypegroup tsTypegroup = type.getTSTypegroup();
		TSTypegroup typeGroupEntity = this.commonDao.get(TSTypegroup.class, tsTypegroup.getId());
		List<TSType> types = this.commonDao.findByProperty(TSType.class, "TSTypegroup.id", tsTypegroup.getId());
		TSTypegroup.allTypes.put(typeGroupEntity.getTypegroupcode().toLowerCase(), types);
	}
	@Override
	public void refleshTypeGroupCach(){
		TSTypegroup.allTypeGroups.clear();
		List<TSTypegroup> typeGroups = this.commonDao.loadAll(TSTypegroup.class);
		for (TSTypegroup tsTypegroup : typeGroups) {
			TSTypegroup.allTypeGroups.put(tsTypegroup.getTypegroupcode().toLowerCase(), tsTypegroup);
		}
	}
	
	//----------------------------------------------------------------
	//update-start--Author:anchao  Date:20130415 for：按钮权限控制
	//----------------------------------------------------------------
	
	@Override
	public Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId,String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		TSRole role = commonDao.get(TSRole.class, roleId);
		CriteriaQuery cq1=new CriteriaQuery(TSRoleFunction.class);
		cq1.eq("TSRole.id",role.getId());
		cq1.eq("TSFunction.id",functionId);
		cq1.add();
		List<TSRoleFunction> rFunctions = getListByCriteriaQuery(cq1,false);
		if(null!=rFunctions && rFunctions.size()>0){
			TSRoleFunction tsRoleFunction =  rFunctions.get(0);
			if(null!=tsRoleFunction.getOperation()){
				String[] operationArry = tsRoleFunction.getOperation().split(",");
				for (int i = 0; i < operationArry.length; i++) {
					operationCodes.add(operationArry[i]);
				}
			}
		}
		return operationCodes;
	}

	@Override
	public Set<String> getOperationCodesByUserIdAndFunctionId(String userId,String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		List<TSRoleUser> rUsers = findByProperty(TSRoleUser.class, "TSUser.id", userId);
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			CriteriaQuery cq1=new CriteriaQuery(TSRoleFunction.class);
			cq1.eq("TSRole.id",role.getId());
			cq1.eq("TSFunction.id",functionId);
			cq1.add();
			List<TSRoleFunction> rFunctions = getListByCriteriaQuery(cq1,false);
			if(null!=rFunctions && rFunctions.size()>0){
				TSRoleFunction tsRoleFunction =  rFunctions.get(0);
				if(null!=tsRoleFunction.getOperation()){
					String[] operationArry = tsRoleFunction.getOperation().split(",");
					for (int i = 0; i < operationArry.length; i++) {
						operationCodes.add(operationArry[i]);
					}
				}
			}
		}
		return operationCodes;
	}
	//----------------------------------------------------------------
	//update-start--Author:anchao  Date:20130415 for：按钮权限控制
	//----------------------------------------------------------------
	@Override
	public void flushRoleFunciton(String id, TSFunction newFunction) {
		TSFunction functionEntity = this.getEntity(TSFunction.class, id);
		if (functionEntity.getTSIcon() == null || !StringUtil.isNotEmpty(functionEntity.getTSIcon().getId())) {
			return;
		}
		TSIcon oldIcon = this.getEntity(TSIcon.class, functionEntity.getTSIcon().getId());
			if(!oldIcon.getIconClas().equals(newFunction.getTSIcon().getIconClas())) {
				//刷新缓存
				HttpSession session = ContextHolderUtils.getSession();
				TSUser user = ResourceUtil.getSessionUserName();
				List<TSRoleUser> rUsers = this.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
				for (TSRoleUser ru : rUsers) {
					TSRole role = ru.getTSRole();
					session.removeAttribute(role.getId());
				} 
			}
	}

	//根据用户名称来获取用户的id
	public TSUser getUserByName(String name){
		List<TSUser> list = commonDao.findByProperty(TSUser.class, "realName", name);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public AjaxJson execute(String ids, HttpServletRequest request,String params,List<UploadFileVO> files,String typeId, SessionVO sessionVO) throws RollbackException {
		AjaxJson j = new AjaxJson();
//		JSONObject paramJsons = new JSONObject();
	
		String sql = null;
		ExeContent exe = null;

//		SessionVO sessionVO = null;
//		if(sessionkey != null && sessionkey.length() > 0){
//			sessionVO = SessionContext.getSessionVO(sessionkey);
//		}
		
		String sessionkey = sessionVO.getSessionKey();
		
		//记录测试模式的用户
		TestUserResult result = new TestUserResult();
		result.setExeid(ids);
		result.setDated(new Date());
		result.setSessionKey(sessionkey);
//		if(sessionkey != null && sessionkey.length() > 0){
//			result.setSessionKey(sessionkey);
//		}
		
	    //操作日志
	    OperateLog operateLog = new OperateLog();
	    
		try{
			if(ids != null){
				//参数json格式
				JSONObject paramJsons = getParams(params, request);
				//请求参数
			    if(!paramJsons.isNullObject() && !paramJsons.isEmpty()){
			    	 result.setParams(paramJsons.toString().replaceAll("\\'", "\\\\'"));
			    	 operateLog.setParams(paramJsons.toString().replaceAll("\\'", "\\\\'"));
			    }
				
				for(String id : ids.split(",")){
					AjaxJson json = new AjaxJson();
					//获取对应ID的SQL查询
					exe = this.findUniqueByProperty(ExeContent.class, "id", id.trim());
					
					if(exe != null){
						operateLog = new OperateLog();
					 	if(sessionVO != null){
					    	operateLog.setUserid(sessionVO.getUserId());
					    }
					    operateLog.setOperateDate(new Date());
					    operateLog.setId(UUIDGenerator.generate());
						operateLog.setExeid(id);
						
						//解析自定义sql表达式
						sql = analyticalSQL(paramJsons, exe.getSqlStatement());   
						
						
						
//						if(sessionVO != null && sessionVO.getIsDebug() == 1){
							result.setExeid(id);
//						}
						
						if(logger.isInfoEnabled()){
							logger.info(sql);
						}
						if("execute".equals(exe.getType())){
							int i = this.executeSql(sql);
							json.setType(exe.getType());
							json.setAffectedRows(i);
							json.setMsg(exe.getMessage());
							j.setType(exe.getType());
							j.setAffectedRows(i);
							j.setMsg(exe.getMessage());
							j.setSql(sql);
						}else if("query".equals(exe.getType())){
							List list = this.findForJdbc(sql);
							json.setType(exe.getType());
							json.setObj(list);
							json.setMsg(exe.getMessage());
							j.setType(exe.getType());
							j.setObj(list);
							j.setMsg(exe.getMessage());
							j.setSql(sql);
						}
						j.setSessionkey(sessionkey);
						j.getArrayAjaxJson().put(exe.getId(),json);
						j.setMsg("执行成功");
						
						operateLog.setOperateType(exe.getType());
					}else{
						logger.error("执行失败，接口id:{}不存在!!", id);
						j.setMsg("执行失败，接口id不存在");
						j.setSuccess(false);
					}
					
					//操作日志
					operateLog.setOperateResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'"));
					logger.debug(JSONObject.fromObject(operateLog).toString());
					
					//如果是debug模式，记录操作的内容
					if(sessionVO.getIsDebug() == 1){
						result.setResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'"));
						save(result);
					}
				}
			} else {
				logger.warn("执行接口的ids为空~");
				j.setMsg("执行失败，接口ids不存在");
				j.setSql(sql);
				j.setSuccess(false);
				RollbackException ex = new RollbackException();
				ex.setAjaxJson(j);
				throw ex;
			}
			
			//判断是否有文件，有则操作上传方法
			if(files != null && files.size() > 0){
				boolean uoloadResult = true;
				String path = this.get(TSType.class,typeId).getValue();
				for(UploadFileVO file : files){
					uoloadResult = FileUtil.write(path, file.decodePicBase64(), file.getName(), false);
				}
				if(!uoloadResult){
					j.setMsg("图片上传失败！");
					j.setSuccess(false);
					
					//如果是debug模式，记录操作的内容
					if(sessionVO.getIsDebug() == 1){
						result.setResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'"));
						save(result);
					}
					
					//操作日志
					operateLog.setOperateResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'"));
					logger.debug(JSONObject.fromObject(operateLog).toString());
					
					RollbackException ex = new RollbackException();
					ex.setAjaxJson(j);
					throw ex;
				}
				j.setMsg("图片上传成功！");
				j.setSuccess(true);
			}	
		}catch (Exception e) {
			j.setMsg("无法正常执行，请检查参数是否必填或sql是否编写正确");
			j.setSuccess(false);
			j.setSql(sql);
			
			//操作日志
			operateLog = new OperateLog();
			if(sessionVO != null){
		    	operateLog.setUserid(sessionVO.getUserId());
		    }
		    operateLog.setOperateDate(new Date());
		    operateLog.setId(UUIDGenerator.generate());
			operateLog.setOperateResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'").replaceAll("\"", "\\\\\""));
			logger.debug(JSONObject.fromObject(operateLog).toString());
			
			//如果是debug模式，记录操作的内容
			if(sessionVO.getIsDebug() == 1){
				result.setResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'").replaceAll("\"", "\\\\\""));
				save(result);
			}
			
			RollbackException ex = new RollbackException();
			ex.setAjaxJson(j);
			e.printStackTrace();
			throw ex;
		}
		return j;
	} 
	
	
	//测试页面的参数测试
	@SuppressWarnings("rawtypes")
	@Override
	public AjaxJson execute(String ids, HttpServletRequest request,String params,List<UploadFileVO> files,String typeId) throws RollbackException {
		AjaxJson j = new AjaxJson();
		JSONObject paramJsons = new JSONObject();
	
		String sql = null;
		ExeContent exe = null;
		
		try{
			for(String id : ids.split(",")){
				AjaxJson json = new AjaxJson();
				//获取对应ID的SQL查询
				exe = this.findUniqueByProperty(ExeContent.class, "id", id);
				sql = exe.getSqlStatement();
				
				paramJsons = getParams(params,request);
				sql = analyticalSQL(paramJsons,sql);
				logger.info("execute sql: {}", sql);
				
				if("execute".equals(exe.getType())){
					int i = this.executeSql(sql);
					json.setType(exe.getType());
					json.setAffectedRows(i);
					json.setMsg(exe.getMessage());
					j.setType(exe.getType());
					j.setAffectedRows(i);
					j.setMsg(exe.getMessage());
					j.setSql(sql);
				}else if("query".equals(exe.getType())){
					List list = this.findForJdbc(sql);
					json.setType(exe.getType());
					json.setObj(list);
					json.setMsg(exe.getMessage());
					j.setType(exe.getType());
					j.setObj(list);
					j.setMsg(exe.getMessage());
					j.setSql(sql);
					j.setSuccess(json.isSuccess());
					j.setMsg(json.getMsg());
				}
				j.getArrayAjaxJson().put(exe.getId(),json);
			}
			
			/*
			 * 判断是否有文件，有则操作上传方法
			 */
			if(files != null && files.size() > 0){
				boolean uoloadResult = true;
				String path = this.get(TSType.class,typeId).getValue();
				for(UploadFileVO file : files){
					uoloadResult = FileUtil.write(path, file.decodePicBase64(), file.getName(), false);
//					files.get(0).getClass().
				}
				if(!uoloadResult){
					j.setMsg("图片上传失败！");
					j.setSuccess(false);
					RollbackException ex = new RollbackException();
					ex.setAjaxJson(j);
					throw ex;
				}
				j.setMsg("上传成功");
				j.setSuccess(true);
			}
		}catch (Exception e) {
			j.setMsg("无法正常执行，请检查参数是否必填或sql是否编写正确");
			j.setSuccess(false);
			j.setSql(sql);
			RollbackException ex = new RollbackException();
			ex.setAjaxJson(j);
			e.printStackTrace();
			throw ex;
		}
		return j;
	} 
	
	//获取请求的参数
	@SuppressWarnings("rawtypes")
	public JSONObject getParams(String params,HttpServletRequest request){
		JSONObject paramJsons = new JSONObject();
		if(StringUtil.isNotEmpty(params)){
			paramJsons = JSONObject.fromObject(params);
		}else{
			Map<String,String> map = new HashMap<String, String>();
			Set set = request.getParameterMap().keySet();
			Iterator it = set.iterator();
			while(it.hasNext()){
				Object key = it.next();
				String k = String.valueOf(key);
				
				//不取请求接口的id和执行的方法
				if(!k.equals("login") && !k.equals("execute") && !k.equals("id") && !k.equals("ids") && !k.equals("sessionkey") && !k.equals("register")){
					map.put(k,request.getParameter(String.valueOf(key)));
				}
			}
			paramJsons = JSONObject.fromObject(map);
		}
		return paramJsons;
	}
	
	// 保存操作日志
	public boolean saveOperateLog(String exeid,String params,AjaxJson j, String userid,String operateType){
		try{
			OperateLog ol = new OperateLog();
			ol.setExeid(exeid);
			ol.setOperateDate(new Date());
			ol.setOperateResult(JSONObject.fromObject(j).toString().replaceAll("\\'", "\\\\'"));
			ol.setUserid(userid);
			ol.setOperateType("login");
			ol.setParams(params);
			save(ol);
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	//解析出自定义SQL表达式
	@SuppressWarnings("rawtypes")
	public String analyticalSQL(JSONObject paramJsons,String sql) throws Exception{
	    Pattern pattern = Pattern.compile("[^\\$]*([\\$][^\\$]*\\})");
	    Matcher matcher = pattern.matcher(sql.toString());
	    //如果有匹配则进行解析
	    while(matcher.find()){
	    	//表达式段落语句  例如【$NotNull{and a=:a}】
	    	String expression = matcher.group(1);
	    	//表达式语句中的变量  例如【:a】
	    	String varStr;
	    	//表达式需要转换的SQL where条件，去除表达式符号的SQL语句
	    	String toSqlWhereStr;

	    	//解析变量名
		    Pattern pattern2;
		    pattern2= Pattern.compile(":([a-zA-z0-9]*)");
		    Matcher  matcher2 = pattern2.matcher(expression);

		    //去参数集合中匹配变量名，匹配成功则代表参数作为条件，将表达式替换为sql语句
		    while(matcher2.find()){
		    	varStr = matcher2.group(1);
		    	if(paramJsons.get(varStr) != null && !paramJsons.get(varStr).equals("")){
		    		Pattern pattern3 = Pattern.compile("\\{([^\\&]*)\\}");
				    Matcher  matcher3 = pattern3.matcher(expression);
					while(matcher3.find()){
			    		if(!varStr.equals("")){
			    			toSqlWhereStr = matcher3.group(1);
				    		sql = sql.replace(expression, toSqlWhereStr);
			    		}else{
			    			toSqlWhereStr = "";
				    		sql = sql.replace(expression, toSqlWhereStr);
			    		}
					}
		    	}else{
		    		sql = sql.replace(expression, "");
		    	}
		    }
	    }
	    
		Iterator it = paramJsons.keys();
		while(it.hasNext()){
			String key =  String.valueOf(it.next());
			String value = paramJsons.getString(key);
		//	value = new String(value.getBytes("iso-8859-1"),"gbk");
			
			if(StringUtil.isNotEmpty(value)){
				try{
					Double.valueOf(value);
					sql = sql.replace(":" + key, value);
//					sql = sql.replace(":" + key, "'" + value + "'");
				}catch (java.lang.NumberFormatException e) {
					if(value.equals("NULL")){
						sql = sql.replace(":"+key, "NULL");
					}else{
						sql = sql.replace(":"+key, "'"+value+	"'");
					}
				}
			}
		}
		
		/*
		while(it.hasNext()){
			String key =  String.valueOf(it.next());
			String value = paramJsons.getString(key);
			try{
				Double.valueOf(value);
				sql = sql.replace(":"+key, value);
			}catch (java.lang.NumberFormatException e) {
				int i = sql.indexOf(":"+key);
				if(i-sql.substring(0, i).lastIndexOf("like") <= 10 && sql.substring(0, i).lastIndexOf("like") > 10){
					sql = sql.replace(":"+key, "'%"+value+	"%'");
				}else{
					sql = sql.replace(":"+key, "'"+value+	"'");
				}
			}
		}*/
	    return sql;
	}

	//检查code是否重复
	public boolean checkCode(String id){
		boolean flag = false;
		try{
			String hql = "from ExeContent where id = '"+ id +"'";
			List<ExeContent> list = this.findByQueryString(hql);
			if(list != null && list.size() > 0){
				flag = true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return flag;
	}
	
	//获取表字段,获取正在使用的数据库
	@SuppressWarnings("rawtypes")
	public List getTableField(String tableName){
		List fieldlist = new ArrayList();
		
		//获取数据库名
		String dbname = "";
		//mysql数据库
		String dbnamesql = "select database()";
		//oracle数据库
		String oradbnamesql = "select SYS_CONTEXT('USERENV','INSTANCE_NAME') from dual";
		
		List dbnamelist = null;
		try{
			dbnamelist = this.findListbySql(dbnamesql);
		}catch(Exception ex){
			dbnamelist = this.findListbySql(oradbnamesql);
		}
		if(dbnamelist != null && dbnamelist.size() > 0){
			dbname = (String)dbnamelist.get(0);
		}
		
		try{
			//mysql数据库
			String fieldsql = "SELECT COLUMN_NAME, column_comment, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA. COLUMNS WHERE table_name = '"+ tableName + "' AND table_schema = '"+ dbname +"'";
			fieldlist = this.findListbySql(fieldsql);
		}catch(Exception ex){
			//oracle数据库
			String oraFieldSql = "select a.column_name,b.comments,a.nullable,a.data_type, a.data_length from all_tab_columns a left join USER_COL_COMMENTS b on a.column_name=b.column_name and a.TABLE_NAME = b.table_name where a.table_name = '"+ tableName +"'";
			fieldlist = this.findListbySql(oraFieldSql);
		}
		return fieldlist;
	}
	
	public String getSystemConfigValue(String code){
		String resultValue = null;
		String sql = "select value from system_config where code=?";
		Map<String, Object> systemConfigMap = this.findOneForJdbc(sql, code);
		if(systemConfigMap != null){
			Object value = systemConfigMap.get("value");
			if(value != null){
				resultValue = value.toString();
			}
		}
		return resultValue;
	}
	
	public void setSystemConfigValue(String code,String value){
		String sql = "update system_config set `value`=? where code=?";
		this.executeSql(sql, value, code);
	}
	
	//判断用户是否已绑定手机
	public boolean checkMobile(WUserEntity user){
		if(StringUtil.isNotEmpty(user.getMobile())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void clearLogPerWeek() {
		logger.info("定时任务-删除历史日志");
		
		//删除操作日志-7天前
		String sql = "call p_delete_c_operate_log()";
		this.executeSql(sql);
		//删除极光推送日志-1个月前
		sql = "call p_delete_0085_jpush_log()";
		this.executeSql(sql);
		//删除快递员位置记录-1个月前
		sql = "call p_delete_0085_courier_location()";
		this.executeSql(sql);
		//删除快递员抢单日志-1个月前
		sql = "call p_delete_0085_courier_scramble_log()";
		this.executeSql(sql);
		//删除快递员抢单日志-1个月前
		sql = "call p_delete_c_session()";
		this.executeSql(sql);
	}

	public void backupOrderData() {
		DateTime dt = DateTime.parse(DateTime.now().minusMonths(3).toString("yyyy-MM-01"));
		int dateTimeSeconds = DateUtils.getSeconds(dt.toDate());
		logger.info("定时任务-备份历史订单, dateTimeSeconds:{}", dateTimeSeconds);
		String sql = "call mv_order_related_data(?, ?)";
		this.executeSql(sql, dateTimeSeconds, 1000);
	}
}
