package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PermissionsDTO extends BaseModelData
{
	private static final long serialVersionUID = 3196402615838002153L;
	private long id;

	public PermissionsDTO()
	{
		set("deleted", "N");
	}

	public PermissionsDTO(String read, String write, String delete)
	{
		this();
		setRead(read);
		setWrite(write);
		setDelete(delete);
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
		return get("name");
	}

	public void setName(String name)
	{
		set("name", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
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
}
