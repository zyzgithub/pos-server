package com.solr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeecg.system.service.SystemService;
import jodd.util.StringUtil;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solr.service.PartnerSolrServiceI;
import com.solr.util.PartnerSolrUtil;

@Service("partnerSolrService")
@Transactional
@SuppressWarnings(value={"rawtypes", "unused"})
public class PartnerSolrServiceImpl extends CommonServiceImpl implements PartnerSolrServiceI{
	
	private final static Logger logger = LoggerFactory.getLogger(PartnerSolrServiceImpl.class);

	@Autowired
	private SystemService systemService;
	
	@Value("${solr_url}")
	private String solrUrl;
	
	@Override
	public List queryMerchantList(String group_id, String pt,
			String sortType, String start, String rows,
			String searchKeyword) {
		
		HttpSolrServer server = new HttpSolrServer(solrUrl);
		SolrDocumentList docs = null;
		
		try {
			if(checkAndUpdateVersion()){
				buildIndex(solrUrl, true);//初始化数据	
			} 
			
			ModifiableSolrParams params = new ModifiableSolrParams(); 
			SolrQuery filterQuery = new SolrQuery();  
			
			filterQuery.add("fq", "{!geofilt}");
			if(StringUtil.isNotEmpty(group_id) && !"0".equals(group_id)){
				filterQuery.add("fq", "category_id:" + group_id);
			}

			if(StringUtil.isNotEmpty(searchKeyword)){
				filterQuery.add("fq", searchKeyword);
			}
			
			if(pt==null || "".equals(pt.trim())){
				params.set("d","999999999");//距离
				params.set("pt", "0,0");
			}else{
				params.set("d",systemService.getSystemConfigValue("distance"));//距离
				params.set("pt", pt);
			}
			params.set("q", "*:*");	
			
			
			params.set("sfield", "latlng");
			params.set("sort", sortType);
			params.set("fl", "*,distince:geodist()");
			params.set("start", start);
			params.set("rows", rows);
			params.set("wt", "json");
			
			params.add(filterQuery);
			
			QueryResponse resp = server.query(params, METHOD.POST);
			docs = resp.getResults();
			for(int i=0;i<docs.size();i++){
				logger.info(docs.get(i).toString());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docs;
	}
	
	public String getVersionNo(){
		return systemService.getSystemConfigValue("solr_version");
	}
	
	public boolean checkAndUpdateVersion(){
		boolean flag = true;
		String oldVersion = PartnerSolrUtil.getOldVersionNo();
		String newVersionNo = this.getVersionNo();
		if(oldVersion != null && newVersionNo.equals(oldVersion)){
			flag = false;
		}else{
			PartnerSolrUtil.updateVersion(newVersionNo);
		}
		return flag;
	}
	
	private void buildIndex(String solrServer, boolean delflag) throws Exception {
		List<Map<String,Object>> list = this.findForJdbc("select id,title,city_id,group_id,address,promotion," +
				"bidding_money,logo_url,operation_state,buy_count,comment_score,delivery_time,location,category_name from merchant_view");
		HttpSolrServer server = null;
		server = new HttpSolrServer(solrServer);

		if(delflag){
			server.deleteByQuery("*:*");	
		}
		
		int count = 0;

		int eachCommit = 100;
			//从数据库中获取酒店数据
		for(int i=0; i<list.size(); i ++){
			Map<String,Object> m =  list.get(i);
			
			Object mer_id = m.get("id");
			Object title = m.get("title");
			Object address = m.get("address");
			Object city_id = m.get("city_id");
			Object category_id = m.get("group_id");
			Object promotion = m.get("promotion");
			Object bidding_money = m.get("bidding_money");
			Object logo_url = m.get("logo_url");
			Object status = m.get("operation_state");
			Object buy_count = m.get("buy_count");
			Object comment_score = m.get("comment_score");
			Object delivery_time = m.get("delivery_time");
			Object latlng = m.get("location");
			Object category_name = m.get("category_name");
			
			count++;
			//添加酒店数据到Solr索引中
			SolrInputDocument doc = new SolrInputDocument();

			doc.addField("mer_id", mer_id);
			doc.addField("title", title);
			doc.addField("address", address);
			doc.addField("city_id", city_id);
			doc.addField("category_id", category_id);
			doc.addField("promotion", promotion);
			doc.addField("bidding_money", bidding_money);
			doc.addField("logo_url", logo_url);
			doc.addField("status", status);
			doc.addField("buy_count", buy_count);
			doc.addField("comment_score", comment_score);
			doc.addField("delivery_time", delivery_time);
			doc.addField("latlng", latlng);
			doc.addField("category_name", category_name);

			server.add(doc);
			//100条commit一次
			if (count % eachCommit == 0) {
				server.commit();
				count = 0;
			}
		}
		if (count > 0) {
			server.commit();
			count = 0;
		}
	}
	
	public List queryMyFavoritesMerchantList(String pt, int userid){		
		return queryMerchantListByMerIdList(this.queryFavoritesList(userid),pt,"0","1000",null);
	}
	
	private List queryMerchantListByMerIdList(List list,String pt,String start,String num,String cityid){
		HttpSolrServer server = new HttpSolrServer(solrUrl);
		SolrDocumentList docs = null;
		
		Map<String,String> params = new HashMap<String,String>(); 
		try {
	        if(list.size() != 0){
				if(checkAndUpdateVersion()){
					buildIndex(solrUrl, true);//初始化数据	
				} 
				
	        	String qStr = "";
	        	Map m = null;
	        	for(int i = 0; i < list.size(); i ++){
	        		m = (Map) list.get(i);
					if(i == 0){
						qStr += "mer_id:" + m.get("mer_id").toString();
					}else{
						qStr += " OR mer_id:" + m.get("mer_id").toString();
					}
				}		
				System.out.println(qStr);
	        	params.put("q", qStr);
	        	if(StringUtil.isNotEmpty(cityid))
	        		params.put("fq", "city_id:" + cityid);
				params.put("pt", pt);//当前经纬度
				params.put("sfield", "latlng");//经纬度的字段
				params.put("d", "999999999999999");
				params.put("sort", "geodist() asc");//根据距离排序
				params.put("fl", "*,distince:geodist()");
				params.put("start", start);//记录开始位置
				params.put("rows", num);//查询的行数
				params.put("wt", "json");//查询的行数
				QueryResponse resp = null;
				
				resp = server.query(new MapSolrParams(params), METHOD.POST);
	
				docs = resp.getResults();
				for(int i=0;i<docs.size();i++){
					System.out.println(docs.get(i));
				}
	        }
		} catch (SolrServerException e) {			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
       
		return docs;		
	}
	
	private List queryFavoritesList(int userid){
		List list = this.findForJdbc("select item_id mer_id from favorites where userid=? and type='2'", userid);
		return list;
	}

	private List queryTeamPartnerList(int userid){
		List list = this.findForJdbc("select partner_id mer_id from team_partner where team_id=?", userid);
		return list;
	}
	
	@Override	
	public void cleanUpSolrCache(){
		String sql="update system_config set `value`=UUID() where code='solr_version'";
		this.executeSql(sql);
	}
	
}
