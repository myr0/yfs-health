package com.varun.yfs.client.index;

public enum ModelDataEnum
{
	City,
	Country,
	Locality,
	State, 
	Town,
	Village,
	ChapterName;

	public static boolean isLocationAdmin(String entityName)
	{
		for (ModelDataEnum entity : values())
		{
			if (entity.name().equalsIgnoreCase(entityName))
				return true;
		}
		return false;
	}

}
