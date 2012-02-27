package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserReportPermissionsDTO extends BaseModelData
{
	private static final long serialVersionUID = -4689344436728499215L;

	private long id;

	public UserReportPermissionsDTO()
	{
		setDeleted("N");
	}

	public UserReportPermissionsDTO(String name, String pass)
	{
		setReportName(name);
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

	public String getReportName()
	{
		return get("reportName");
	}

	public final void setReportName(String name)
	{
		set("reportName", name);
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
		UserReportPermissionsDTO user = (UserReportPermissionsDTO) obj;
		return user.getReportName().equalsIgnoreCase(this.getReportName());
	}

}
