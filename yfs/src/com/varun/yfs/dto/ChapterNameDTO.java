package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ChapterNameDTO extends BaseModelData
{
	private static final long serialVersionUID = 6280337431842932388L;
	private long id;

	public ChapterNameDTO()
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
}
