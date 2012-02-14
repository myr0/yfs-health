package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userClinicPermissions")
public class UserClinicPermissions implements Serializable
{
	private static final long serialVersionUID = 1069769060104254078L;

	@Id
	@GeneratedValue
	@Column(name = "userClinicPermissionsId")
	private long id;

	@Column(nullable = false)
	private String clinicName;

	@Column(nullable = true)
	private String read;

	@Column(nullable = true)
	private String write;

	@Column(nullable = true)
	private String delete;

	public UserClinicPermissions()
	{
	}

	public UserClinicPermissions(String name, String pass)
	{
		setClinicName(name);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getClinicName()
	{
		return clinicName;
	}

	public final void setClinicName(String name)
	{
		this.clinicName = name;
	}

	public void setRead(String read)
	{
		this.read = read;
	}

	public String getRead()
	{
		return read;
	}

	public void setWrite(String write)
	{
		this.write = write;
	}

	public String getWrite()
	{
		return write;
	}

	public void setDelete(String delete)
	{
		this.delete = delete;
	}

	public String getDelete()
	{
		return delete;
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
		UserClinicPermissions other = (UserClinicPermissions) obj;
		if (!clinicName.equalsIgnoreCase(other.clinicName))
			return false;
		return true;
	}
}
