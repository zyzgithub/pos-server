package com.wm.service.game;

import java.util.List;

import org.jeecgframework.core.common.service.CommonService;

import com.wm.entity.game.GameEntity;

/**
 * 游戏分数积分兑换规则 数据访问层接口
 * @author lfq
 * @email  545987886@qq.com
 * @2015-5-11
 */
public interface GameServiceI extends CommonService{

	/**
	 * 查询游戏分数积分兑换规则列表
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param enabled  是否启用:0未启用，1已启动
	 * @return
	 */
	List<GameEntity> getGameList(String enabled);
	
	/**
	 * 根据游戏openId获取游戏分数积分兑换规则对象
	 * @author lfq
	 * @email  545987886@qq.com
	 * @param gameOpenId
	 * @return
	 */
	GameEntity  getByGameOpenId(String gameOpenId);
}
