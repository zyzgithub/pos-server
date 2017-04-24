package com.team.wechat.controller;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.team.wechat.message.resp.Article;
import com.team.wechat.message.resp.NewsMessage;
import com.team.wechat.util.MapUtil;
import com.team.wechat.util.MessageUtil;
import com.team.wechat.util.SignUtil;
import com.wm.entity.merchant.MerchantEntity;


/**   
 * @Title: Controller
 * @Description: 微信公众平台
 * @author zhangdaihao
 * @date 2013-12-21 23:22:44
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/weChatController")
public class WeChatController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WeChatController.class);
	private String productKeyword = "产品";
	@Autowired
	private CommonService commonService;
	//主页
	//@Autowired
	//private HomeServiceI homeService;
	
	//@Autowired
	//private CompanyServiceI companyService;
	
	//产品
	//@Autowired
	//private ProductServiceI productService;
	//关键字服务类
	//private KeywordServiceI keywordService;
	
	//图文回复
//	@Autowired
//	private ImgResponderServiceI imgResponderService;
	//protected String token = null;//"cpnvnh1387105169";
	
	@Autowired
	private SystemService systemService;
	
	
	private String respMainUrl = null;
	@SuppressWarnings("unused")
	private String wechaId = null;//发送者微信ID
	
	/**
	 * 微信请求主入口
	 */
	public void doSignature(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		PrintWriter out = response.getWriter();
		logger.info("checkSignature:signature="+signature+";timestamp="+timestamp+";nonce="+nonce+";echostr="+echostr);
		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		
		boolean isPass = SignUtil.checkSignature(signature, timestamp, nonce);
		logger.info("checkSignature result:"+isPass);

		if (isPass) {
			out.print(echostr);
		}
		out.flush();
		out.close();
		
	}
	
	
	
	/**
	 * 微信请求主入口
	 */
	@RequestMapping(params = "doHanderRequest")
	public ModelAndView doHanderRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		System.out.println("enter doHanderRequest method");
		//MockHttpServletResponse response1 = new MockHttpServletResponse(); 
	/*	List<ClassifyEntity> classfiyList = classifyService.findByProperty(ClassifyEntity.class, "token", token);
		
		List<FlashEntity> flashList = flashService.findByProperty(FlashEntity.class, "token", token);
		
		request.setAttribute("classfiyList", classfiyList);
		request.setAttribute("flashList", flashList);
		
		return new ModelAndView("wap/Index_128_index");*/
		
		if (request.getMethod().equalsIgnoreCase("Get")){
			 doSignature(request,response);
		}else if (request.getMethod().equalsIgnoreCase("Post")){
			 handerWeChatRequest(request, response);
		}

		return null;
	}
	/**
	 * 微信请求具体处理方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void handerWeChatRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		logger.info("CoreService.processRequest:");
//		token = request.getParameter("token");
//		System.out.println("token:"+token);
		String token = request.getParameter("token");
		//request.getParameter(arg0)
		if(StringUtils.isBlank(token))System.out.println("token is null");
		// 调用核心业务类接收消息、处理消息
		String respMessage = processRequest(request);
		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.flush();
		out.close();
	}
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public String processRequest(HttpServletRequest request) {
		String respMessage = null;
		System.out.println(request.toString());
		
		// PropertiesUtil.getBaseUrl();
		// String BASE_URL = null;
		try {
			// xml请求解析
			
			//FIXME 注释
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			//Map<String,String> requestMap = new HashMap<String, String>();
			//requestMap.put("FromUserName", "chen");
			//requestMap.put("ToUserName", "deng");
			//requestMap.put("MsgType", "location");
			//requestMap.put("Content", "首页");
			//requestMap.put("Location_X", "22.997415");
			//requestMap.put("Location_Y", "113.276605");
		//	requestMap.put("FromUserName", "chen");
			

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			this.wechaId = fromUserName;
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			
			String token = request.getParameter("token");
			
			// 接收用户发送的文本消息内容
			String content = requestMap.get("Content");

			String path = request.getContextPath();
			respMainUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
			
			
			// 创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);

			System.out.println("信息类型：" + msgType);
			
			// List<Article> articleList = new ArrayList<Article>();
			if(MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
				//事件
				String event = requestMap.get("Event");
//				String eventKey = requestMap.get("EventKey");
				System.out.println(event);
				//关注
				if("subscribe".equals(event)) {
//					respMessage = respHomeIndex(token,respMessage, newsMessage);
					//初始化vip用户信息
//					VipUserEntity vipUserEntity = commonService.findUniqueByPropAndToken(VipUserEntity.class, token, "wechaId", wechaId);
//					if(vipUserEntity == null ) {
//						vipUserEntity = new VipUserEntity();
//						vipUserEntity.setWechaId(wechaId);
//						vipUserEntity.setToken(token);
//						commonService.save(vipUserEntity);
//					}
				}else if("CLICK".equals(event)){//菜单点击事件
//					respMessage = this.respMenuClickEvent(token,respMessage, newsMessage, eventKey, fromUserName);
				}else if("VIEW".equals(event)){//菜单跳转链接事件
				} else if(MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equals(event)){
					List<Article> articleList = companysLocation(token,requestMap);
					
					// 设置图文消息个数
					newsMessage.setArticleCount(articleList.size());
					
					// 设置图文消息包含的图文集合
					newsMessage.setArticles(articleList);
					
					// 将图文消息对象转换成xml字符串
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
					System.out.print(respMessage);
					return respMessage;
				}
				
			}
			else if(MessageUtil.REQ_MESSAGE_TYPE_LOCATION.equals(msgType)){
			
//					String path = request.getContextPath();
//					respMainUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
				List<Article> articleList = companysLocation(token,requestMap);
				
				// 设置图文消息个数
				newsMessage.setArticleCount(articleList.size());
				
				// 设置图文消息包含的图文集合
				newsMessage.setArticles(articleList);
				
				// 将图文消息对象转换成xml字符串
				respMessage = MessageUtil.newsMessageToXml(newsMessage);
				System.out.print(respMessage);
				return respMessage;
			} else if (StringUtils.isNotBlank(content)) {
				if ("首页".equals(content)) {
//					respMessage = respHomeIndex(token,respMessage, newsMessage);
				}else if(content.indexOf(productKeyword)!=-1) {
//					System.out.println("产品导购");
//					respMessage = respProduct(token,respMessage, content, newsMessage);
				} else {
					//关键字查询
//					respMessage = respKeyWord(token,respMessage, fromUserName,
//							toUserName, content, newsMessage);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}



	/**
	 * 返回关键字查询结果
	 * @param respMessage
	 * @param fromUserName
	 * @param toUserName
	 * @param content
	 * @param newsMessage
	 * @return
	 */
//	private String respKeyWord(String token,String respMessage, String fromUserName,
//			String toUserName, String content, NewsMessage newsMessage) {
//		StringBuffer sql = new StringBuffer();
//		sql.append("from KeywordEntity as k where k.token = ")
//		.append("'")
//		.append(token)
//		.append("' and k.keyword = '")
//		.append(content)
//		.append("'");
//		KeywordEntity keyWord = commonService.singleResult(sql.toString());
//		if(keyWord!=null) {
//			//说明关键字存在
//			String module = keyWord.getModule();
//			System.out.println("module:"+module);
//			if("text".endsWith(module)) {
//				StringBuffer textSql = new StringBuffer();
//				textSql.append("from TextResponderEntity t where t.token = ");
//				textSql.append("'");
//				textSql.append(token);
//				textSql.append("' and t.keyword = '");
//				textSql.append(content);
//				textSql.append("'");
//				//关键字类型是文本
//				TextResponderEntity textResponderEntity = commonService.singleResult(textSql.toString());
//				if(textResponderEntity!=null) {
//					TextMessage textMsg = new TextMessage();
//					textMsg.setFromUserName(toUserName);
//					textMsg.setToUserName(fromUserName);
//					textMsg.setCreateTime(new Date().getTime());
//					textMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//					textMsg.setFuncFlag(0);
//					textMsg.setContent(textResponderEntity.getText());
//					respMessage = MessageUtil.textMessageToXml(textMsg);
//				}
//			}else if("img".endsWith(module)) {
//				//关键字类型是图文
//				//imgResponderService.
//				//commonService.findByQueryString(hql)
//				respMessage = respImg(token,respMessage, content,
//						newsMessage);
//			}
//		}else {
//			//关键字不存在
//		}
//		return respMessage;
//	}
//	private String respKeyWord(String token,String respMessage, String fromUserName,
//			String toUserName, String content, NewsMessage newsMessage) {
//		StringBuffer sql = new StringBuffer();
//		sql.append("from KeywordEntity as k where k.token = ")
//			.append("'")
//			.append(token)
//			.append("' and (k.keyword = '")
//			.append(content)
//			.append("' or k.fuzzyQuery like '%")
//			.append(content)
//			.append("%')");
//		List<KeywordEntity> keyWordList = commonService.findByQueryString(sql.toString());
//		System.out.println(keyWordList.size());
//		List<Article> articleList = new ArrayList<Article>();
//		if(keyWordList != null && keyWordList.size() >0){
//			
//			for (KeywordEntity keyword : keyWordList) {
//				System.out.println("keyword module:" + keyword.getModule());
//				if("text".equals(keyword.getModule())){
//					
//				}else if("img".equals(keyword.getModule())){
//					ImgResponderEntity imgResponder = commonService.get(ImgResponderEntity.class, keyword.getPid());
//					Article articleTemp = new Article();
//					articleTemp.setTitle(imgResponder.getTitle());
//					articleTemp.setDescription(imgResponder.getText());
//					articleTemp.setPicUrl(imgResponder.getPic());
//					articleTemp.setUrl(this.respMainUrl + "wap/" + imgResponder.getUrl() + "&token=" + token + "&wechaId=" + wechaId);
//					articleList.add(articleTemp);
//				}else if("product".equals(keyword.getModule())){
//					ProductEntity productEntity = commonService.get(ProductEntity.class, keyword.getPid());
//					Article article = new Article();
//					article.setTitle(productEntity.getName());
//					//article.setDescription(productEntity.getIntro());
//					article.setPicUrl(productEntity.getLogourl());
//					article.setUrl(this.respMainUrl + "wap/productWapController.do?productContent&productid="+productEntity.getId()+"&token="+token+"&wechaId="+wechaId);
//					System.out.println("URL：" + article.getUrl());
//					articleList.add(article);
//				}
//			}
//			// 设置图文消息个数
//			newsMessage.setArticleCount(articleList.size());			
//			// 设置图文消息包含的图文集合
//			newsMessage.setArticles(articleList);			
//			// 将图文消息对象转换成xml字符串
//			respMessage = MessageUtil.newsMessageToXml(newsMessage);
//			
//			System.out.print(respMessage);
//		}else {
//			//关键字不存在
//			TextMessage textMsg = new TextMessage();
//			textMsg.setFromUserName(toUserName);
//			textMsg.setToUserName(fromUserName);
//			textMsg.setCreateTime(new Date().getTime());
//			textMsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//			textMsg.setFuncFlag(0);
//			textMsg.setContent("关键字不存在！");
//			respMessage = MessageUtil.textMessageToXml(textMsg);
//
//		}
//		return respMessage;
//	}

	/**
	 * @param respMessage
	 * @param content
	 * @param newsMessage
	 * @return
	 */
//	private String respProduct(String token,String respMessage, String content,
//			NewsMessage newsMessage) {
//		//产品导购
//		StringBuffer sql = new StringBuffer();
//		//ProductEntity
//		sql.append("from ProductEntity as p where p.token = ")
//		.append("'")
//		.append(token)
//		.append("' and p.keyword like '%")
//		.append(content.substring(content.indexOf(productKeyword)+productKeyword.length()+1))
//		.append("%'");
//		List<ProductEntity> productList = commonService.findByQueryString(sql.toString());
//		
//		if(productList!=null && productList.size()>0) {
//			//存在对应数据
//			for (ProductEntity productEntity : productList) {
//				List<Article> articleList = new ArrayList<Article>();
//				Article article = new Article();
//				article.setTitle(productEntity.getName());
//				//article.setDescription(productEntity.getIntro());
//				article.setPicUrl(productEntity.getLogourl());
//				article.setUrl(this.respMainUrl + "wap/productWapController.do?productContent&productid="+productEntity.getId()+"&token="+token+"&wechaId="+wechaId);
//				System.out.println("URL：" + article.getUrl());
//				articleList.add(article);
//				// 设置图文消息个数
//				newsMessage.setArticleCount(articleList.size());
//				// 设置图文消息包含的图文集合
//				newsMessage.setArticles(articleList);
//				// 将图文消息对象转换成xml字符串
//				respMessage = MessageUtil.newsMessageToXml(newsMessage);
//			}
//		} else {
//			//查询不到数据
//		}
//		return respMessage;
//	}



	/**
	 * 返回首页信息
	 * @param respMessage
	 * @param newsMessage
	 * @return
	 */
//	private String respHomeIndex(String token,String respMessage, NewsMessage newsMessage) {
//		HomeEntity homeEntity = commonService.findUniqueByProperty(
//				HomeEntity.class, "token", token);
//		if (homeEntity != null) { 
//			List<Article> articleList = new ArrayList<Article>();
//			Article article = new Article();
//			article.setTitle(homeEntity.getTitle());
//			article.setDescription(homeEntity.getInfo());
//			article.setPicUrl(homeEntity.getPicurl());
//			//article.setUrl(homeEntity.getHomeurl());写死
//			article.setUrl(this.respMainUrl + "wap/indexWapController.do?index&wechaId=" + wechaId+"&token="+token);
//			System.out.println("首页：" + article.getUrl());
//			articleList.add(article);
//			// 设置图文消息个数
//			newsMessage.setArticleCount(articleList.size());
//			// 设置图文消息包含的图文集合
//			newsMessage.setArticles(articleList);
//			// 将图文消息对象转换成xml字符串
//			respMessage = MessageUtil.newsMessageToXml(newsMessage);
//		}
//		return respMessage;
//	}
	/**
	 * 处理菜单点击事件
	 * @param respMessage
	 * @param newsMessage
	 * @param key
	 * @param wechaId
	 * @return
	 */
//	private String respMenuClickEvent(String token,String respMessage, NewsMessage newsMessage, String key, String wechaId) {
//		int keyValue = Integer.parseInt(key);
//		DiymenClassEntity diymenClass = commonService.findUniqueByPropAndToken(
//				DiymenClassEntity.class, token, "id", keyValue);
//		if (diymenClass != null) {
//			List<Article> articleList = new ArrayList<Article>();
//			Article article = new Article();
//			article.setTitle(diymenClass.getTitle());
//			if(!StringUtil.isEmpty(diymenClass.getKeyword()) && diymenClass.getKeyword().indexOf("会员") != -1){//会员中心查询用户名称
//				VipUserEntity vipUserEntity = commonService.findUniqueByPropAndToken(VipUserEntity.class, token, "wechaId", wechaId);
//				if(vipUserEntity != null){
//					if(!StringUtil.isEmpty(vipUserEntity.getWechaname())){
//						article.setDescription("欢迎您回来，" + vipUserEntity.getWechaname());					
//					}else{
//						article.setDescription("点击领取会员卡。");
//					}
//				}
//			}else{
//				article.setDescription(diymenClass.getDescribable());	
//			}
//			article.setPicUrl(diymenClass.getPicUrl());
//			article.setUrl(diymenClass.getUrl() + "&wechaId=" + wechaId + "#wechat_redirect");
//			articleList.add(article);
//			// 设置图文消息个数
//			newsMessage.setArticleCount(articleList.size());
//			// 设置图文消息包含的图文集合
//			newsMessage.setArticles(articleList);
//			// 将图文消息对象转换成xml字符串
//			respMessage = MessageUtil.newsMessageToXml(newsMessage);
//		}
//		return respMessage;
//	}


	
	/**
	 * 获取距离最近的5个网点位置信息
	 * http://localhost:8080/WX/weChatController.do?doHanderRequest&msgType=location
	 * @param requestMap
	 * @return
	 */
	private List<Article> companysLocation(String token ,Map<String,String> requestMap){
		
		//获取用户位置信息
		String startX = requestMap.get("Location_X");
		String startY = requestMap.get("Location_Y");
		
		//查出网点位置
		List<MerchantEntity> merchantList = commonService.findByProperty(MerchantEntity.class, "display", "Y");
		List<Double> distanceList = new ArrayList<Double>();
		Map<Double,MerchantEntity> locationMerchantMap = new HashMap<Double,MerchantEntity>();
		Double distance ;
		
		//计算网点距离
		for(MerchantEntity mer : merchantList){
			distance = 0d;
			distance =MapUtil.GetShortDistance(MapUtil.strToDouble(startX), MapUtil.strToDouble(startY), mer.getLng(), mer.getLat());
			distanceList.add(distance);
			locationMerchantMap.put(distance, mer);
		}
		
		//排序网点
		Collections.sort(distanceList, new Comparator<Double>(){
			@Override
			public int compare(Double o1, Double o2) {
				// TODO Auto-generated method stub
				if(o1 == null)
					return  -1;
				if(o2 == null)
					return 1;
				return (int)((Double)o2-(Double)o1);
			}
			
		});
		
		List<Article> articleList = new ArrayList<Article>();
		
		for(int i = 0; i<distanceList.size() && i<5 ;i++){
			MerchantEntity merchant = locationMerchantMap.get(distanceList.get(i));
			
			Article article = new Article();
			article.setTitle(merchant.getTitle());
			article.setDescription(merchant.getAddress()+"<br/>联系方式:"+merchant.getMobile());
			article.setPicUrl(systemService.getSystemConfigValue("img_url") + merchant.getLogoUrl());
//			
//			String api = "http://api.map.baidu.com/direction?origin=latlng:"+startX+","+startY+"|name:出发地&"
//				+"destination=latlng:"+c.getLatitude()+","+c.getLongitude()+"|name:目的地&region=广州"
//				+"&mode=driving&output=html&src=yourCompanyName|yourAppName";
	//		String mapEncodeURL = null;
		/*	try {
				 mapEncodeURL = URLEncoder.encode(api, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			String api = respMainUrl+"takeOutController.do?merchantList&merchantid=" + merchant.getId();
			article.setUrl(api);
			articleList.add(article);
		
		}
		
		return articleList;
		
	}
	
}
