package com.varun.yfs.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;

public class StandardDTO extends BaseModelData
{
	private static final long serialVersionUID = -6969501047599067980L;

	public StandardDTO()
	{
	}

	public StandardDTO(String val)
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

	public static ListStore<StandardDTO> getValues()
	{
		ListStore<StandardDTO> listStore = new ListStore<StandardDTO>();

		for (String standardName : getStringValues())
		{
			StandardDTO standard = new StandardDTO();
			standard.setName(standardName);
			listStore.add(standard);
		}

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

		StandardDTO yesNo = (StandardDTO) arg0;
		return this.getName().equalsIgnoreCase(yesNo.getName());
	}

	public static List<String> getStringValues()
	{
		List<String> values = new ArrayList<String>();
		values.addAll(Arrays.asList("PreNursery", "LKG", "UKG", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX",
				"X", "XI", "XII"));
		return values;
	}
}
