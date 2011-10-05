package com.varun.yfs.client.util;

public class Util
{

	private static final String SPACE = " ";
	private static final String EMPTY = "";

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
}
