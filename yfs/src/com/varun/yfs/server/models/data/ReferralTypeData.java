package com.varun.yfs.server.models.data;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.dto.UserDTO;

public class ReferralTypeData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(ReferralTypeData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		ModelData model = new BaseModelData();
		model.set("data", DataUtil.getModelList(ModelDataEnum.ReferralType.name()));

		model.set("configIds", Arrays.asList("name"));
		model.set("configCols", Arrays.asList("Name"));
		model.set("configType", Arrays.asList("Text"));
		
		model.set("permissions", userDto.getEntityPermissionsMap().get(ModelDataEnum.ReferralType.name().toLowerCase()));
		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		List<ModelData> lstModels = model.get("data");
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			DataUtil.<ModelData> saveListStore(ModelDataEnum.ReferralType.name(), lstModels);
		} catch (Exception ex)
		{
			status = RpcStatusEnum.FAILURE;
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
		}
		return status;
	}
}
