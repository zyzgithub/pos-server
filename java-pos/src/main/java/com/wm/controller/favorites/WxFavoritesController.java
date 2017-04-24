package com.wm.controller.favorites;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.takeout.vo.MerchantSimpleVo;
import com.wm.entity.favorites.FavoritesEntity;
import com.wm.entity.location.Location;
import com.wm.service.favorites.FavoritesServiceI;
import com.wm.service.merchant.MerchantServiceI;

/**
 * @Title: Controller
 * @Description: favorites
 * @author wuyong
 * @date 2015-01-07 09:57:02
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/wxfavoritesController")
public class WxFavoritesController extends BaseController {
	/**
	 * Logger for this class
	 */
	// private static final Logger logger =
	// Logger.getLogger(FavoritesController.class);

	@Autowired
	private FavoritesServiceI favoritesService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private MerchantServiceI merchantService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@RequestMapping(value = "/wxuserFavMerchants.do")
	@ResponseBody
	public Map<String, Object> wxuserFavMerchants(HttpServletRequest request,
			@RequestParam int page, @RequestParam int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId = getUserId(request, null);
		if(null == userId) {
			map.put("state", "fail");
			return map;
		}
		
		Location loc = getLocation(request);
		// step 1 根据用户id查用户收藏店铺
		List<MerchantSimpleVo> merchants = merchantService.queryUserFavMerchantByUserId(userId, loc.getLng(), loc.getLat(), page, rows);
		// step 2 把用户最新的经纬度保存在session
		map.put("state", "success");
		map.put("obj", merchants);
		return map;
	}
	
	@RequestMapping(value="/add.do", method=RequestMethod.POST)
	@ResponseBody
	private void add(@RequestParam Integer merchantId, HttpServletRequest request) {
		Integer userid = getUserId(request, null);
		favoritesService.collectOrCancle("2", userid, merchantId);
	}

	@RequestMapping(value="/get.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, String> get(@RequestParam Integer merchantId, HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		Integer userId = getUserId(request, null);
		FavoritesEntity fav = favoritesService.findByUserAndMerchant(userId, merchantId);
		
		if(null != fav)
			map.put("state", "success");
		else
			map.put("state", "fail");
		
		return map;
	}
	
}
