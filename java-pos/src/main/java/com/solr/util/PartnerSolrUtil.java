package com.solr.util;

import java.util.Hashtable;

public class PartnerSolrUtil {

	private static Hashtable<String, String> versionTable = new Hashtable<String, String>();
	
	/**
	 * 初始化索引数据
	 * @param solrServer
	 * @throws Exception
	 */
	
	public static String getOldVersionNo(){
		String oldVersion = (String) versionTable.get("versionNo");
		return oldVersion;
	}
	
	public static void updateVersion(String newVersionNo){
		versionTable.put("versionNo", newVersionNo);
		System.out.println("newVersion:" + newVersionNo);
	}

}
