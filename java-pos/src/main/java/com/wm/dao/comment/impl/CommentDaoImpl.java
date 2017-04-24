package com.wm.dao.comment.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.dao.impl.GenericBaseCommonDao;
import org.springframework.stereotype.Repository;

import com.wm.controller.takeout.vo.CommentScoreVo;
import com.wm.controller.takeout.vo.CommentVo;
import com.wm.dao.comment.CommentDao;
import com.wm.entity.comment.CommentEntity;

@Repository("commentDao")
@SuppressWarnings("unchecked")
public class CommentDaoImpl extends GenericBaseCommonDao<CommentEntity, Integer> implements CommentDao {

	@Override
	public CommentScoreVo queryCommentScore(int target, int targetId) {
		// TODO Auto-generated method stub
		String sql = "select count(oc.id) as total, cast(ifnull(avg(oc.grade), 5) as decimal(2, 1)) as score "
				+ "from 0085_order_comment as oc "
				+ "where oc.comment_target = ? and oc.comment_target_id = ? and oc.comment_display = ?";
		
		SQLQuery query = createSqlQuery(sql, target, targetId, "Y");
		query.setResultTransformer(Transformers.aliasToBean(CommentScoreVo.class));
		return (CommentScoreVo)query.uniqueResult();
	}

	@Override
	public List<CommentVo> queryByMerchantId(int target, int targetId, int start, int rows) {
		// TODO Auto-generated method stub
		String sql = "select oc.comment_content as content, oc.grade as score, "
				+ "from_unixtime(oc.comment_time, '%Y-%m-%d %H:%i') as time, "
				+ "u.nickname as userName, u.photoUrl as headIcon "
				+ "from 0085_order_comment as oc, user as u "
				+ "where oc.comment_target = ? and oc.comment_target_id = ? "
				+ "and oc.comment_display = ? and oc.user_id = u.id "
				+ "order by oc.id desc";
		SQLQuery query = createSqlQuery(sql, target, targetId, "Y");
		query.setFirstResult(start).setMaxResults(rows);
		query.setResultTransformer(Transformers.aliasToBean(CommentVo.class));
		return query.list();
	}
	
	@Override
	public List<CommentVo> queryByCourierId(String display ,int target, int targetId, int start, int rows) {
		// TODO Auto-generated method stub
		String sql = "select oc.comment_content as content, oc.grade as score, "
				+ "from_unixtime(oc.comment_time, '%Y-%m-%d %H:%i') as time, "
				+ "u.username as userName, u.photoUrl as headIcon "
				+ "from 0085_order_comment as oc, user as u "
				+ "where oc.comment_target = ? and oc.comment_target_id = ? "
				+ "and oc.comment_display = ? and oc.user_id = u.id "
				+ "order by oc.id desc";
		SQLQuery query = createSqlQuery(sql, target, targetId, display);
		query.setFirstResult(start).setMaxResults(rows);
		query.setResultTransformer(Transformers.aliasToBean(CommentVo.class));
		return query.list();
	}
	
	@Override
	public CommentScoreVo queryCommentScore(int target, int targetId,List<String> displayList ) {
		// TODO Auto-generated method stub
		String sql = "select count(oc.id) as total, cast(ifnull(avg(oc.grade), 5) as decimal(2, 1)) as score "
				+ "from 0085_order_comment as oc "
				+ "where oc.comment_target = ? and oc.comment_target_id = ? and comment_display in (? , ?)";
		
		SQLQuery query = createSqlQuery(sql, target, targetId,displayList.get(0),displayList.get(1));
		query.setResultTransformer(Transformers.aliasToBean(CommentScoreVo.class));
		return (CommentScoreVo)query.uniqueResult();
	}
}
