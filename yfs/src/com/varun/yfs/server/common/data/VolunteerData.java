package com.varun.yfs.server.common.data;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

public class VolunteerData extends AbstractData
{
	public List<ModelData> getModelList()
	{
		return DataUtil.getModelList("Volunteer");
	}
}
