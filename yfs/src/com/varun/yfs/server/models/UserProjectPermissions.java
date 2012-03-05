package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserProjectPermissions")
public class UserProjectPermissions implements Serializable
{
	private static final long serialVersionUID = -2814894310932414503L;

	@Id
	@GeneratedValue
	@Column(name = "userProjectPermId")
	private long id;

	@Column(nullable = false)
	private String projectName;

	@Column(nullable = true)
	private String read1;

	@Column(nullable = true)
	private String write1;

	@Column(nullable = true)
	private String deleted;

	public UserProjectPermissions()
	{
	}

	public UserProjectPermissions(String name, String pass)
	{
		setProjectName(name);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public final void setProjectName(String name)
	{
		this.projectName = name;
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
		UserProjectPermissions other = (UserProjectPermissions) obj;
		if (!projectName.equalsIgnoreCase(other.projectName))
			return false;
		return true;
	}
}
