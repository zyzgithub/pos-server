package com.wm.controller.topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import jeecg.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import com.base.VO.UploadFileVO;
import com.wm.entity.topic.TopicEntity;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.service.topic.TopicServiceI;

/**
 * @Title: Controller
 * @Description: topic
 * @author wuyong
 * @date 2015-01-26 14:08:58
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("ci/topicController")
public class TopicController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(TopicController.class);

	@Autowired
	private TopicServiceI topicService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SystemconfigServiceI systemconfigService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * topic列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "topic")
	public ModelAndView topic(HttpServletRequest request) {
		return new ModelAndView("com/wm/topic/topicList");
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
	public void datagrid(TopicEntity topic, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TopicEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				topic);
		this.topicService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除topic
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TopicEntity topic, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		topic = systemService.getEntity(TopicEntity.class, topic.getId());
		message = "删除成功";
		topicService.delete(topic);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加topic
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(TopicEntity topic, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(topic.getId())) {
			message = "更新成功";
			TopicEntity t = topicService.get(TopicEntity.class, topic.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(topic, t);
				topicService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			topicService.save(topic);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}

		return j;
	}

	/**
	 * topic列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TopicEntity topic, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(topic.getId())) {
			topic = topicService.getEntity(TopicEntity.class, topic.getId());
			req.setAttribute("topicPage", topic);
		}
		return new ModelAndView("com/wm/topic/topic");
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(params = "createTopic")
	@ResponseBody
	public AjaxJson createTopic(int userId, int teamId, String title,
			String content, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String fullPath = "";
		Map<String, Object> objs = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<UploadFileVO> files = null;
		String prefixPath = systemconfigService.getValByCode("topic_url");

		int topicId = topicService.createPost(userId, teamId, title, content);

		String filesString = request.getParameter("files");
		if (StringUtil.isNotEmpty(topicId)) {
			objs.put("topicId", topicId);

			if (StringUtil.isNotEmpty(filesString)) {
				JSONArray jsons = JSONArray.fromObject(filesString);
				files = jsons.toList(jsons, UploadFileVO.class);
				UploadFileVO file = files.get(0);
				// 上传头像操作
				String relPathString = topicService.uploadTopicPhoto(topicId,
						file, request.getSession().getServletContext()
								.getRealPath("/"));
				fullPath = prefixPath + relPathString;
				objs.put("fullPath", fullPath);
			} else {
				j.setMsg("图片添加失败");
			}
		} else {
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("操作失败");
		}

		list.add(objs);
		j.setObj(list);
		return j;
	}

}
