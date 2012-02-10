package com.varun.yfs.server.common.data;

import java.util.Arrays;
import java.util.Collections;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;

public class DefaultData extends AbstractData
{
	public ModelData getModel()
	{
		ModelData model = new BaseModelData();
		model.set("data", Collections.emptyList());

		model.set("configIds", Arrays.asList("name"));
		model.set("configCols", Arrays.asList("Name"));
		model.set("configType", Arrays.asList("Text"));
		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.SUCCESS;
	}
}
