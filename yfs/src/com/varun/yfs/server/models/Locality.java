package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "Locality")
public class Locality implements Serializable
{
	private static final long serialVersionUID = 8467271735823152112L;
	@Id
	@GeneratedValue
	@Column(name = "localityId")
	private long id;

	@Column(nullable = false)
	private String localityName;

	@Column(nullable = false)
	private String deleted;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "cityId", nullable = false, updatable = true, insertable = true)
	@Fetch(FetchMode.SELECT)
	private City city;

	public Locality()
	{
		setDeleted("N");
	}

	public Locality(String name)
	{
		setName(name);
		setDeleted("N");
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
		return localityName;
	}

	public final void setName(String name)
	{
		this.localityName = name;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public City getCity()
	{
		return city;
	}

	public void setCity(City city)
	{
		this.city = city;
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
		Locality other = (Locality) obj;
		if (!localityName.equalsIgnoreCase(other.localityName))
			return false;
		return true;
	}

}
