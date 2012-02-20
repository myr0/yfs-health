package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserReportPermissions")
public class UserReportPermissions implements Serializable
{

	private static final long serialVersionUID = -7523287168327552826L;

	@Id
	@GeneratedValue
	@Column(name = "userReportPermissionsId")
	private long id;

	@Column(nullable = false)
	private String reportName;

	@Column(nullable = true)
	private String read;

	@Column(nullable = true)
	private String write;

	@Column(nullable = true)
	private String delete;

	public UserReportPermissions()
	{
	}

	public UserReportPermissions(String name, String pass)
	{
		setReportName(name);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getReportName()
	{
		return reportName;
	}

	public final void setReportName(String name)
	{
		this.reportName = name;
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
		UserReportPermissions other = (UserReportPermissions) obj;
		if (!reportName.equalsIgnoreCase(other.reportName))
			return false;
		return true;
	}
}
