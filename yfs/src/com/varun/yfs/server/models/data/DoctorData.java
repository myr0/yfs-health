package com.varun.yfs.server.models.data;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.dto.UserDTO;

public class DoctorData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(DoctorData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		LOGGER.debug("Attempting data load");

		ModelData model = new BaseModelData();
		model.set("data", DataUtil.getModelList(ModelDataEnum.Doctor.name()));

		model.set("configIds", Arrays.asList("name"));
		model.set("configCols", Arrays.asList("Name"));
		model.set("configType", Arrays.asList("Text"));

		model.set("permissions", userDto.getEntityPermissionsMap().get(ModelDataEnum.Doctor.name().toLowerCase()));
		LOGGER.debug("Data load complete.");
		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		LOGGER.debug("Attempting to save model");
		
		List<ModelData> lstModels = model.get("data");
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			DataUtil.<ModelData> saveListStore(ModelDataEnum.Doctor.name(), lstModels);
			
			LOGGER.debug("Save model completed successfully.");
		} catch (Exception ex)
		{
			LOGGER.error("Model could not be saved, encountered error :" + ex);
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}
}
