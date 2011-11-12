package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ClinicDTO extends BaseModelData
{
	private static final long serialVersionUID = 855431885574040155L;
	private long id;
	private CityDTO city;

	public ClinicDTO()
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
		return get("clinicName");
	}

	public void setName(String name)
	{
		set("clinicName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public CityDTO getCity()
	{
		return city;
	}

	public void setCity(CityDTO city)
	{
		set("cityName", city);
		this.city = city;
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
		result = prime * result + ((city == null) ? 0 : city.hashCode());
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
		ClinicDTO other = (ClinicDTO) obj;
		if(!this.getName().equalsIgnoreCase(other.getName()))
			return false;
		return true;
	}

	
}
