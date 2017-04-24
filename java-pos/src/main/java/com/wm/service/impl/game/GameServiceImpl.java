package com.wm.service.impl.game;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wm.entity.game.GameEntity;
import com.wm.service.game.GameServiceI;
import com.wm.util.StringUtil;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

@Service("gameService")
@Transactional
public class GameServiceImpl extends CommonServiceImpl implements GameServiceI {

	@Override
	public List<GameEntity> getGameList(String enabled) {
		List<GameEntity> list=null;
		if (!StringUtil.isEmpty(enabled)) {
			list = this.commonDao.findByProperty(GameEntity.class, "enabled", enabled);
		}else{
			list = this.commonDao.findByQueryString("from GameEntity");
			
		}
		return list;
	}

	@Override
	public GameEntity getByGameOpenId(String gameOpenId) {
		return this.commonDao.findUniqueByProperty(GameEntity.class, "gameOpenId", gameOpenId);
	}
	
}