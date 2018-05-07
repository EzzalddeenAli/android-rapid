package com.lh.rapid.util;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类。
 */
@SuppressLint("SimpleDateFormat")
public abstract class StrUtils {

	public static final int INDEX_NOT_FOUND = -1;

	private StrUtils() {
	}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 *
	 * @param value
	 *            待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isEmpty(str);
	}


	public static String replaceWhiteSpace(String str) {
		return str.replaceAll("\n", "");
	}

	public static String trimAllWhitespace(String str) {
		return str.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "").toString();
	}

	public static String deleteWhitespace(String str) { // from apache
		if (isEmpty(str)) {
			return str;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		return new String(chs, 0, count);
	}

	public static String removeEnd(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.endsWith(remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * 验证账号的合法性(邮箱和手机号码)
	 *
	 * @param str
	 * @return
	 */
	public static boolean checkAccount(String str) {
		if (checkEmail(str)) {
			return true;
		} else
			return checkMobile(str);
	}

	/**
	 * 验证email的合法性
	 *
	 * @param emailStr
	 * @return
	 */
	public static boolean checkEmail(String emailStr) {
		String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";// "^([a-z0-9A-Z]+[-|._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(emailStr.trim());
		return matcher.matches();
	}

	/**
	 * 验证手机号的合法性
	 *
	 * @param str
	 * @return
	 */
	public static boolean checkMobile(String str) {
		Pattern p = Pattern.compile("1[34578][0-9]{9}");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 验证password的合法性(必须是6-14数字或字母及组合)
	 *
	 * @param str
	 * @return
	 */
	public static boolean checkPassword(String str) {
		Pattern p = Pattern.compile("^[\\w+$]{6,14}+$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 验证动态验证码str的合法性（必须是6个数字组成）
	 */
	public static boolean checkIdentify(String str) {
		Pattern p = Pattern.compile("\\d{6}");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 验证邮编的合法性
	 */
	public static boolean checkPostCode(String str) {
		Pattern p = Pattern.compile("\\d{6}");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 验证身份证的合法性
	 */
	public static boolean checkIDCard(String str){
		Pattern p1 = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
		Matcher m1 = p1.matcher(str);
		Pattern p2 = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
		Matcher m2 = p2.matcher(str);
		return (m1.matches() || m2.matches());
	}

	/**
	 * 将14986这样的字符串变为1.4w
	 */
	public static String changeNum(String num) {
		if (isEmpty(num))
			return "";
		if (num.equals("0"))
			return num;
		num = num.replaceAll("^(0+)", "");
		int length = num.length();
		if (length < 4)
			return num;
		else if (length == 4)
			return num.charAt(0) + "." + num.charAt(1) + "K";
		else if (length == 5) {
			return num.charAt(0) + "." + num.charAt(1) + "W";
		} else {
			String numOfW = num.substring(0, length - 4);
			return numOfW + "W";
		}
	}

	/**
	 * 将15812345678这样的手机号码改为 158****5678
	 */
	public static String changePhone(String num) {
		if (!checkMobile(num))
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num.length(); i++) {
			if (i >= 3 && i <= 6)
				sb.append("*");
			else
				sb.append(num.charAt(i));
		}
		return sb.toString();
	}

	public static boolean isValidUrl(String url) {
		if (isNotBlank(url) && (url.startsWith("http://") || url.startsWith("https://")))
			return true;
		return false;
	}

	public static String stripEnd(String str, String stripChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return str;
		}
		if (stripChars == null) {
			while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != INDEX_NOT_FOUND)) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	public static boolean contains(String str, char searchChar) {
		if (isEmpty(str)) {
			return false;
		}
		return str.indexOf(searchChar) >= 0;
	}

	public static boolean contains(String str, String searchStr) {
		if ((str == null) || (searchStr == null)) {
			return false;
		}
		return str.indexOf(searchStr) >= 0;
	}

	public static boolean containsIgnoreCase(String str, String searchStr) {
		if ((str == null) || (searchStr == null)) {
			return false;
		}
		int len = searchStr.length();
		int max = str.length() - len;
		for (int i = 0; i <= max; i++) {
			if (str.regionMatches(true, i, searchStr, 0, len)) {
				return true;
			}
		}
		return false;
	}

	public static String substring(String s, int len) {
		if (isNotBlank(s)) {
			if (s.length() < len)
				return s;
			return TextUtils.substring(s, 0, len);
		}
		return s;
	}

	/**
	 * 去掉字符串多余的换行
	 */
	public static String trimBlankLine(String s) {
		if (isNotBlank(s)) {
			return s.trim().replaceAll("(\\s*?\\r?\\n\\s*?)+", "\n").trim();
		}
		return s;
	}

	public static String replaceLast(String string, String from, String to) {
		int lastIndex = string.lastIndexOf(from);
		if (lastIndex < 0)
			return string;
		String tail = string.substring(lastIndex).replaceFirst(from, to);
		return string.substring(0, lastIndex) + tail;
	}

	/**
	 * 将图片url地址最后部分的中文字符转义为UTF-8字符
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String urlEncode(String url, String charset){
		int lastSprit = url.lastIndexOf('/');
		String urlPre = url.substring(0, lastSprit + 1);
		String urlSuf = url.substring(lastSprit+1);
		urlSuf = Uri.encode(urlSuf, charset);
		return urlPre + urlSuf;
	}
}
