package com.varun.yfs.dto;

import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class CityDTO extends BaseModelData
{
	private static final long serialVersionUID = 534279695566650743L;
	private long id;
	private StateDTO state;
	private Set<LocalityDTO> locality;

	public CityDTO()
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
		return get("cityName");
	}

	public void setName(String name)
	{
		set("cityName", name);
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
	
	public void setLocality(Set<LocalityDTO> locality)
	{
		this.locality = locality;
	}
	
	public Set<LocalityDTO> getLocality()
	{
		return locality;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}

	
}
