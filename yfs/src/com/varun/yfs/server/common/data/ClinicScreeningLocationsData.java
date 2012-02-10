package com.varun.yfs.server.common.data;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.CityDTO;
import com.varun.yfs.dto.ClinicDTO;

public class ClinicScreeningLocationsData extends AbstractData
{
	public ModelData getModel()
	{
		List<ModelData> nodes = new ArrayList<ModelData>();

		ModelData model = new BaseModelData();
		model.set("data", nodes);

		List<CityDTO> rootNodes = DataUtil.<CityDTO> getModelList("City");
		for (CityDTO cityDTO : rootNodes)
		{
			ModelData rootNode = new BaseModelData();
			rootNode.set("name", cityDTO.getName());
			rootNode.set("icon", "");
			nodes.add(rootNode);

			List<ModelData> chapterNodes = new ArrayList<ModelData>();
			rootNode.set("children", chapterNodes);

			List<ClinicDTO> lstScrDet = DataUtil.getClinics("City", "id", String.valueOf(cityDTO.getId()));
			for (ClinicDTO clinicDTO : lstScrDet)
			{
				ModelData scrNode = new BaseModelData();
				scrNode.set("name", clinicDTO.toString());
				scrNode.set("id", String.valueOf(clinicDTO.getId()));
				scrNode.set("icon", "screeningIndividual");
				chapterNodes.add(scrNode);
			}
		}

		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.SUCCESS;
	}
}
