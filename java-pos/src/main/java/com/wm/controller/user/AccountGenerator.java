package com.wm.controller.user;

import org.apache.commons.lang.RandomStringUtils;

public class AccountGenerator {
	private static final String ALL_LETTER = "abcdefghijklmnopqrstuvwxyz";
	private static final String ALL_NUMBER = "0123456789";
	private static final int RANDOM_LETTER_NUM = 3;
	
	/**
	 * 获取用户账号，3位随机字母+时间戳+随机数
	 * @return
	 */
	public static String getAccount() {
		String account = RandomStringUtils.random(RANDOM_LETTER_NUM, ALL_LETTER);
		account += Long.toString(new java.util.Date().getTime()).substring(7) + RandomStringUtils.random(3, ALL_NUMBER);
		return account; 
	}
	
	public static String getRandomNum(int num){
		return RandomStringUtils.random(num, ALL_NUMBER);
	}
}
