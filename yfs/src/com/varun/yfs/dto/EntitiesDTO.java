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

	// @Override
	// public boolean equals(Object obj) {
	// return this.getName().equalsIgnoreCase(((EntitiesDTO) obj).getName());
	// }

}
