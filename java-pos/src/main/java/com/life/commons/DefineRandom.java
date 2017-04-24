package com.life.commons;

import java.util.Random;

/**
 * @author chinahuangxc
 */
public class DefineRandom {

    /** 纯数字的随机基本数据 */
    public static final char[] NUMBERS = new char[10];
    /** 纯大写英文字符的随机基本数据 */
    public static final char[] UPPER_STRINGS = new char[26];
    /** 纯小写英文字符的随机基本数据 */
    public static final char[] LOWER_STRINGS = new char[26];
    /** 所有英文字符的随机基本数据 */
    public static final char[] STRINGS = new char[52];
    /** 数字、字符组合的随机基本数据 */
    public static final char[] CHARS = new char[62];

    /** 数字字符的长度 */
    public static final int NUMBERS_LENGTH = NUMBERS.length;
    /** 英文字符(纯小写或纯大写)的长度 */
    public static final int ENG_LENGTH = UPPER_STRINGS.length;
    /** 所有英文字符的长度 */
    public static final int STRINGS_LENGTH = STRINGS.length;
    /** 所有字符的长度 */
    public static final int CHARS_LENGTH = CHARS.length;

    static {
        for (int i = 0; i < 10; i++) {
            NUMBERS[i] = (char) (48 + i);
        }
        for (int i = 0; i < 26; i++) {
            UPPER_STRINGS[i] = (char) (65 + i);
            LOWER_STRINGS[i] = (char) (97 + i);
        }
        System.arraycopy(UPPER_STRINGS, 0, STRINGS, 0, ENG_LENGTH);
        System.arraycopy(LOWER_STRINGS, 0, STRINGS, ENG_LENGTH, ENG_LENGTH);
        System.arraycopy(NUMBERS, 0, CHARS, 0, NUMBERS_LENGTH);
        System.arraycopy(UPPER_STRINGS, 0, CHARS, NUMBERS_LENGTH, ENG_LENGTH);
        System.arraycopy(LOWER_STRINGS, 0, CHARS, NUMBERS_LENGTH + ENG_LENGTH, ENG_LENGTH);
    }

    public static String randomNumber(int length) {
        char[] returnStr = new char[length];
        for (int i = 0; i < length; i++) {
            returnStr[i] = NUMBERS[random(NUMBERS_LENGTH)];
        }
        return new String(returnStr);
    }

    public static String randomLowerString(int length) {
        char[] returnStr = new char[length];
        for (int i = 0; i < length; i++) {
            returnStr[i] = LOWER_STRINGS[random(ENG_LENGTH)];
        }
        return new String(returnStr);
    }

    public static String randomUpperString(int length) {
        char[] returnStr = new char[length];
        for (int i = 0; i < length; i++) {
            returnStr[i] = UPPER_STRINGS[random(ENG_LENGTH)];
        }
        return new String(returnStr);
    }

    public static String randomString(int length){
        char[] returnStr = new char[length];
        for (int i = 0; i < length; i++) {
            returnStr[i] = STRINGS[random(STRINGS_LENGTH)];
        }
        return new String(returnStr);

    }

    public static String randomChar(int length) {
        char[] returnStr = new char[length];
        for (int i = 0; i < length; i++) {
            returnStr[i] = CHARS[random(CHARS_LENGTH)];
        }
        return new String(returnStr);
    }

    private static final Random random = new Random();

    public static int random(int length) {
        return random.nextInt(length);
    }

    public static void main(String[] args) {
        System.out.println(new String(STRINGS));
        System.out.println(randomNumber(6));
        System.out.println(randomLowerString(6));
        System.out.println(randomUpperString(6));
        System.out.println(randomString(6));
        System.out.println(randomChar(6));
        
        System.out.println(randomUpperString(16));
        System.out.println(randomChar(32).toUpperCase());
        System.out.println(randomChar(16).toUpperCase());
        
        System.out.println(randomChar(32));
    }
}