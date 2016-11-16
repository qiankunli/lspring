package org.lqk.lspring.util;

public class StringUtil {
	public static String lowerCaseFirstChar(String prop) {
		char ch = (char) (prop.charAt(0) + 32);
		return prop.replaceFirst(prop.charAt(0) + "", ch + "");
	}

	public static String biggerCaseFirstChar(String prop) {
		char ch = (char) (prop.charAt(0) - 32);
		return prop.replaceFirst(prop.charAt(0) + "", ch + "");
	}

	public static String setMethodName(String prop) {

		return "set" + biggerCaseFirstChar(prop);
	}
}
