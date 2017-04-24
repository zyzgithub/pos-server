package com.wm.controller.comment;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.controller.takeout.vo.CommentScoreVo;
import com.wm.controller.takeout.vo.CommentVo;
import com.wm.entity.comment.CommentEntity;
import com.wm.service.comment.CommentServiceI;

/**
 * @Title: Controller
 * @Description: 评论/评价
 * @author wuyong
 * @date 2015-08-13 19:59:03
 * @version V1.0
 *
 */
@Controller
@RequestMapping("ci/commentController")
public class CommentController extends BaseController {

	@Autowired
	private CommentServiceI commentService;
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
	 * 评论/评价列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "comment")
	public ModelAndView comment(HttpServletRequest request) {
		return new ModelAndView("com/wm/comment/commentList");
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
	public void datagrid(CommentEntity comment, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CommentEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				comment);
		this.commentService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除评论/评价
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CommentEntity comment, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		comment = systemService.getEntity(CommentEntity.class, comment.getId());
		message = "删除成功";
		commentService.delete(comment);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加评论/评价
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CommentEntity comment, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(comment.getId())) {
			message = "更新成功";
			CommentEntity t = commentService.get(CommentEntity.class,
					comment.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(comment, t);
				commentService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			commentService.save(comment);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}
		return j;
	}

	/**
	 * 评论/评价列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CommentEntity comment,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(comment.getId())) {
			comment = commentService.getEntity(CommentEntity.class,
					comment.getId());
			req.setAttribute("commentPage", comment);
		}
		return new ModelAndView("com/wm/comment/comment");
	}

	/**
	 * 获得快递员评价列表
	 * 
	 * @param display
	 *            是否显示
	 * @param target
	 *            评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 * @param targetId
	 *            评价对象主键
	 * @param page
	 *            起始页(从1开始)
	 * @param rows
	 *            每页显示行数
	 * @return
	 */
	@RequestMapping(params = "queryByCourierId")
	@ResponseBody
	public AjaxJson queryByCourierId(String display, int target, int targetId,
			int page, int rows) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(display) && targetId != 0) {
			if (page == 0) {
				page = 1;
			}
			List<CommentVo> comments = commentService.queryByCourierId(display,
					target, targetId, page, rows);
			if (comments != null && comments.size() > 0) {
				j.setObj(comments);
				j.setSuccess(true);
			} else {
				j.setMsg("暂无评论");
				j.setSuccess(false);
			}
		} else {
			j.setMsg("查询评论失败，显示标记不允许为空！");
			j.setSuccess(false);
		}
		return j;
	}

	/**
	 * 获得快递员评分星级
	 * 
	 * @param target
	 *            评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	 * @param targetId
	 *            评价对象主键
	 * @return
	 */
	@RequestMapping(params = "queryCommentScore")
	@ResponseBody
	public AjaxJson queryCommentScore(int target, int targetId) {
		AjaxJson j = new AjaxJson();
		List<String> displayList = new ArrayList<String>();
		displayList.add("Y");
		displayList.add("S");
		if (targetId != 0) {
			CommentScoreVo commentScore = commentService.queryCommentScore(
					target, targetId, displayList);
			if (commentScore != null) {
				j.setObj(commentScore);
				j.setSuccess(true);
			} else {
				j.setMsg("暂无评分");
				j.setSuccess(false);
			}
		} else {
			j.setMsg("查询评分失败，快递员ID不允许为空！");
			j.setSuccess(false);
		}
		return j;
	}
}
