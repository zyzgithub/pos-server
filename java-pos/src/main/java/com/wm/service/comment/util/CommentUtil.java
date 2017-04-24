package com.wm.service.comment.util;

import java.util.Random;

import org.apache.commons.lang.StringUtils;

public class CommentUtil {

	private static String merchantCommentDef = "挺不错的,很好,nice,平靓正,味道不错,好评,非常好5分,物美价廉,性价比高";
	private static String courierCommentDef = "服务很满意，好想一直让你送哦,看你好辛苦啊，真想一起和你享受这份餐,速度快得我受不了,这个点，又可以看到你了，这是我一直点餐的原因,你的影子，是我吃外卖的动力！,没有见过那么帅的，好开心！,希望明天还会看到你！,每次看到外卖我就想到你！,是你让我知道什么叫做好服务！,那位帅哥，希望每天都是你送！";
	private static Random random = new Random();
	private static String dotSepa = ",";

	public static String getRandomMerchantComment() {
		String[] merArr = StringUtils.split(merchantCommentDef, dotSepa);
		return merArr[random.nextInt(merArr.length)];
	}

	public static String getRandomCourierComment() {
		String[] courierArr = StringUtils.split(courierCommentDef, dotSepa);
		return courierArr[random.nextInt(courierArr.length)];
	}
	

	 /**
	  * 
	  * @param type //评价对象：0-快递员，1-商家，2-口味，3-速度，4-服务
	  * @return 好评
	  */
	public static String getRandomCommentByType(int type) {
		if(type == 0){
			return getRandomCourierComment();
		}
		if(type == 1){
			return getRandomMerchantComment();
		}
		return "好评";
	}
	
	
}
