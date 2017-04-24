package com.wm.service.impl.comment;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.wm.controller.takeout.vo.CommentScoreVo;
import com.wm.controller.takeout.vo.CommentVo;
import com.wm.dao.comment.CommentDao;
import com.wm.service.comment.CommentServiceI;

@Service("commentService")
@Transactional
public class CommentServiceImpl extends CommonServiceImpl implements CommentServiceI {

	@Autowired
	private CommentDao commentDao;
	
	@Override
	public CommentScoreVo queryCommentScore(int target, int targetId) {
		return commentDao.queryCommentScore(target, targetId);
	}

	@Override
	public List<CommentVo> queryByMerChantId(int target, int targetId, int start, int rows) {
		return commentDao.queryByMerchantId(target, targetId, start, rows);
	}
	

	@Override
	public List<CommentVo> queryByCourierId(String display, int target,
			int targetId, int page, int rows) {
		StringBuffer sbsql = new StringBuffer();
		sbsql.append("select oc.order_id as orderId, CONCAT(IFNULL(oc.tags,''),'  ',IFNULL(oc.comment_content,'')) as content, oc.grade as score, ")
				.append("from_unixtime(oc.comment_time , '%Y-%m-%d %H:%i:%s') as time, ")
				.append("u.username as userName, u.photoUrl as headIcon, u.mobile as mobile")
				.append(" FROM 0085_order_comment  as oc, user as u")
				.append(" WHERE ")
				.append(" oc.comment_target = ? and oc.comment_target_id = ? ")
				.append(" and oc.comment_display = ? and oc.user_id = u.id ")
				.append(" order by oc.comment_time desc ");
		List<CommentVo> orvList = Lists.newArrayList();
		orvList = this.findObjForJdbc(sbsql.toString(), page, rows,
				CommentVo.class,target,targetId,display);
		return orvList;
	}
	
	
	@Override
	public CommentScoreVo queryCommentScore(int target, int targetId, List<String> displayList ) {
		return commentDao.queryCommentScore(target, targetId, displayList);
	}

	@Override
	public Integer[] getCourierDaylyCommentScore(Integer courierId, String commentDate) {
		Integer totalComm = 0;
		Integer totalScore = 0;
		String sql= "select count(*) c, sum(grade) s from 0085_order_comment where comment_target=0 and comment_target_id=? and date(from_unixtime(comment_time))=?";
		List<Map<String, Object>> list = this.findForJdbc(sql, new Object[]{courierId, commentDate});
		if(list != null && list.size() > 0){
			Map<String, Object> map = list.get(0);
			if(map.get("c") != null){
				totalComm = Integer.parseInt(map.get("c").toString());
			}
			if(map.get("s") != null){
				totalScore = Integer.parseInt(map.get("s").toString());
			}
		}
		
		return new Integer[]{totalComm, totalScore};
	}

	@Override
	public List<Map<String, Object>> findCommentStati(Integer merchantId, String statiDate) {
		String sql= "select count(*) c, sum(grade) s from 0085_order_comment "
				+ " where comment_target=1 and comment_target_id=? and date(from_unixtime(comment_time))=?";
		return this.findForJdbc(sql, new Object[]{merchantId, statiDate});
	}

}