package com.varun.yfs.server.models.data;

import java.util.Arrays;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.dto.UserDTO;

public class EntityData extends AbstractData
{
	@Override
	public ModelData getModel(UserDTO userDto)
	{
		ModelData model = new BaseModelData();
		model.set("data", DataUtil.getModelList("Entities"));

		model.set("configIds", Arrays.asList("name"));
		model.set("configCols", Arrays.asList("Name"));
		model.set("configType", Arrays.asList("Text"));

		model.set("permissions", userDto.getEntityPermissionsMap().get("Entities".toLowerCase()));
		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.SUCCESS;
	}
}
