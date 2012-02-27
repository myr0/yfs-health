package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserChapterPermissionsDTO extends BaseModelData
{

	private static final long serialVersionUID = -913827435230713476L;

	private long id;

	public UserChapterPermissionsDTO()
	{
		setDeleted("N");
	}

	public UserChapterPermissionsDTO(String name, String pass)
	{
		setChapterName(name);
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

	public String getChapterName()
	{
		return get("chapterName");
	}

	public final void setChapterName(String name)
	{
		set("chapterName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
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
		UserChapterPermissionsDTO user = (UserChapterPermissionsDTO) obj;
		return user.getChapterName().equalsIgnoreCase(this.getChapterName());
	}

}
