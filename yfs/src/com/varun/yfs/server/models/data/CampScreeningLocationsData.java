package com.varun.yfs.server.models.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.CampScreeningDetailDTO;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.PermissionTypeEnum;
import com.varun.yfs.dto.UserDTO;

public class CampScreeningLocationsData extends AbstractData
{
	@SuppressWarnings("unchecked")
	@Override
	public ModelData getModel(UserDTO userDto)
	{
		List<ModelData> nodes = new ArrayList<ModelData>();

		List<String> lstChapterPermissions = userDto.getChaptersWithPermission(PermissionTypeEnum.READ);

		List<String> chapsWithRead;
		if (userDto.isAdmin())
			chapsWithRead = (List<String>) DataUtil.executeQuery("select name from ChapterName where deleted = 'N'");
		else
			chapsWithRead = lstChapterPermissions;

		ModelData model = new BaseModelData();
		model.set("data", nodes);

		List<ChapterNameDTO> rootNodes = DataUtil.<ChapterNameDTO> getModelList("ChapterName");
		for (ChapterNameDTO chapterNameDTO : rootNodes)
		{
			String chapName = chapterNameDTO.getName();

			if (!chapsWithRead.contains(chapName))
				continue;

			ModelData rootNode = new BaseModelData();
			rootNode.set("name", chapName);
			rootNode.set("icon", "");
			nodes.add(rootNode);

			List<ModelData> projectNodes = new ArrayList<ModelData>();
			rootNode.set("children", projectNodes);

			// get projects under the chapter
			Map<String, List<ModelData>> mapChap2Screening = new HashMap<String, List<ModelData>>();

			List<CampScreeningDetailDTO> lstScrDet = DataUtil.getCampScreeningDetail("ChapterName", "id",
					String.valueOf(chapterNameDTO.getId()));

			for (CampScreeningDetailDTO scrDto : lstScrDet)
			{
				ModelData scrNode = new BaseModelData();
				scrNode.set("name", scrDto.toString());
				scrNode.set("id", String.valueOf(scrDto.getId()));
				scrNode.set("icon", "screeningIndividual");

				String key = scrDto.getProjectName().getName();
				if (mapChap2Screening.containsKey(key))
				{
					mapChap2Screening.get(key).add(scrNode);
				} else
				{
					List<ModelData> lst = new ArrayList<ModelData>();
					lst.add(scrNode);
					mapChap2Screening.put(key, lst);
				}
			}

			for (String key : mapChap2Screening.keySet())
			{
				ModelData projectNode = new BaseModelData();
				projectNode.set("name", key);
				projectNode.set("icon", "screeningIndividual");
				projectNode.set("children", mapChap2Screening.get(key));
				projectNodes.add(projectNode);
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
