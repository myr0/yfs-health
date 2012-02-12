package com.varun.yfs.server.common.data;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.CampScreeningDetailDTO;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.UserDTO;

public class CampScreeningLocationsData extends AbstractData
{
	public ModelData getModel(UserDTO userDto)
	{
		List<ModelData> nodes = new ArrayList<ModelData>();

		ModelData model = new BaseModelData();
		model.set("data", nodes);

		List<ChapterNameDTO> rootNodes = DataUtil.<ChapterNameDTO> getModelList("ChapterName");
		for (ChapterNameDTO chapterNameDTO : rootNodes)
		{
			ModelData rootNode = new BaseModelData();
			rootNode.set("name", chapterNameDTO.getName());
			rootNode.set("icon", "");
			nodes.add(rootNode);

			List<ModelData> chapterNodes = new ArrayList<ModelData>();
			rootNode.set("children", chapterNodes);
			
			// get projects under the chapter

			List<CampScreeningDetailDTO> lstScrDet = DataUtil.getCampScreeningDetail("ChapterName", "id",
					String.valueOf(chapterNameDTO.getId()));
			for (CampScreeningDetailDTO screeningDetailDTO : lstScrDet)
			{
				ModelData scrNode = new BaseModelData();
				scrNode.set("name", screeningDetailDTO.toString());
				scrNode.set("id", String.valueOf(screeningDetailDTO.getId()));
				scrNode.set("icon", "screeningIndividual");
				chapterNodes.add(scrNode);
			}
		}

		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.FAILURE;
	}
}
