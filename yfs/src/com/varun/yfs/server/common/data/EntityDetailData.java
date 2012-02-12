package com.varun.yfs.server.common.data;

import java.util.Arrays;
import java.util.Collections;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.UserDTO;

public class EntityDetailData extends AbstractData
{
	public ModelData getModel(UserDTO userDto)
	{
		ModelData model = new BaseModelData();
		model.set("data", Collections.EMPTY_LIST);

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
