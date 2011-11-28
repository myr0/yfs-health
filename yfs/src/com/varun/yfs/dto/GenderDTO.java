package com.varun.yfs.dto;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;

public class GenderDTO extends BaseModelData
{
	private static final long serialVersionUID = -6969501047599067980L;

	public GenderDTO()
	{
	}

	public GenderDTO(String val)
	{
		this.setName(val);
	}

	public String getName()
	{
		return get("name");
	}

	public final void setName(String name)
	{
		set("name", name);
	}

	public static ListStore<GenderDTO> getValues()
	{
		GenderDTO male = new GenderDTO();
		male.setName("Male");

		GenderDTO female = new GenderDTO();
		female.setName("Female");
		
		ListStore<GenderDTO> listStore = new ListStore<GenderDTO>();
		listStore.add(female);
		listStore.add(male);
		return listStore;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 == null)
			return false;

		if (this.getClass() != arg0.getClass())
			return false;

		if (this.getName() == null)
			return false;

		GenderDTO yesNo = (GenderDTO) arg0;
		return this.getName().equalsIgnoreCase(yesNo.getName());
	}

	public static List<String> getStringValues()
	{
		List<String> values = new ArrayList<String>();
		values.add("Male");
		values.add("Female");
		return values;
	}
}
