package com.wm.service.comment;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.controller.takeout.vo.CommentScoreVo;
import com.wm.controller.takeout.vo.CommentVo;

public interface CommentServiceI extends CommonService{

	CommentScoreVo queryCommentScore(int target, int targetId);

	List<CommentVo> queryByMerChantId(int target, int targetId, int start, int rows);
	
	CommentScoreVo queryCommentScore(int target, int targetId,List<String> displayList ); 

	List<CommentVo> queryByCourierId(String display,int target, int targetId, int page, int rows);
	
	/**
	 * 统计某一天快递员获得的评价总数、总分
	 * @param courierId
	 * @param commentDate
	 */
	Integer[] getCourierDaylyCommentScore(Integer courierId, String commentDate);

	/**
	 * 查询商家评论统计
	 * @param merchantId
	 * @param statiDate 评论日期 yyyy-MM-dd
	 * @return
	 */
	List<Map<String, Object>> findCommentStati(Integer merchantId, String statiDate);

}
