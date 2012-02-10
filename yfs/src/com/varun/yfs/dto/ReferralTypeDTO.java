package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ReferralTypeDTO extends BaseModelData
{
	private static final long serialVersionUID = 5626142713395394597L;
	private long id;

	public ReferralTypeDTO()
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
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		ReferralTypeDTO refType = (ReferralTypeDTO) obj;

		if (this.getName().equalsIgnoreCase(refType.getName()))
			return true;

		return false;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
