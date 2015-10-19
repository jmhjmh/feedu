package com.feedu.main;

public class StringUtil {
	/**
	 * 判断是否是value
	 * 
	 * @param valueToken
	 * @param value
	 *            开始字符串
	 * @return
	 */
	public static boolean isValue(String valueToken, String value) {
		if (valueToken.indexOf(value) != -1) {
			return true;
		}
		return false;
	}

	/**
	 * 提取value值
	 * 
	 * @param valueToken
	 *            传入
	 * @param startString
	 *            开始
	 * @param unStart
	 *            跳过字数
	 * @return
	 */
	public static String getValue(String valueToken, String startString,
			String endString, int unStart) {
		int start = valueToken.indexOf(startString);
		int end = valueToken.length();
		String tempStr = valueToken.substring(start + unStart, end);
		end = tempStr.indexOf(endString, unStart);
		if (end == -1) {
			end = tempStr.length();
		}
		return tempStr.substring(0, end);
	}

}
