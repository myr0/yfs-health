package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ChapterName implements Serializable
{
	private static final long serialVersionUID = -6293347565863506025L;
	private long id;
	private String name;
	private String deleted;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "countryId", nullable = true, updatable = true, insertable = true)
	private Country country;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "stateId", nullable = true, updatable = true, insertable = true)
	private State state;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "villageId", nullable = true, updatable = true, insertable = true)
	private Village village;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "townId", nullable = true, updatable = true, insertable = true)
	private Town town;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "cityId", nullable = true, updatable = true, insertable = true)
	private City city;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "localityId", nullable = true, updatable = true, insertable = true)
	private Locality locality;

	public ChapterName()
	{
		setDeleted("N");
	}

	public ChapterName(String name)
	{
		setName(name);
		setDeleted("N");
	}

	@Id
	@GeneratedValue
	@Column(name = "chapterNameId")
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
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(nullable = false)
	public String getDeleted()
	{
		return deleted;
	}

	public void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public void setCountry(Country country)
	{
		this.country = country;
	}

	public Country getCountry()
	{
		return country;
	}

	public State getState()
	{
		return state;
	}

	public Village getVillage()
	{
		return village;
	}

	public Town getTown()
	{
		return town;
	}

	public City getCity()
	{
		return city;
	}

	public Locality getLocality()
	{
		return locality;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public void setVillage(Village village)
	{
		this.village = village;
	}

	public void setTown(Town town)
	{
		this.town = town;
	}

	public void setCity(City city)
	{
		this.city = city;
	}

	public void setLocality(Locality locality)
	{
		this.locality = locality;
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
		ChapterName other = (ChapterName) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
