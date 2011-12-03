package com.varun.yfs.server.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "country")
public class Country implements Serializable
{
	private static final long serialVersionUID = -7972100793996969092L;
	private long id;
	private String countryName;
	private String deleted;
	private Set<State> states = new HashSet<State>();

	public Country()
	{
		setDeleted("N");
	}

	public Country(String name)
	{
		setName(name);
		setDeleted("N");
	}

	@Id
	@GeneratedValue
	@Column(name = "countryId")
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
		return countryName;
	}

	public final void setName(String name)
	{
		this.countryName = name;
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

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "country")
	@Fetch(FetchMode.SELECT)
	public Set<State> getStates()
	{
		return states;
	}

	public void setStates(Set<State> states)
	{
		this.states = states;
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

		Country other = (Country) obj;
		if (!countryName.equalsIgnoreCase(other.countryName))
			return false;
		return true;
	}
}
