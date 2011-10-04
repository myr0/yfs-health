package com.varun.yfs.dto;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UsersDTO extends BaseModelData
{
	private static final long serialVersionUID = 3196402615838002153L;
	private long id;
	private List<LocalityDTO> localities;
	private List<VillageDTO> villages;
	private List<TownDTO> towns;

	public UsersDTO()
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

	public String getPassword()
	{
		return get("password");
	}

	public void setPassword(String password)
	{
		set("password", password);
	}

	public void setLocalities(List<LocalityDTO> localities)
	{
		set("localities", localities);
		this.localities = localities;
	}

	public List<LocalityDTO> getLocalities()
	{
		return this.localities;
	}

	public void setVillages(List<VillageDTO> villages)
	{
		set("villages", villages);
		this.villages = villages;
	}

	public List<VillageDTO> getVillages()
	{
		return this.villages;
	}

	public void setTowns(List<TownDTO> towns)
	{
		set("towns", towns);
		this.towns = towns;
	}

	public List<TownDTO> getTowns()
	{
		return this.towns;
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
