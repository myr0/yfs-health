package com.varun.yfs.server.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "state")
public class State implements Serializable
{
	private static final long serialVersionUID = 6593825060763079909L;
	private long id;
	private String stateName;
	private String deleted;
	private Country country;
	private Set<City> cities;
	private Set<Town> towns;
	private Set<Village> villages;

	public State()
	{
		setDeleted("N");
	}

	public State(String name)
	{
		setName(name);
		setDeleted("N");
	}

	public State(String name, Country country)
	{
		setName(name);
		setCountry(country);
		setDeleted("N");
	}

	@Id
	@GeneratedValue
	@Column(name = "stateId")
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Column(nullable = false)
	public String getName()
	{
		return stateName;
	}

	public final void setName(String name)
	{
		this.stateName = name;
	}

	@Column(nullable = false)
	public final String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "countryId", nullable = false, updatable = true, insertable = true)
	@Fetch(FetchMode.SELECT)
	public Country getCountry()
	{
		return country;
	}

	public final void setCountry(Country country)
	{
		this.country = country;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "state")
	@Fetch(FetchMode.SELECT)
	public Set<City> getCities()
	{
		return cities;
	}

	public void setCities(Set<City> cities)
	{
		this.cities = cities;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "state")
	@Fetch(FetchMode.SELECT)
	public Set<Town> getTowns()
	{
		return towns;
	}

	public void setTowns(Set<Town> towns)
	{
		this.towns = towns;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "state")
	@Fetch(FetchMode.SELECT)
	public Set<Village> getVillages()
	{
		return villages;
	}

	public void setVillages(Set<Village> villages)
	{
		this.villages = villages;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		State other = (State) obj;
		if (!stateName.equalsIgnoreCase(other.stateName))
			return false;
		return true;
	}

}
