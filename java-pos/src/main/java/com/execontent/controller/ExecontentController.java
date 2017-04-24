package com.execontent.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;
import net.sf.json.JSONArray;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.easyui.ComboTreeModel;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.base.VO.TableFieldVO;
import com.base.VO.UploadFileVO;
import com.base.exception.RollbackException;
import com.execontent.entity.ExeContent;
import com.execontent.entity.SqlParam;
import com.execontent.service.ExecontentServiceI;
import com.exelist.entity.ExeListEntity;


/**   
 * @Title: Controller
 * @Description: sql测控
 * @author zhenjunzhuo
 * @date 2014-07-03 17:47:03
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/web/execontentController")
public class ExecontentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ExecontentController.class);

	@Autowired
	private ExecontentServiceI execontentService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * sql测控列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "execontent")
	public ModelAndView execontent(HttpServletRequest request) {
		return new ModelAndView("/execontent/execontentList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	@ResponseBody
	public  List<TreeGrid> datagrid(ExeContent execontent,HttpServletRequest request, HttpServletResponse response, TreeGrid treegrid) {
		//查询条件组装器
		CriteriaQuery cq = new CriteriaQuery(ExeContent.class);

		if (treegrid.getId() != null) {
			cq.eq("exeContent.code", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.isNull("exeContent");
		}
		cq.add();
		
		List<ExeContent> execontentList = systemService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setIdField("code");
		treeGridModel.setTextField("name");
		treeGridModel.setIcon("id");
		treeGridModel.setSrc("sqlStatement");
		treeGridModel.setChildList("exeContents");
		
		treeGrids = systemService.treegrid(execontentList, treeGridModel);
		return treeGrids;
	}

	/**
	 * 删除sql测控
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ExeContent execontent, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		execontent = systemService.findUniqueByProperty(ExeContent.class, "id", execontent.getId());
	
		if(execontent.getExeContents() != null && execontent.getExeContents().size() > 0){  //把父接口下的子接口下都删掉
			for(ExeContent e: execontent.getExeContents()){
				//删除执行过的sql
				List<ExeListEntity> sqllist = systemService.findByQueryString("from ExeListEntity where exeid = '"+ e.getId() +"'");
				if(sqllist != null && sqllist.size() > 0){
					systemService.deleteAllEntitie(sqllist);
				}
				
				//删除参数记录
				List<SqlParam> paramlist = systemService.findByQueryString("from SqlParam where exeid = '"+ e.getId() +"'");
				if(paramlist != null && paramlist.size() > 0){
					systemService.deleteAllEntitie(paramlist);
				}
			}
			
			//删除执行过的sql
			List<ExeListEntity> sqllist = systemService.findByQueryString("from ExeListEntity where exeid = '"+ execontent.getId() +"'");
			if(sqllist != null && sqllist.size() > 0){
				systemService.deleteAllEntitie(sqllist);
			}
			
			//删除参数记录
			List<SqlParam> paramlist = systemService.findByQueryString("from SqlParam where exeid = '"+ execontent.getId() +"'");
			if(paramlist != null && paramlist.size() > 0){
				systemService.deleteAllEntitie(paramlist);
			}
		}
		message = "删除成功";
		execontentService.delete(execontent);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加sql测控
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ExeContent execontent, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		if (execontent.getLevel() == 1) {
			execontent.setExeContent(null);
		}
		List<ExeContent> data = execontentService.findByQueryString("from ExeContent where id= '"+ execontent.getId().trim() +"'");
		if (data != null && data.size() > 0) {
			message = "更新成功";
			try {
				ExeContent t = data.get(0);
				MyBeanUtils.copyBeanNotNull2Bean(execontent, t);
				execontentService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			execontent.setId(execontent.getId().trim());
			execontentService.save(execontent);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * sql测控列表页面跳转	
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ExeContent execontent, HttpServletRequest req) {
		String isSerach = req.getParameter("type");
		if(StringUtil.isNotEmpty(isSerach) && isSerach.equals("search")){
			req.setAttribute("type", "serach");
		}
		if (StringUtil.isNotEmpty(execontent.getId())) {
			execontent = execontentService.getEntity(ExeContent.class, execontent.getId());
			req.setAttribute("execontentPage", execontent);
		}
		return new ModelAndView("/execontent/execontent");
	}
	
	/**
	 * 父级权限下拉菜单
	 */
	@RequestMapping(params = "setPExecontent")
	@ResponseBody
	public List<ComboTree> setPFunction(ExeContent execontent,HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(ExeContent.class);
		if (comboTree.getId() != null) {
			cq.eq("exeContent.code", comboTree.getId());
		}
		if (comboTree.getId() == null) {
			cq.isNull("exeContent");
		}
		cq.add();
		List<ExeContent> execontentList = systemService.getListByCriteriaQuery(cq, false);
		List<ComboTree> comboTrees = new ArrayList<ComboTree>();
		ComboTreeModel comboTreeModel = new ComboTreeModel("code", "name", "exeContents");
		comboTrees = systemService.ComboTree(execontentList, comboTreeModel, null);
		return comboTrees;
	}
	
	/**检查code是否存在**/
	@RequestMapping(params = "checkId")
	@ResponseBody
	public AjaxJson checkId(String id,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		boolean flag = systemService.checkCode(id);
		j.setSuccess(flag);
		return j;
	}
	
	/**跳转到测试界面**/
	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "test")
	public ModelAndView test(ExeContent execontent,HttpServletRequest req) {
		String id = req.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			execontent = execontentService.findUniqueByProperty(ExeContent.class, "id", id);
			req.setAttribute("execontent", execontent);
			
			String sql = execontent.getSqlStatement().trim();
			
			String sqltype = ((String[])sql.split(" "))[0].trim();
					
			//表名
			String tableName = null;
			//表字段
			List fieldlist;
			
			Pattern pattern2= Pattern.compile(":([a-zA-Z0-9_]*)");
			Pattern pattern3 = Pattern.compile("([(a-z_A-Z]*)[\\s]*=");
			
			//获取where条件=左边的字段名
			 List<String> tempFields;
			 
			//获取where条件=右边的参数名
			 List<String> tempParams;
			 
			//查询情况
			if(execontent != null){
				//参数列表
				List<TableFieldVO> paramList = new ArrayList<TableFieldVO>();
				
				if(sqltype.equals("select") || sqltype.equals("delete")){
					//获取表名
					sql = sql.substring(sql.toLowerCase().indexOf("from ") + 4).trim();
					tableName = sql.split(" ")[0];
					
					//获取表字段
					fieldlist = systemService.getTableField(tableName);
				
					//获取sql语句的参数
					int whereindex = sql.toLowerCase().indexOf("where");
					if(whereindex != -1){
						String expression = sql.substring(whereindex+5);
					    Matcher  matcher2 = pattern2.matcher(expression);
					    Matcher matcher3 = pattern3.matcher(expression);
					  
					    tempFields = new ArrayList<String>();
					    tempParams = new ArrayList<String>();
					    
					    //获取where条件=左边的字段名
					    while(matcher3.find()){
					    	String varStr3 = matcher3.group(1);
					    	if(varStr3 != null && varStr3.length() > 0){
					    		tempFields.add(varStr3);
					    	}
					    }
					    
					    //获取where条件=右边的参数名
					    while(matcher2.find()){
					    	String varStr2 = matcher2.group(1);
					    	if(varStr2 != null && varStr2.length() > 0){
					    		tempParams.add(varStr2);
					    	}
					    }
					    
					    //当左边获取的参数与右边获取的参数的个数不一致时，表示sql较为复杂，不截取参数，需手动添加
					    if(tempFields.size() == tempParams.size()){ 
						   for(int j = 0; j < tempParams.size(); j++){
							    String temp = tempFields.get(j);
							   
							    if(fieldlist != null){
							    	for(int i = 0; i < fieldlist.size(); i++){
							    		Object[] obj = (Object[])fieldlist.get(i);
							    		String objname = ((String)obj[0]).toLowerCase();
							    		if(temp.toLowerCase().indexOf(objname) != -1){
							    			TableFieldVO tf = new TableFieldVO();
							    			tf.setParamName(tempParams.get(j));
							    			tf.setFieldName(objname);
							    			tf.setComment((String)obj[1]);
							    			tf.setIsNull((String)obj[2]);
							    			tf.setDatatype((String)obj[3]);
							    			try{
							    				//mysql数据库
							    				tf.setDatalength((BigInteger)obj[4]);
							    			}catch(Exception ex){
							    				//oracle数据库
							    				BigInteger aa = ((BigDecimal)obj[4]).toBigInteger();
							    				tf.setDatalength(aa);
							    			}
							    			
							    			paramList.add(tf);
							    			break;
							    		}
							    	}
							    }else{
							    	break;
							    }
						   }
					    }
					}
				}else{
					if(sqltype.equals("insert")){
						int start = sql.toLowerCase().trim().indexOf("into");
						int end = sql.toLowerCase().trim().indexOf("values");
						tableName = sql.trim().substring(start + 4, end ).trim();
						String[] filedStr = null;//表名后面的字段列表
						if(tableName.indexOf("(") != -1){
							filedStr = tableName.substring(tableName.indexOf("(") + 1,tableName.indexOf(")")).split(",");
							tableName = tableName.substring(0,tableName.indexOf("(")).trim();
						}
						
						//获取表字段
						fieldlist = systemService.getTableField(tableName);
						
						//values后面的参数列表
						String vParams = sql.substring(end + 6).trim();
						vParams = vParams.replace("(", "").replace(")", "").trim();
						String[] paramArr = vParams.split(",");
						
						for(int i = 0; i < paramArr.length; i++){
							String p = paramArr[i].trim();
							if(filedStr != null && filedStr.length > 0){
								String f = filedStr[i].trim();
								
								if(p.indexOf(":") == -1){ //如果参数是没有加:，表示不是变量
									continue;
								}else{
									int num = 0;
									for(int j = 0; j < fieldlist.size(); j++){
										Object[] obj = (Object[])fieldlist.get(j);
							    		String objname = ((String)obj[0]).toLowerCase();
							    		
							    		TableFieldVO tf = new TableFieldVO();
										if(f.toLowerCase().equals(objname.toLowerCase())){  
							    			tf.setParamName(p.replace(":", "").trim());
							    			tf.setFieldName(objname);
							    			tf.setComment((String)obj[1]);
							    			tf.setIsNull((String)obj[2]);
							    			tf.setDatatype((String)obj[3]);
							    			try{
							    				tf.setDatalength((BigInteger)obj[4]);
							    			}catch(Exception ex){
							    				//把BigDecimal转换成str,截取小数点后面
							    				String str = ((BigDecimal)obj[4]).toString();
							    				String inte = str.split("\\.")[0];
							    				BigInteger b = new BigInteger(inte);
							    				tf.setDatalength((BigInteger)b);
							    			}
							    			paramList.add(tf);
										}else{
											++num;
										}
									}
									if(num == fieldlist.size()){
										TableFieldVO tf = new TableFieldVO();
										tf.setParamName(p.trim().substring(1));
										paramList.add(tf);
									}
								}
							}else{
								if(p.indexOf(":") != -1){
									TableFieldVO tf = new TableFieldVO();
									tf.setParamName(p.trim().substring(1));
									paramList.add(tf);
								}
							}
						}
					}
					
					if(sqltype.equals("update")){
						int start = sql.toLowerCase().trim().indexOf("update");
						int middle = sql.toLowerCase().trim().indexOf("set");
						int end = sql.toLowerCase().trim().indexOf("where");
						tableName = sql.substring(start + 7, middle).trim();
						
						//获取表字段
						fieldlist = systemService.getTableField(tableName);
						
						//获取set的语句块
						String vals = "";
						if(end == -1){
							vals = sql.substring(middle+3).trim();
						}else{
							vals = sql.substring(middle+3, end).trim();
						}
						
						String[] conts = vals.trim().split(",");
	
						for(int i = 0; i < conts.length; i++){
							int index  = conts[i].trim().indexOf("=");
							String fieldName = conts[i].substring(0,index).trim();
							String paramName =  conts[i].substring(index+1).trim().substring(1).trim();
//							String paramName =  conts[i].substring(index+1).trim();
							logger.info("fieldName:{},paramName:{}", fieldName, paramName);
							
							if(fieldlist != null && paramName != null && paramName.length() > 0){
								for(int j = 0; j < fieldlist.size(); j++){
									Object[] field = (Object[])fieldlist.get(j);
						    		String objname = ((String)field[0]).toLowerCase();
									
									if(objname.equalsIgnoreCase(fieldName)){
										TableFieldVO tf = new TableFieldVO();
						    			tf.setParamName(paramName);
						    			tf.setFieldName(objname);
						    			tf.setComment((String)field[1]);
						    			tf.setIsNull((String)field[2]);
						    			tf.setDatatype((String)field[3]);
						    			
						    			try{
						    				tf.setDatalength((BigInteger)field[4]);
						    			}catch(Exception ex){
						    				BigDecimal b = (BigDecimal)field[4];
						    				tf.setDatalength(new BigInteger(b.toString()));
						    			}
						    			
						    			paramList.add(tf);
						    			break;
									}
								}
							}else{
								break;
							}
						}
						
						if(end != -1){
							//where后面的语句
							String expression = sql.substring(end+6);
							Matcher matcher2 = pattern2.matcher(expression);
						    Matcher matcher3 = pattern3.matcher(expression);
						  
						    tempFields = new ArrayList<String>();
						    tempParams = new ArrayList<String>();
						    
						    //获取where条件=左边的字段名
						    while(matcher3.find()){
						    	String varStr3 = matcher3.group(1);
						    	if(varStr3 != null && varStr3.length() > 0){
						    		logger.info("varStr3:"+varStr3);
						    		tempFields.add(varStr3);
						    	}
						    }
						    
						    //获取where条件=右边的参数名
						    while(matcher2.find()){
						    	String varStr2 = matcher2.group(1);
						    	if(varStr2 != null && varStr2.length() > 0){
						    		logger.info("varStr2:"+varStr2);
						    		tempParams.add(varStr2);
						    	}
						    }
							
						   for(int j = 0; j < tempParams.size(); j++){
							    String temp = tempFields.get(j);
								   
							    if(fieldlist != null){
							    	for(int i = 0; i < fieldlist.size(); i++){
							    		Object[] obj = (Object[])fieldlist.get(i);
							    		String objname = ((String)obj[0]).toLowerCase();
							    		if(temp.equalsIgnoreCase(objname)){
							    			TableFieldVO tf = new TableFieldVO();
							    			tf.setParamName(tempParams.get(j));
							    			tf.setFieldName(objname);
							    			tf.setComment((String)obj[1]);
							    			tf.setIsNull((String)obj[2]);
							    			tf.setDatatype((String)obj[3]);
							    			
							    			try{
							    				tf.setDatalength((BigInteger)obj[4]);
							    			}catch(Exception ex){
							    				BigDecimal b = (BigDecimal)obj[4];
							    				tf.setDatalength(new BigInteger(b.toString()));
							    			}
							    			
							    			//判断是否已经存在相同名称的参数
							    			boolean isExsist = false;
							    			for(TableFieldVO vo: paramList){
								    			if(vo.getParamName().equals(tempParams.get(j))){
								    				isExsist = true;
								    				break;
								    			}
								    		}
							    			if(isExsist == false){
							    				paramList.add(tf);
							    			}
							    			break;
							    		}
							    	}
							    }else{
							    	break;
							    }
						   }
						}
					}
				}
				
				for(TableFieldVO tf : paramList){
					logger.info("fieldName : {}", tf.getFieldName());
					logger.info("paramName : {}", tf.getParamName());
				}
				
				//请求参数
				List<SqlParam> reqParmList = new ArrayList<SqlParam>();
				reqParmList = systemService.findByQueryString("from SqlParam where paramType = 0 and exeid = '"+ execontent.getId() +"'");
				
				req.setAttribute("paramList", paramList);
				req.setAttribute("tableName", tableName);
				
				//第一次时，把系统检测的参数保存到sqlparam表中
				if(reqParmList == null || reqParmList.size() < 1){
					//保存参数到sqlparam表中
					if(paramList != null && paramList.size() > 0){
						for(TableFieldVO vo :paramList){
							SqlParam sp = new SqlParam();
							if(vo != null && vo.getIsNull() != null){
								if(vo.getIsNull().equals("YES")){
									sp.setCannull(0);
								}else{
									sp.setCannull(1);
								}
							}
							if(vo.getClass() != null){
								sp.setDescrition(vo.getComment());
							}
							if(vo.getDatatype() != null){
								sp.setDataType(vo.getDatatype());
							}
							sp.setExeid(execontent.getId());
							if(vo.getParamName() != null){
								sp.setName(vo.getParamName());
							}
							systemService.save(sp);
						}
						reqParmList = systemService.findByQueryString("from SqlParam where paramType = 0 and exeid = '"+ execontent.getId() +"' order by id");
					}
				}
			
				//请求参数
				req.setAttribute("reqParmList", reqParmList);
				//检测的参数的个数
				req.setAttribute("paramListsize", paramList.size());
				
				if(execontent.getType().equals("query")){
					List<SqlParam> responseParamList = new ArrayList<SqlParam>();
					responseParamList = systemService.findByQueryString("from SqlParam where paramType = 1 and exeid = '"+ execontent.getId() +"'");
					req.setAttribute("responseParamList", responseParamList);
				}
				
				//相差的个数
				req.setAttribute("gap", reqParmList.size()- paramList.size());
				
				//请求参数的名称字符串
				String sqlparamstr = "";
				for(SqlParam vo:reqParmList){
					sqlparamstr = sqlparamstr + vo.getName() + ",";
				}
				logger.info("sqlparamstr:{}", sqlparamstr);
				req.setAttribute("sqlparamstr", sqlparamstr);
			}
		}
		return new ModelAndView("/execontent/test");
	}
	
	/**调试**/
	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(params = "executeTest")
	@ResponseBody
	public AjaxJson executeTest(String params,String ids,HttpServletRequest request,String sessionkey){
		AjaxJson j = new AjaxJson();
		List<UploadFileVO> files = null;
		String typeId = null;
		try {
			String filesString = request.getParameter("files");
			logger.info("executeTest files : {}", files);
			if(StringUtil.isNotEmpty(filesString)){
				JSONArray jsons = JSONArray.fromObject(filesString);
				files =jsons.toList(jsons,UploadFileVO.class);
				typeId = request.getParameter("typeId");
			}
			params = "{" + params + "}";
			j= systemService.execute(ids, request, params, files, typeId);
		}catch (RollbackException e) {
			e.printStackTrace();
			return e.getAjaxJson();
		}
		return j;
	}
	
	/**保存参数信息**/
	@RequestMapping(params = "saveNewParam")
	@ResponseBody
	public AjaxJson saveNewParam(String name,String comment,String dataType,int cannull,String exeid, int paramType, HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		try {
			SqlParam p = new SqlParam();
			p.setName(name);
			p.setDataType(dataType);
			p.setDescrition(comment);
			p.setCannull(cannull);
			p.setExeid(exeid);
			p.setParamType(paramType);
			
			systemService.save(p);
		}catch (Exception e) {
			j.setMsg("保存失败,请重试");
			e.printStackTrace();
		}
		j.setMsg("保存成功");
		return j;
	}
	
	/**删除参数**/
	@RequestMapping(params = "deleteParam")
	@ResponseBody
	public AjaxJson deleteParam(String id,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		try {
			SqlParam p = systemService.getEntity(SqlParam.class, id);
			if(p != null){
				systemService.delete(p);
			}
		}catch (Exception e) {
			j.setMsg("删除失败,请重试");
			e.printStackTrace();
		}
		j.setMsg("删除成功");
		return j;
	}
	
	/**保存编辑的请求参数**/
	@RequestMapping(params = "saveEditParam")
	@ResponseBody
	public AjaxJson saveEditParam(String name,String descrition,String dataType,int cannull,String id,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		try {
			SqlParam p = systemService.getEntity(SqlParam.class, id);
			if(p != null){
				p.setCannull(cannull);
				p.setDescrition(descrition);
				p.setName(name);
				p.setDataType(dataType);
				systemService.updateEntitie(p);
			}
		}catch (Exception e) {
			j.setMsg("编辑失败,请重试");
			e.printStackTrace();
		}
		j.setMsg("编辑成功");
		return j;
	}
	
	
	/**sql测试提交**/
	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "sqlTest")
	@ResponseBody
	public AjaxJson sqlTest(String sql,HttpServletRequest request){
		logger.info("sqlTest sql : {}", sql);
		AjaxJson j = new AjaxJson();
		try {
			String type = sql.trim().substring(0,sql.indexOf(" ")).trim();
			
			if(type != null && type.length() > 0){
				if(type.equals("insert")){
					j.setType("insert");
					int i = systemService.executeSql(sql, new Object[]{});
					j.setAffectedRows(i);
				} else if(type.equals("update")){
					j.setType("update");
					int i = systemService.executeSql(sql, new Object[]{});
					j.setAffectedRows(i);
				} else if(type.equals("delete")){
					j.setType("delete");
					int i = systemService.executeSql(sql, new Object[]{});
					j.setAffectedRows(i);
				} else if(type.equals("select")){
					List data = systemService.findListbySql(sql);
					j.setType("select");
					j.setObj(data);
				}
			}
			j.setMsg("执行成功");
		}catch (Exception e) {
			j.setMsg("sql有错误,请重试");
			j.setSuccess(false);
			e.printStackTrace();
		}
		j.setSql(sql);
		return j;
	}
	
	/**url测试提交**/
	@RequestMapping(params = "urlTest")
	@ResponseBody
	public AjaxJson urlTest(String url,HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		url = url.replace(" ", "");
		URLConnection connection = null;
	    try {
	        connection = new URL(url).openConnection();
	        connection.connect();
	
	        InputStream fin = connection.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(fin,"utf-8"));
	        StringBuffer buffer = new StringBuffer();
	        String temp = null;
	        while ((temp = br.readLine()) != null) {
	            buffer.append(temp);
	        }
	        j.setObj(buffer.toString());
	        j.setMsg("url连接成功");
	    } catch (IOException io) {
	    	j.setMsg("连接失败，请检查url是否正确或网络是否正常");
	    	j.setSuccess(false);
	    	io.printStackTrace();
	    }
	    
		return j;
	}
}
