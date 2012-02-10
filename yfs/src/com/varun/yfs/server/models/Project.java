package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Entity
public class Project implements Serializable
{
	private static final long serialVersionUID = -6293347565863506025L;

	@Id
	@GeneratedValue
	@Column(name = "projectId")
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String deleted;

	@ManyToOne(cascade = { CascadeType.ALL })
	// @JoinColumn(name = "chapterNameId", nullable = true, updatable = true,
	// insertable = true)
	@JoinTable(name = "Project_Chapter", joinColumns = @JoinColumn(name = "projectId"),
			inverseJoinColumns = @JoinColumn(name = "chapterNameId"))
	private ChapterName chapterName;

	public Project()
	{
		setDeleted("N");
	}

	public Project(String name)
	{
		setName(name);
		setDeleted("N");
	}

	public Project(String string, ChapterName chapterName2)
	{
		this(string);
		this.chapterName = chapterName2;
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

	public void setChapterName(ChapterName chapterName)
	{
		this.chapterName = chapterName;
	}

	public ChapterName getChapterName()
	{
		return chapterName;
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
		Project other = (Project) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
