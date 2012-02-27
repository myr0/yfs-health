package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserProjectPermissionsDTO extends BaseModelData
{
	private static final long serialVersionUID = -4689344436728499215L;

	private long id;

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
		return get("projectName");
	}

	public final void setProjectName(String name)
	{
		set("projectName", name);
	}

	public String getDeleted()
	{
		return get("projectName");
	}

	public final void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public void setRead(String read)
	{
		set("read", read);
	}

	public String getRead()
	{
		return get("read");
	}

	public void setWrite(String write)
	{
		set("write", write);
	}

	public String getWrite()
	{
		return get("write");
	}

	public void setDelete(String delete)
	{
		set("delete", delete);
	}

	public String getDelete()
	{
		return get("delete");
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		UserProjectPermissionsDTO user = (UserProjectPermissionsDTO) obj;
		return user.getProjectName().equalsIgnoreCase(this.getProjectName());
	}

}
