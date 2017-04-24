package com.wm.controller.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.VO.UploadFileVO;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.service.team.TeamTopicServiceI;

@Controller
@RequestMapping("ci/teamController")
public class TeamController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(TeamController.class);

	@Autowired
	private SystemconfigServiceI systemconfigService;

	@Autowired
	private TeamTopicServiceI teamTopicService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 */
	@RequestMapping(params = "getTeamTopicList")
	@ResponseBody
	public AjaxJson getTeamTopicList(String userId, String start, String num,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();

		List<Map<String, Object>> list = teamTopicService.getTeamTopicList(
				userId, start, num);
		j.setObj(list);
		j.setSuccess(true);
		return j;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@RequestMapping(params = "createTeam")
	@ResponseBody
	public AjaxJson createTeam(String userId, String teamName, String topic,
			String introduction, String label, HttpServletRequest request,
			HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		String fullPath = "";
		Map<String, Object> objs = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<UploadFileVO> files = null;
		String prefixPath = systemconfigService.getValByCode("team_url");
	
		int teamId = teamTopicService.createTeam(userId, teamName, topic,
				introduction, label);

		String filesString = request.getParameter("files");
		if (StringUtil.isNotEmpty(teamId)) {
			objs.put("teamId", teamId);
			
			if (StringUtil.isNotEmpty(filesString)) {
				JSONArray jsons = JSONArray.fromObject(filesString);
				files = jsons.toList(jsons, UploadFileVO.class);
				UploadFileVO file = files.get(0);
				// 上传头像操作
				String relPathString = teamTopicService.uploadTeamPhoto(teamId,
						file, request.getSession().getServletContext()
								.getRealPath("/"));
				fullPath = prefixPath + relPathString;
				objs.put("fullPath", fullPath);
			} else {
				j.setMsg("头像上传失败");
			}
		}else{
			j.setStateCode("01");
			j.setSuccess(false);
			j.setMsg("操作失败");
		}

		list.add(objs);
		j.setObj(list);
		return j;
	}

}
