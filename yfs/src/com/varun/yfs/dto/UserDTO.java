package com.varun.yfs.dto;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserDTO extends BaseModelData
{
	private static final long serialVersionUID = 3196402615838002153L;
	private long id;
	private List<ChapterNameDTO> chapterNames;
	private List<ProjectDTO> projects;

	public UserDTO()
	{
		set("deleted", "N");
	}

	public UserDTO(String name, String password)
	{
		setName(name);
		setPassword(password);
		setLoggedIn(false);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		set("id", id);
		this.id = id;
	}

	public String getName()
	{
		return get("name");
	}

	public final void setName(String name)
	{
		set("name", name);
	}

	public String getPassword()
	{
		return get("password");
	}

	public final void setPassword(String password)
	{
		set("password", password);
	}

	public void setChapterNames(List<ChapterNameDTO> chapterNames)
	{
		set("chapterNames", chapterNames);
		this.chapterNames = chapterNames;
	}

	public List<ChapterNameDTO> getChapterNames()
	{
		return this.chapterNames;
	}

	public void setProjects(List<ProjectDTO> villages)
	{
		set("projects", villages);
		this.projects = villages;
	}

	public List<ProjectDTO> getProjects()
	{
		return this.projects;
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public final void setLoggedIn(boolean loggedIn)
	{
		set("loggedIn", loggedIn);
	}

	public boolean getLoggedIn()
	{
		return get("loggedIn");
	}

	public void setSessionId(String id2)
	{
		set("sessionId", id2);
	}

	public String getSessionId()
	{
		return get("sessionId");
	}

	public void setRole(String role)
	{
		set("role", role);
	}

	public String getRole()
	{
		return get("role");
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		UserDTO user = (UserDTO) obj;
		return user.getName().equalsIgnoreCase(this.getName());
	}
}
