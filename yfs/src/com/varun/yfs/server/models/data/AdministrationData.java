package com.varun.yfs.server.models.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.EntitiesDTO;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.dto.UserEntityPermissionsDTO;
import com.varun.yfs.dto.YesNoDTO;

public class AdministrationData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(AdministrationData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		LOGGER.debug("Attempting data load");

		ModelData model = new BaseModelData();

		List<Object> modelList = DataUtil.getModelList("Entities");

		List<Object> lstModels = null;

		if (userDto.isAdmin())
			lstModels = modelList;
		else
			lstModels = applyPermission(userDto, modelList);

		model.set("data", lstModels);

		model.set("configIds", Arrays.asList("name"));
		model.set("configCols", Arrays.asList("Name"));
		model.set("configType", Arrays.asList("Text"));

		LOGGER.debug("Data load complete.");

		return model;
	}

	protected List<Object> applyPermission(UserDTO userDto, List<Object> modelList)
	{
		LOGGER.debug("Applying Permissions to the data - " + modelList);

		List<Object> lstModels = new ArrayList<Object>();
		List<UserEntityPermissionsDTO> lstEntityPermissions = userDto.getEntityPermissions();
		for (UserEntityPermissionsDTO userEntityPermissionsDTO : lstEntityPermissions)
		{
			if (userEntityPermissionsDTO.getRead().equalsIgnoreCase(YesNoDTO.YES.toString()))
			{
				String entityName = userEntityPermissionsDTO.getEntityName();

				EntitiesDTO tempModel = new EntitiesDTO();
				tempModel.set("name", entityName);

				int idx = modelList.indexOf(tempModel);
				if (idx >= 0)
					lstModels.add(modelList.get(idx));
			}
		}

		LOGGER.debug("Applying Permissions to the data completed - " + lstModels);
		return lstModels;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.FAILURE;
	}

}
