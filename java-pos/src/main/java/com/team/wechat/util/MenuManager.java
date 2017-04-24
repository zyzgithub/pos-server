package com.team.wechat.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.team.wechat.vo.Button;
import com.team.wechat.vo.CommonViewButton;
import com.team.wechat.vo.ComplexButton;
import com.team.wechat.vo.Menu;

/**
 * 菜单管理器类
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx078e2a4c0e9eea40";
		// 第三方用户唯一凭证密钥
		String appSecret = "37f94854197ee3d5bf7ed4d47a2ef2ce";

		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonViewButton btn11 = new CommonViewButton();
		btn11.setName("");
		btn11.setType("view");
		//btn11.setKey("11");
		btn11.setUrl("http://211.149.187.23/WX/wap/indexWapController.do?content&contentId=72");
		
		CommonViewButton btn12 = new CommonViewButton();
		btn12.setName("微网站");
		btn12.setType("view");
		btn12.setUrl("http://211.149.187.23/WX/wap/indexWapController.do?index&wechaId=wx001&token=cpnvnh1387105169");

		CommonViewButton btn13 = new CommonViewButton();
		btn13.setName("品牌介绍");
		btn13.setType("view");
		btn13.setUrl("http://211.149.187.23/WX/wap/indexWapController.do?list&classid=315&token=cpnvnh1387105169&wechaId=wx001");

		CommonViewButton btn14 = new CommonViewButton();
		btn14.setName("最新活动");
		btn14.setType("view");
		btn14.setUrl("http://211.149.187.23/WX/wap/indexWapController.do?list&classid=319&token=cpnvnh1387105169&wechaId=wx001");
		

		
		
		
		CommonViewButton btn21 = new CommonViewButton();
		btn21.setName("产品中心");
		btn21.setType("view");
		btn21.setUrl("http://211.149.187.23/WX/wap/productWapController.do?productType");

		CommonViewButton btn22 = new CommonViewButton();
		btn22.setName("网点分布");
		btn22.setType("view");
		btn22.setUrl("http://211.149.187.23/WX/wap/indexWapController.do?companyList");

		
		
		CommonViewButton btn31 = new CommonViewButton();
		btn31.setName("会员中心");
		btn31.setType("view");
		btn31.setUrl("http://211.149.187.23/WX/wap/memberCardWapController.do?centerIndex&token=cpnvnh1387105169");

		CommonViewButton btn32 = new CommonViewButton();
		btn32.setName("娱乐抽奖");
		btn32.setType("view");
		btn32.setUrl("http://211.149.187.23/WX/wap/memberCardWapController.do?lotteryCenter&token=cpnvnh1387105169&wechaId=wx001");

		CommonViewButton btn33 = new CommonViewButton();
		btn33.setName("经销商专区");
		btn33.setType("view");
		btn33.setUrl("http://211.149.187.23/WX/wap/companyWapController.do?login&token=cpnvnh1387105169");

//		CommonViewButton btn32 = new CommonViewButton();
//		btn32.setName("电影排行榜");
//		btn32.setType("view");
//		btn32.setUrl("32");


		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("品牌相关");
		mainBtn1
				.setSub_button(new Button[] { btn12, btn13, btn14});

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("产品网点");
		mainBtn2.setSub_button(new Button[] { btn21, btn22});

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("娱乐抽奖");
		mainBtn3.setSub_button(new Button[] { btn31,btn32,btn33});

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] {mainBtn1,mainBtn2,mainBtn3 });

		return menu;
	}
}