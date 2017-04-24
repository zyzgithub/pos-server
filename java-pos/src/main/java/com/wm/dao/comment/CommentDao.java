package com.wm.dao.comment;

import java.util.List;

import org.jeecgframework.core.common.dao.IGenericBaseCommonDao;

import com.wm.controller.takeout.vo.CommentScoreVo;
import com.wm.controller.takeout.vo.CommentVo;

public interface CommentDao extends IGenericBaseCommonDao {

	CommentScoreVo queryCommentScore(int target, int targetId);

	List<CommentVo> queryByMerchantId(int target, int targetId, int start, int rows);
	
	List<CommentVo> queryByCourierId(String display ,int target, int targetId, int start, int rows);
	
	CommentScoreVo queryCommentScore(int target, int targetId,List<String> displayList );

}
