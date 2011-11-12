package com.varun.yfs.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "users")
public class User implements Serializable
{
	private static final long serialVersionUID = 3137505216329922435L;

	@Id
	@GeneratedValue
	@Column(name = "userId")
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = true)
	private String role;

	// @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
	// CascadeType.MERGE })
	// @JoinColumn(name = "localityId", nullable = true, updatable = true,
	// insertable = true)
	// @Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "User_ChapterName", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "chapterNameId") })
	@Column(nullable = true)
	@Fetch(FetchMode.SELECT)
	private List<ChapterName> chapterNames;

	// @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
	// CascadeType.MERGE })
	// @JoinColumn(name = "villageId", nullable = true, updatable = true,
	// insertable = true)
	// @Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "User_Project", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "projectId") })
	@Column(nullable = true)
	@Fetch(FetchMode.SELECT)
	private List<Project> projects;

	// @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
	// CascadeType.MERGE })
	// @JoinColumn(name = "townId", nullable = true, updatable = true,
	// insertable = true)
	// @Fetch(FetchMode.SELECT)
	// @ManyToMany(cascade = CascadeType.ALL)
	// @JoinTable(name = "User_Town", joinColumns = { @JoinColumn(name =
	// "userId") }, inverseJoinColumns = { @JoinColumn(name = "townId") })
	// @Column(nullable = true)
	// @Fetch(FetchMode.SELECT)
	// private List<Town> towns ;

	@Column(nullable = false)
	private String deleted;

	public User()
	{
		setDeleted("N");
	}

	public User(String name, String pass)
	{
		setName(name);
		setPassword(pass);
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

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

	public void setChapterNames(List<ChapterName> chapterName)
	{
		this.chapterNames = chapterName;
	}

	public List<ChapterName> getChapterNames()
	{
		return chapterNames;
	}

	public void setProjects(List<Project> project)
	{
		this.projects = project;
	}

	public List<Project> getProjects()
	{
		return projects;
	}

	// public void setTowns(List<Town> towns)
	// {
	// this.towns = towns;
	// }
	//
	// public List<Town> getTowns()
	// {
	// return towns;
	// }

	public void setRole(String role)
	{
		this.role = role;
	}

	public String getRole()
	{
		return role;
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
		User other = (User) obj;
		if (!name.equalsIgnoreCase(other.name))
			return false;
		return true;
	}

}
