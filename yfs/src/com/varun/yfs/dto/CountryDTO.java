package com.varun.yfs.dto;

import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class CountryDTO extends BaseModelData
{
	private static final long serialVersionUID = 3471915163210103614L;
	private long id;
	private Set<StateDTO> states;

	public CountryDTO()
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
		return get("countryName");
	}

	public void setName(String name)
	{
		set("countryName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public Set<StateDTO> getStates()
	{
		return states;
	}

	public void setStates(Set<StateDTO> states)
	{
		this.states = states;
	}

	@Override
	public boolean equals(Object obj)
	{
		String countryName = null;
		if (obj == null)
			return false;

		return this.getName().equalsIgnoreCase(obj.toString());
	}

	@Override
	public String toString()
	{
		return getName();
	}

}
