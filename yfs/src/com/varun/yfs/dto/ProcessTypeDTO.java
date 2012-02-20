package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ProcessTypeDTO extends BaseModelData
{
	private static final long serialVersionUID = 5626142713395394597L;
	private long id;

	public ProcessTypeDTO()
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
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessTypeDTO other = (ProcessTypeDTO) obj;
		if (!this.getName().equalsIgnoreCase(other.getName()))
			return false;
		return true;
	}
}
