package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserProjectPermissionsDTO extends BaseModelData
{
	private long id;

	private String projectName;

	private String deleted;

	private String read;

	private String write;

	private String delete;

	public UserProjectPermissionsDTO()
	{
		setDeleted("N");
	}

	public UserProjectPermissionsDTO(String name, String pass)
	{
		setProjectName(name);
		setDeleted("N");
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

	public String getProjectName()
	{
		return projectName;
	}

	public final void setProjectName(String name)
	{
		set("projectName", name);
		this.projectName = name;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		set("deleted", deleted);
		this.deleted = deleted;
	}

	public void setRead(String read)
	{
		set("read", read);
		this.read = read;
	}

	public String getRead()
	{
		return read;
	}

	public void setWrite(String write)
	{
		set("write", write);
		this.write = write;
	}

	public String getWrite()
	{
		return write;
	}

	public void setDelete(String delete)
	{
		set("delete", delete);
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
		if (projectName == null)
			return false;

		UserProjectPermissionsDTO other = (UserProjectPermissionsDTO) obj;
		if (!projectName.equalsIgnoreCase(other.projectName))
			return false;
		return true;
	}

}
