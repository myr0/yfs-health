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
@Table(name = "city")
public class City implements Serializable
{
	private static final long serialVersionUID = -1324852594905448485L;
	private long id;
	private String cityName;
	private String deleted;
	private State stateName;
	private Set<Locality> locality;

	public City()
	{
		setDeleted("N");
	}

	public City(String name)
	{
		setName(name);
		setDeleted("N");
	}

	@Id
	@GeneratedValue
	@Column(name = "cityId")
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
		return cityName;
	}

	public final void setName(String name)
	{
		this.cityName = name;
	}

	@Column(nullable = false)
	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "stateId", nullable = false, updatable = true, insertable = true)
	@Fetch(FetchMode.SELECT)
	public State getState()
	{
		return stateName;
	}

	public void setState(State state)
	{
		this.stateName = state;
	}

	public void setLocality(Set<Locality> locality)
	{
		this.locality = locality;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "city")
	public Set<Locality> getLocality()
	{
		return locality;
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
		City other = (City) obj;
		if (!cityName.equalsIgnoreCase(other.cityName))
			return false;
		return true;
	}

}
