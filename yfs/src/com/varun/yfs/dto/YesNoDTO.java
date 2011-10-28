package com.varun.yfs.dto;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;

public class YesNoDTO extends BaseModelData
{
	private static final long serialVersionUID = 6280337431842932388L;
	public static YesNoDTO YES = new YesNoDTO("YES");
	public static YesNoDTO NO = new YesNoDTO("NO");

	public YesNoDTO()
	{
	}

	public YesNoDTO(String name)
	{
		setName(name);
	}

	public String getName()
	{
		return get("name");
	}

	public void setName(String name)
	{
		set("name", name);
	}

	public static ListStore<YesNoDTO> getValues()
	{
		ListStore<YesNoDTO> listStore = new ListStore<YesNoDTO>();
		listStore.add(NO);
		listStore.add(YES);
		return listStore;
	}

	public static List<String> getStringValues()
	{
		List<String> listStore = new ArrayList<String>();
		listStore.add(NO.toString());
		listStore.add(YES.toString());
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

		YesNoDTO yesNo = (YesNoDTO) arg0;
		return this.getName().equalsIgnoreCase(yesNo.getName());
	}
}
