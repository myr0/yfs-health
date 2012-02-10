package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class VillageDTO extends BaseModelData
{
	private static final long serialVersionUID = -4630853241868593988L;
	private long id;
	private StateDTO state;

	public VillageDTO()
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
		return get("villageName");
	}

	public void setName(String name)
	{
		set("villageName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public StateDTO getState()
	{
		return state;
	}

	public void setState(StateDTO state)
	{
		set("stateName", state);
		this.state = state;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
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
		VillageDTO other = (VillageDTO) obj;
		if (!this.getName().equalsIgnoreCase(other.getName()))
			return false;
		return true;
	}

}
