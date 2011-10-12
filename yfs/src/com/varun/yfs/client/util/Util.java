package com.varun.yfs.client.util;

public class Util
{

	private static final String SPACE = " ";
	public static final String EMPTY = "";

	public static String stripSpace(String entityName)
	{
		return entityName.replaceAll(SPACE, EMPTY);
	}

	public static String safeToString(Object obj)
	{
		if (obj == null)
			return EMPTY;
		else
			return obj.toString();
	}

	public static boolean isEmpty(String str)
	{
		if (str == null)
			return true;
		return str.trim().equalsIgnoreCase(EMPTY);
	}

	public static String firstCharLower(String value)
	{
		char[] values = value.toCharArray();
		values[0] = Character.toLowerCase(value.charAt(0));

		return new String(values);
	}
}
