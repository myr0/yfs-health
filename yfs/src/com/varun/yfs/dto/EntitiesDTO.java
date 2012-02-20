package com.varun.yfs.dto;

public class EntitiesDTO extends com.extjs.gxt.ui.client.data.BaseModelData
{

	private static final long serialVersionUID = 5364045369931557276L;
	private long id;

	public EntitiesDTO()
	{
		set("deleted", "N");
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return get("name");
	}

	public void setName(String name)
	{
		set("name", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public void setGroupName(String groupName)
	{
		set("groupName", groupName);
	}

	public String getGroupName()
	{
		return get("groupName");
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (get("name") == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		return getName().equalsIgnoreCase(((EntitiesDTO) obj).getName());
	}

}
