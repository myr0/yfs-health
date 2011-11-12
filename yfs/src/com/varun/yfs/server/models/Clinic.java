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
@Table(name = "clinic")
public class Clinic implements Serializable
{
	private static final long serialVersionUID = 8467271735823152112L;
	@Id
	@GeneratedValue
	@Column(name = "clinicId")
	private long id;

	@Column(nullable = false)
	private String clinicName;

	@Column(nullable = false)
	private String deleted;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "cityId", nullable = false, updatable = true, insertable = true)
	@Fetch(FetchMode.SELECT)
	private City city;

	public Clinic()
	{
		setDeleted("N");
	}

	public Clinic(String name)
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
		return clinicName;
	}

	public void setName(String name)
	{
		this.clinicName = name;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public void setDeleted(String deleted)
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
		Clinic other = (Clinic) obj;
		if (!clinicName.equalsIgnoreCase(other.clinicName))
			return false;
		return true;
	}

}
