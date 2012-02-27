package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserClinicPermissionsDTO extends BaseModelData
{
	private static final long serialVersionUID = -4689344436728499215L;

	private long id;

	public UserClinicPermissionsDTO()
	{
		setDeleted("N");
	}

	public UserClinicPermissionsDTO(String name, String pass)
	{
		setClinicName(name);
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

	public String getClinicName()
	{
		return get("clinicName");
	}

	public final void setClinicName(String name)
	{
		set("clinicName", name);
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
		UserClinicPermissionsDTO user = (UserClinicPermissionsDTO) obj;
		return user.getClinicName().equalsIgnoreCase(this.getClinicName());
	}

}
