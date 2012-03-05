package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserEntityPermissions")
public class UserEntityPermissions implements Serializable
{

	private static final long serialVersionUID = -5724624514854329005L;

	@Id
	@GeneratedValue
	@Column(name = "userEntityPermissionsId")
	private long id;

	@Column(nullable = false)
	private String entityName;

	@Column(nullable = true)
	private String read1;

	@Column(nullable = true)
	private String write1;

	@Column(nullable = true)
	private String deleted;

	public UserEntityPermissions()
	{
	}

	public UserEntityPermissions(String name, String pass)
	{
		setEntityName(name);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public final void setEntityName(String name)
	{
		this.entityName = name;
	}

	public void setRead(String read)
	{
		this.read1 = read;
	}

	public String getRead()
	{
		return read1;
	}

	public void setWrite(String write)
	{
		this.write1 = write;
	}

	public String getWrite()
	{
		return write1;
	}

	public void setDelete(String delete)
	{
		this.deleted = delete;
	}

	public String getDelete()
	{
		return deleted;
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
		UserEntityPermissions other = (UserEntityPermissions) obj;
		if (!entityName.equalsIgnoreCase(other.entityName))
			return false;
		return true;
	}
}
