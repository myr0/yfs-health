package com.varun.yfs.dto;

import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class StateDTO extends BaseModelData
{
	private static final long serialVersionUID = -7162436929563087934L;
	private long id;
	private CountryDTO country;
	private Set<CityDTO> cities;
	private Set<TownDTO> towns;
	private Set<VillageDTO> villages;

	public StateDTO()
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
		return get("stateName");
	}

	public void setName(String name)
	{
		set("stateName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public void setCountry(CountryDTO country)
	{
		set("countryName", country);
		this.country = country;
	}

	public CountryDTO getCountry()
	{
		return this.country;
	}

	public Set<CityDTO> getCities()
	{
		return cities;
	}

	public void setCities(Set<CityDTO> cities)
	{
		this.cities = cities;
	}

	public Set<TownDTO> getTowns()
	{
		return towns;
	}

	public void setTowns(Set<TownDTO> towns)
	{
		this.towns = towns;
	}

	public Set<VillageDTO> getVillages()
	{
		return villages;
	}

	public void setVillages(Set<VillageDTO> villages)
	{
		this.villages = villages;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;

		StateDTO refType = (StateDTO) obj;

		if (this.getName().equalsIgnoreCase(refType.getName()))
			return true;

		return false;
	}
}
