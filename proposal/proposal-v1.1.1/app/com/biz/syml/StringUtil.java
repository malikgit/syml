package com.biz.syml;

import java.util.Arrays;
import java.util.List;


public class StringUtil {

	public static final boolean isNullOrEmpty(String s) {
		if (s == null) return true;
		return s.isEmpty();
	}


	public static final boolean allIsNotNullAndNotEmpty(String...strings) {
		for (final String s : strings) {
			if (isNullOrEmpty(s)) return false;
		}
		return true;
	}


	/**
	 * For example, if you have string like this:<br/>
	 * <code>List&lt;String&gt; list = Arrays.asList("aaa", "bbb", "ccc");</code></br>
	 * invoking this method like this:<br/>
	 * <code>listToCommaSeparatedValue(Arrays.asList("aaa", "bbb", "ccc"), " - ")</code><br/>
	 * Would produce this string:<br/>
	 * <code>aaa - bbb - ccc</code>.<br/>
	 * Note that default @param separator value is " *** ". 
	 */
	public static final String listToCommaSeparatedString(List<String> list, String separator) {
		StringBuilder builder = new StringBuilder();
		for (String s : list) {
			builder.append(s).append(isNullOrEmpty(separator) ? " *** " : separator);
		}
		return builder.toString();
	}


	public static String concat(String[] strings, Integer...indexToExclude) {
		StringBuilder builder = new StringBuilder();
		final List<String> listStrings = Arrays.asList(strings);
		final List<Integer> listIntegers = Arrays.asList(indexToExclude);
		for (final String s : strings) {
			final Integer stringIndex = listStrings.indexOf(s);
			if (listIntegers.contains(stringIndex)) continue;
			builder.append(s).append(" ");
		}
		return builder.toString();
	}


	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}
}
