package com.wm.service.impl.networkDevelop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.wm.controller.networkDevelop.dto.CommunityInfoDTO;
import com.wm.controller.networkDevelop.dto.CompetitionAnalysisDTO;
import com.wm.controller.networkDevelop.dto.MerchantInfoDTO;
import com.wm.controller.networkDevelop.dto.NetworkDevelopDTO;
import com.wm.controller.networkDevelop.dto.ShopDtlDTO;
import com.wm.entity.networkDevelop.NetworkDevelopmentEntity;
import com.wm.entity.networkDevelop.NetworkPhotosEntity;
import com.wm.entity.networkDevelop.ShopDetailEntity;
import com.wm.service.category.CategoryServiceI;
import com.wm.service.networkDevelop.NetworkDevelopServiceI;
import com.wm.util.StringUtil;

@Service("networkDevelopService")
@Transactional
public class NetworkDevelopServiceImpl  extends CommonServiceImpl implements NetworkDevelopServiceI{
	
	private static final Logger logger = LoggerFactory.getLogger(NetworkDevelopServiceImpl.class);

	@Autowired
	private CategoryServiceI categoryService;
	
	@Override
	public AjaxJson saveCommunityInfo(CommunityInfoDTO dto) {
		AjaxJson json = new AjaxJson();
		NetworkDevelopmentEntity entity = null;
		try {
			if(dto.getId()!=0){
				entity = this.get(NetworkDevelopmentEntity.class, dto.getId());
			}
			if(entity != null){
				if(entity.getState().intValue() == 2){
					json.setMsg("此网点已通过审核");
					json.setSuccess(false);
					json.setStateCode("03");
					return json;
				}
//				if(entity.getCompleteStatus().equals("1,2,3,4") && entity.getState().intValue() == 1){
//					json.setMsg("此网点已处于待审核状态，请勿更改");
//					json.setSuccess(false);
//					json.setStateCode("04");
//					return json;
//				}
				String completeStatus = getCompleteStatus(entity, "1");
				entity.setCompleteStatus(completeStatus);
			}
			if(entity == null){
				entity = new NetworkDevelopmentEntity();
				entity.setCompleteStatus("1");
				entity.setCreateDate(DateUtils.getDate());
			}
			entity.setCourierId(dto.getCourierId());
			entity.setCommunityName(dto.getCommunityName());
			entity.setHousehold(dto.getHousehold());
			entity.setCommunityAddress(dto.getCommunityAddress());
			entity.setLatitude(new BigDecimal(dto.getLatitude()));
			entity.setLongitude(new BigDecimal(dto.getLongitude()));
			entity.setRent(Double.parseDouble(dto.getRent()));
			entity.setHousePrice(Double.parseDouble(dto.getHousePrice()));
			entity.setDoneDate(DateUtils.getDate());
			entity.setState(1);
			saveOrUpdate(entity);
			Map<String,Integer> rtMap=new HashMap<String,Integer>();
			rtMap.put("id", entity.getId());
			rtMap.put("communitySet",1);//给前端标识是否已设置，默认未设置，1已设置
			json.setObj(rtMap);
			json.setMsg("保存社区资料成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存社区资料失败");
			json.setSuccess(false);
			json.setStateCode("02");
		}
		return json;
	}

	@Override
	public AjaxJson saveCompetitionAnalysis(CompetitionAnalysisDTO dto) {
		AjaxJson json = new AjaxJson();
		NetworkDevelopmentEntity entity = null;
		try {
			if(dto.getId()!=0){
				entity = this.get(NetworkDevelopmentEntity.class, dto.getId());
			}
			if(entity != null){
				if(entity.getState().intValue() == 2){
					json.setMsg("此网点已通过审核");
					json.setSuccess(false);
					json.setStateCode("03");
					return json;
				}
//				if(entity.getCompleteStatus().equals("1,2,3,4") && entity.getState().intValue() == 1){
//					json.setMsg("此网点已处于待审核状态，请勿更改");
//					json.setSuccess(false);
//					json.setStateCode("04");
//					return json;
//				}
				String completeStatus = getCompleteStatus(entity, "2");
				entity.setCompleteStatus(completeStatus);
			}
			if(entity == null){
				entity = new NetworkDevelopmentEntity();
				entity.setCompleteStatus("2");
				entity.setCreateDate(DateUtils.getDate());
//				json.setMsg("请先填写社区资料");
//				json.setSuccess(false);
//				json.setStateCode("01");
//				return json;
			}
			entity.setCourierId(dto.getCourierId());
			entity.setElmAmount(dto.getElmAmount());
			entity.setBaiduAmount(dto.getBaiduAmount());
			entity.setMeituanAmount(dto.getMeituanAmount());
			entity.setKoubeiAmount(dto.getKoubeiAmount());
			entity.setOtherAmount(dto.getOtherAmount());
			entity.setOrderAmount(dto.getOrderAmount());
			entity.setDoneDate(DateUtils.getDate());
			entity.setState(1);
			saveOrUpdate(entity);	
			Map<String,Integer> rtMap=new HashMap<String,Integer>();
			rtMap.put("id", entity.getId());
			rtMap.put("CompetitionSet",1);//给前端标识是否已设置，默认未设置，1已设置
			json.setObj(rtMap);
			json.setMsg("保存竞争分析记录成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存竞争分析记录失败");
			json.setSuccess(false);
			json.setStateCode("02");
		}
		return json;
	}

	@Override
	public AjaxJson saveMerchantInfo(MerchantInfoDTO dto) {
		AjaxJson json = new AjaxJson();
		NetworkDevelopmentEntity entity = null;
		try {
			if(dto.getId()!=0){
				entity = this.get(NetworkDevelopmentEntity.class, dto.getId());
			}
			if(entity != null){
				if(entity.getState().intValue() == 2){
					json.setMsg("此网点已通过审核");
					json.setSuccess(false);
					json.setStateCode("03");
					return json;
				}
//				if(entity.getCompleteStatus().equals("1,2,3,4") && entity.getState().intValue() == 1){
//					json.setMsg("此网点已处于待审核状态，请勿更改");
//					json.setSuccess(false);
//					json.setStateCode("04");
//					return json;
//				}
				String completeStatus = getCompleteStatus(entity, "3");
				entity.setCompleteStatus(completeStatus);
			}
			if(entity == null){
				entity = new NetworkDevelopmentEntity();
				entity.setCompleteStatus("3");
				entity.setCreateDate(DateUtils.getDate());
//				json.setMsg("请先填写社区资料");
//				json.setSuccess(false);
//				json.setStateCode("01");
//				return json;
			}
			entity.setCourierId(dto.getCourierId());
			entity.setShopAmount(Integer.parseInt(dto.getShopTotalAmount()));
			entity.setShopRent(Double.parseDouble(dto.getShopRent()));
			entity.setDoneDate(DateUtils.getDate());
			entity.setState(1);
			saveOrUpdate(entity);	
			Map<String,Integer> rtMap=new HashMap<String,Integer>();
			rtMap.put("id", entity.getId());
			rtMap.put("merchantInfoSet",1);//给前端标识是否已设置，默认未设置，1已设置
			json.setObj(rtMap);
			json.setMsg("保存商家情况成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("保存商家情况失败");
			json.setSuccess(false);
			json.setStateCode("02");
		}
		return json;
	}

	@Override
	public AjaxJson saveShopDtl(ShopDtlDTO dto) {
		AjaxJson json = new AjaxJson();
		NetworkDevelopmentEntity entity = null;
		try {
			if(dto.getId()!=0){
				entity = this.get(NetworkDevelopmentEntity.class, dto.getId());
			}
			if(entity != null){
				if(entity.getState().intValue() == 2){
					json.setMsg("此网点已通过审核");
					json.setSuccess(false);
					json.setStateCode("03");
					return json;
				}
//				if(entity.getCompleteStatus().equals("1,2,3,4") && entity.getState().intValue() == 1){
//					json.setMsg("此网点已处于待审核状态，请勿更改");
//					json.setSuccess(false);
//					json.setStateCode("04");
//					return json;
//				}
				String completeStatus = getCompleteStatus(entity, "4");
				entity.setCompleteStatus(completeStatus);
			}
			if(entity == null){
				entity = new NetworkDevelopmentEntity();
				entity.setCompleteStatus("4");
				entity.setCreateDate(DateUtils.getDate());
//				json.setMsg("请先填写社区资料");
//				json.setSuccess(false);
//				json.setStateCode("01");
//				return json;
			}
			entity.setCourierId(dto.getCourierId());
			entity.setState(1);
			entity.setDoneDate(DateUtils.getDate());
			saveOrUpdate(entity);
			
			int id=dto.getId();
			int type = dto.getType();
			ShopDetailEntity shoppEntity = getShopDetailEntityByNidAndType(id, type);
			if(shoppEntity == null){
				shoppEntity = new ShopDetailEntity();
			}
			logger.info(JSON.toJSONString(shoppEntity));
			shoppEntity.setNetworkDevid(id);
			shoppEntity.setShopAmount(dto.getShopAmount());
			shoppEntity.setType(type);
			saveOrUpdate(shoppEntity);
			List<NetworkPhotosEntity> shopList = findObjForJdbc("select photo_type photoType, url from 0085_network_photos where networkd_devid = ? order by photo_type,photo_id", NetworkPhotosEntity.class, id);
			String[] urls=dto.getUrl().toString().split(",");
			boolean isHaveUrl = false;
			for (int i = 0; i < urls.length; i++) {
				if (!StringUtil.isEmpty(urls[i])) {
					for(NetworkPhotosEntity networkPhotosEntity : shopList){
						if(urls[i].equals(networkPhotosEntity.getUrl()) && type == networkPhotosEntity.getPhotoType().intValue()){
							//如果已存在的相同类型的相同的URL
							isHaveUrl = true;	
						}
					}
					//没有的URL就保存
					if(!isHaveUrl){
						NetworkPhotosEntity photosEntity=new NetworkPhotosEntity();
						photosEntity.setPhotoType(type);
						photosEntity.setUrl(urls[i]);
						photosEntity.setNetworkdDevid(id);
						photosEntity.setPhotoOrder(i);
						saveOrUpdate(photosEntity);
					}
				}
			}
			Map<String,Integer> rtMap=new HashMap<String,Integer>();
			rtMap.put("id", entity.getId());
			rtMap.put("ShopDtlSet",1);//给前端标识是否已设置，默认未设置，1已设置
			json.setObj(rtMap);
			json.setMsg("保存商家详情成功");
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			if(e instanceof IncorrectResultSetColumnCountException){
				e.printStackTrace();
				logger.warn("数据异常，同一社区id" + dto.getId() + "同一门店类型type" + dto.getType() + "存在多条记录");
				json.setMsg("数据异常");
				json.setSuccess(false);
				json.setStateCode("04");
				return json;
			}
			e.printStackTrace();
			json.setMsg("保存商家详情失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}


	@Override
	public List<Map<String, Object>> getNetworkRecord(Integer courierId,
			int page, int rows) {
		StringBuilder query = new StringBuilder();
		query.append("select id, community_name communityName,create_date createDate, state, ifnull(refuse_reason, '') refuseReason, complete_status completeStatus");
		query.append(" from 0085_network_development" );
		query.append(" where courier_id = ? and  create_date is NOT null order by create_date desc");
		List<Map<String, Object>> list = findForJdbcParam(query.toString(), page, rows, courierId);
		if(CollectionUtils.isNotEmpty(list)){
				for(int i = 0; i<list.size(); i++){
					//如果没有填完信息，返回状态4
					if(!list.get(i).get("completeStatus").equals("1,2,3,4")){
						list.get(i).put("state", 4);
					}
					String createDate = list.get(i).get("createDate").toString();
					String dateDate = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.S").parseDateTime(createDate).toString("yyyy年MM月dd日 HH:mm:ss");
					list.get(i).put("createTime", dateDate);
				}
			}
		return list;
	}

	
	@Override
	public AjaxJson getNetworkDevDtl(Integer id) {
		logger.info("获取id为： " + id + "的网点详情");
		AjaxJson json = new AjaxJson();
		NetworkDevelopmentEntity entity = null;
		try {
			entity = this.get(NetworkDevelopmentEntity.class, id);
			if(entity == null){
				logger.warn("找不到id为： " + id + "对应的网点详情");
				json.setMsg("找不到该网点详情");
				json.setSuccess(false);
				json.setStateCode("02");
				return json;
			}
			NetworkDevelopDTO dto = new NetworkDevelopDTO();
			dto.setId(entity.getId());  //记录id
			dto.setCommunityName(entity.getCommunityName()==null ? "" : entity.getCommunityName()); //社区名称
			dto.setCourierId(entity.getCourierId());//快递员id
			dto.setHousehold(entity.getHousehold() == null ? 0 : entity.getHousehold().intValue());//住户数量
			dto.setHousePrice(entity.getHousePrice() == null ? 0 :entity.getHousePrice().doubleValue());//房价
			dto.setRent(entity.getRent() == null ? 0 : entity.getRent().doubleValue());//2房1厅租金
			dto.setLatitude(entity.getLatitude() == null ? "" : entity.getLatitude().toString());//纬度
			dto.setLongitude(entity.getLongitude() == null ? "" : entity.getLongitude().toString()); // 经度
			dto.setCommunityAddress(entity.getCommunityAddress() == null ? "" : entity.getCommunityAddress());

			dto.setShopTotalAmount(entity.getShopAmount() == null ? 0 : entity.getShopAmount());//门店总数量
			dto.setShopRent(entity.getShopRent() == null ? 0 : entity.getShopRent());//门店租金
			dto.setElmAmount(entity.getElmAmount() == null ? 0 : entity.getElmAmount().intValue()); //饿了么数量
			dto.setBaiduAmount(entity.getBaiduAmount() == null ? 0 : entity.getBaiduAmount().intValue()); //百度数量
			dto.setMeituanAmount(entity.getMeituanAmount() == null ? 0 : entity.getMeituanAmount().intValue());//美团数量
			dto.setKoubeiAmount(entity.getKoubeiAmount() == null ? 0 : entity.getKoubeiAmount().intValue());//口碑数量
			dto.setOtherAmount(entity.getOtherAmount() == null ? 0 : entity.getOtherAmount().intValue());//其它
			dto.setOrderAmount(entity.getOrderAmount() == null ? 0 : entity.getOrderAmount().intValue());//外卖日订单数量
			List<Map<String, Object>> categoryList = categoryService.getCategoryGroup("communityShop");
			
			//找出该网点所有店面照片
			StringBuilder query = new StringBuilder();
			query.append(" select photo_type photoType, url from 0085_network_photos where networkd_devid = ? order by photo_type,photo_id");
			List<Map<String, Object>> photoUrlList = findForJdbc(query.toString(), id);
			
			//找出不同类型对应门店数量
			query = new StringBuilder();
			query.append(" select shop_amount from 0085_shop_detail where network_devid = ? and type = ? ");
			
			List<Map<String, Object>> shopTypeList = new ArrayList<Map<String,Object>>();
			Map<String, Object> shopMap = new HashMap<String, Object>();
			List<String> urlList = new ArrayList<String>();
			Integer shop_amount = 0;
			String string = "";
			for(Map<String, Object> categoryMap : categoryList){
				for(Map<String, Object> photoTypeMap : photoUrlList){
					if(categoryMap.get("id").toString().equals(photoTypeMap.get("photoType").toString())){
						urlList.add(photoTypeMap.get("url").toString());
						if(string == ""){
							string = string + photoTypeMap.get("url").toString();
						}
						else{
							string = string + "," + photoTypeMap.get("url").toString();
						}
					}
				}
				shop_amount = findOneForJdbc(query.toString(), Integer.class, id, categoryMap.get("id"));
				shopMap.put("type", categoryMap.get("id"));
				shopMap.put("urlString", string);
				shopMap.put("typeName", categoryMap.get("name"));
				shopMap.put("shopAmount", shop_amount==null ? 0:shop_amount);
				shopMap.put("urls", urlList);
				shopTypeList.add(shopMap);
				shopMap = new HashMap<String, Object>();
				urlList = new ArrayList<String>();
				string = "";
			}
			
			dto.setShopList(shopTypeList);
			
//			List<ShopDtlDTO> rtlist = new ArrayList<ShopDtlDTO>();
//			String sql = "SELECT id,network_devid networkDevid,shop_amount shopAmount,set_state ,type FROM 0085_shop_detail t WHERE network_devid= ? AND type=?";
//			String photoSql = "SELECT photo_id,networkd_devid networkDevid,photo_type,url FROM 0085_network_photos t WHERE networkd_devid=? AND photo_type=? order by order asc ";
//			for (int i = 0; i < categoryList.size(); i++) {
//				List<ShopDetailEntity> dtl = this.findObjForJdbc(sql, ShopDetailEntity.class,id.toString(),categoryList.get(i).get("id").toString());
//				List<NetworkPhotosEntity> photos =  this.findObjForJdbc(photoSql, NetworkPhotosEntity.class,id.toString(),categoryList.get(i).get("id").toString());
//				ShopDtlDTO shopDto = new ShopDtlDTO();
//				String[] sb = new String[photos.size()];
//				for (int j = 0; j < photos.size(); j++) {
//					sb[j]=photos.get(j).getUrl();
//				}
//				shopDto.setUrls(sb);
//				shopDto.setType(Integer.parseInt(categoryList.get(i).get("id").toString()));
//				shopDto.setTypeName(categoryList.get(i).get("name").toString());
//				if (dtl != null && dtl.size() > 0) {
//					shopDto.setShopAmount(dtl.get(0).getShopAmount());
//					shopDto.setState("1");
//				}else {
//					shopDto.setState("0");
//				}
//				rtlist.add(shopDto);
//			}
//			Map<String, List<ShopDtlDTO>> rMap=new HashMap<String, List<ShopDtlDTO>>();
//			rMap.put("shopDtl", rtlist);
			json.setObj(dto);
//			json.setArrayAjaxJson(rMap);
			json.setSuccess(true);
			json.setStateCode("00");
			json.setMsg("获取网点开拓详情成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("获取网点开拓详情失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
	
	private String getCompleteStatus(NetworkDevelopmentEntity entity, String status){
		String completeStutas = entity.getCompleteStatus();
		String string = "";
		Set<String> set = new TreeSet<String>();
		if(!completeStutas.equals("0")){
			String[] str = completeStutas.split(",");
			CollectionUtils.addAll(set, str);
			set.add(status);
			string = StringUtils.join(set, ",");
		}
		return string;
	}

	private ShopDetailEntity getShopDetailEntityByNidAndType(Integer nid, Integer type){
//		StringBuilder queryBuilder = new StringBuilder();
//		queryBuilder.append(" select id, network_devid, shop_amount, type ");
//		queryBuilder.append(" from 0085_shop_detail");
//		queryBuilder.append(" where network_devid = ? and type = ?");
//		Map<String, Object> map = findOneForJdbc(queryBuilder.toString(),  nid, type);
//		ShopDetailEntity shopDetailEntity = new ShopDetailEntity();
//		if(map != null){
//			shopDetailEntity.setId(Integer.valueOf(map.get("id").toString()));
//			shopDetailEntity.setNetworkDevid(Integer.valueOf(map.get("network_devid").toString()));
//			shopDetailEntity.setShopAmount(Integer.valueOf(map.get("shop_amount").toString()));
//			shopDetailEntity.setType(Integer.valueOf(map.get("type").toString()));
//		}
//		return shopDetailEntity;
		CriteriaQuery cq = new CriteriaQuery(ShopDetailEntity.class);
		cq.eq("networkDevid", nid);
		cq.eq("type", type);
		cq.add();
		
		List<ShopDetailEntity> entities = getListByCriteriaQuery(cq, false);
		
		if(CollectionUtils.isEmpty(entities)){
			return null;
		}
		return entities.get(0);

	}
}
