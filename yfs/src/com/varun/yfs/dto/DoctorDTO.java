package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class DoctorDTO extends BaseModelData
{
	private static final long serialVersionUID = -3127025786082687723L;
	private long id;

	public DoctorDTO()
	{
		set("deleted", "N");
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

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 == null)
			return false;
		if (get("name") == null)
			return false;
		if (arg0.getClass() != this.getClass())
			return false;
		DoctorDTO doctor = (DoctorDTO) arg0;
		return getName().equalsIgnoreCase(doctor.getName());
	}
}
