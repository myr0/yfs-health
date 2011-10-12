package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.store.ListStore;

public class YesNoDTO extends BaseModelData
{
	private static final long serialVersionUID = 6280337431842932388L;

	public YesNoDTO()
	{
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
		YesNoDTO yes = new YesNoDTO();
		yes.setName("YES");

		YesNoDTO no = new YesNoDTO();
		no.setName("NO");

		ListStore<YesNoDTO> listStore = new ListStore<YesNoDTO>();
		listStore.add(no);
		listStore.add(yes);
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
