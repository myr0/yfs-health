package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userChapterPermissions")
public class UserChapterPermissions implements Serializable
{
	private static final long serialVersionUID = -420052322734423454L;

	@Id
	@GeneratedValue
	@Column(name = "userChapterPermId")
	private long id;

	@Column(nullable = false)
	private String chapterName;

	@Column(nullable = true)
	private String read;

	@Column(nullable = true)
	private String write;

	@Column(nullable = true)
	private String delete;

	public UserChapterPermissions()
	{
	}

	public UserChapterPermissions(String name, String pass)
	{
		setChapterName(name);
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
		return chapterName;
	}

	public String getChapterName()
	{
		return chapterName;
	}

	public final void setChapterName(String name)
	{
		this.chapterName = name;
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
		UserChapterPermissions other = (UserChapterPermissions) obj;
		if (!chapterName.equalsIgnoreCase(other.chapterName))
			return false;
		return true;
	}


}
