package com.wm.util;

import java.util.List;

public class ListUtil {
	
	/**
	 * l!=Null  return true
	 * l==Null  return false
	 * l.size <0 return  false
	 * @param l
	 * @return
	 */
	public static Boolean isEmpty(List l){
		if(l==null)return false;
		return l.size()>0?true:false;
	}
	
	
}
