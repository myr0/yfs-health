package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ChapterNameDTO extends BaseModelData
{
	private static final long serialVersionUID = 6280337431842932388L;
	private long id;
	private CountryDTO country;
	private StateDTO state;
	private VillageDTO village;
	private TownDTO town;
	private CityDTO city;
	private LocalityDTO locality;

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
		return get("chapterName");
	}

	public void setName(String name)
	{
		set("chapterName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public CountryDTO getCountry()
	{
		return country;
	}

	public StateDTO getState()
	{
		return state;
	}

	public VillageDTO getVillage()
	{
		return village;
	}

	public TownDTO getTown()
	{
		return town;
	}

	public CityDTO getCity()
	{
		return city;
	}

	public LocalityDTO getLocality()
	{
		return locality;
	}

	public void setCountry(CountryDTO country)
	{
		this.country = country;
		set("countryName", country.getName());
	}

	public void setState(StateDTO state)
	{
		this.state = state;
		set("stateName", state.getName());
	}

	public void setVillage(VillageDTO village)
	{
		this.village = village;
		set("villageName", village.getName());
	}

	public void setTown(TownDTO town)
	{
		this.town = town;
		set("townName", town.getName());
	}

	public void setCity(CityDTO city)
	{
		this.city = city;
		set("cityName", city.getName());
	}

	public void setLocality(LocalityDTO locality)
	{
		this.locality = locality;
		set("localityName", locality.getName());
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 == null)
			return false;
		if (arg0.getClass() != this.getClass())
			return false;

		ChapterNameDTO chap = (ChapterNameDTO) arg0;
		if (chap.getName() == null)
			return false;
		return chap.getName().equalsIgnoreCase(this.getName());
	}

	@Override
	public String toString()
	{
		return getName();
	}

}
