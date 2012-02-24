package com.varun.yfs.server.common.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.dto.UserEntityPermissionsDTO;
import com.varun.yfs.dto.YesNoDTO;

public class AdministrationData extends AbstractData
{
	@Override
	public ModelData getModel(UserDTO userDto)
	{
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

		return model;
	}

	protected List<Object> applyPermission(UserDTO userDto, List<Object> modelList)
	{
		List<Object> lstModels = new ArrayList<Object>();
		List<UserEntityPermissionsDTO> lstEntityPermissions = userDto.getEntityPermissions();
		for (UserEntityPermissionsDTO userEntityPermissionsDTO : lstEntityPermissions)
		{
			if (userEntityPermissionsDTO.getRead().equalsIgnoreCase(YesNoDTO.YES.toString()))
			{
				String entityName = userEntityPermissionsDTO.getEntityName();

				ModelData tempModel = new BaseModelData();
				tempModel.set("name", entityName);

				int idx = modelList.indexOf(tempModel);
				if (idx >= 0)
					lstModels.add(modelList.get(idx));
			}
		}
		return lstModels;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.FAILURE;
	}

}
