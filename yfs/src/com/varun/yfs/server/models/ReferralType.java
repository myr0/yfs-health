package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ReferralType implements Serializable
{
	private static final long serialVersionUID = -1102055805351454738L;
	private long id;
	private String name;
	private String deleted;

	public ReferralType()
	{
		setDeleted("N");
	}

	public ReferralType(String name)
	{
		setName(name);
		setDeleted("N");
	}

	@Id
	@GeneratedValue
	@Column(name = "referralTypeId")
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

	public final void setName(String name)
	{
		this.name = name;
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
		ReferralType other = (ReferralType) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
