package com.varun.yfs.server.models.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.PermissionTypeEnum;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;
import com.varun.yfs.dto.UserDTO;

public class SchoolScreeningLocationsData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(SchoolScreeningLocationsData.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public ModelData getModel(UserDTO userDto)
	{
		LOGGER.debug("Attempting data load");
		
		List<ModelData> nodes = new ArrayList<ModelData>();

		List<String> lstChaptersPermissions = userDto.getChaptersWithPermission(PermissionTypeEnum.READ);

		List<String> chapsWithRead = null;
		if (userDto.isAdmin())
			chapsWithRead = (List<String>) DataUtil.executeQuery("select name from ChapterName where deleted = 'N'");
		else
			chapsWithRead = lstChaptersPermissions;

		ModelData model = new BaseModelData();
		model.set("data", nodes);
		
		model.set("permissions", userDto.getChapterPermissionsMap());

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

			List<SchoolScreeningDetailDTO> lstScrDet = DataUtil.getSchoolScreeningDetail("ChapterName", "id",
					String.valueOf(chapterNameDTO.getId()));
			for (SchoolScreeningDetailDTO scrDto : lstScrDet)
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
		LOGGER.debug("Data load complete.");
		return model;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.SUCCESS;
	}
}
