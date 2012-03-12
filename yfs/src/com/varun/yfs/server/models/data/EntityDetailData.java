package com.varun.yfs.server.models.data;

import java.util.Arrays;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.UserDTO;

public class EntityDetailData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(EntityDetailData.class);
	
	@Override
	public ModelData getModel(UserDTO userDto)
	{
		LOGGER.debug("Attempting data load");
	
		ModelData model = new BaseModelData();
		model.set("data", Collections.EMPTY_LIST);

		model.set("configIds", Arrays.asList("name"));
		model.set("configCols", Arrays.asList("Name"));
		model.set("configType", Arrays.asList("Text"));
		LOGGER.debug("Data load complete.");
		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.SUCCESS;
	}
}
