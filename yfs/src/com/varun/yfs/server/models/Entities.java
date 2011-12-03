package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Entities implements Serializable
{
	private static final long serialVersionUID = -9205170388180671156L;

	@Id
	@GeneratedValue
	@Column(name = "ENTITY_ID")
	private long id;

	@Column(nullable = false)
	private String groupName;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String deleted;

	public Entities()
	{
		setDeleted("N");
	}

	public Entities(String name, String groupName)
	{
		setName(name);
		setGroupName(groupName);
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
		return name;
	}

	public final void setName(String name)
	{
		this.name = name;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public final void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getGroupName()
	{
		return groupName;
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
		Entities other = (Entities) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
