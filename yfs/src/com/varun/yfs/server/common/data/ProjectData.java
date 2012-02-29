package com.varun.yfs.server.common.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.dto.ProjectDTO;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.dto.UserProjectPermissionsDTO;
import com.varun.yfs.dto.YesNoDTO;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.models.ChapterName;
import com.varun.yfs.server.models.Project;

public class ProjectData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(ProjectData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		ModelData modelData = new BaseModelData();

		List<ModelData> list = DataUtil.<ModelData> getModelList(ModelDataEnum.Project.name());
		modelData.set("data", this.applyPermission(userDto, list));
		modelData.set("parentStoreChapter", DataUtil.<ModelData> getModelList(ModelDataEnum.ChapterName.name()));

		modelData.set("configIds", Arrays.asList("projectName", "chapterName"));
		modelData.set("configCols", Arrays.asList("Project Name", "Chapter"));
		modelData.set("configType", Arrays.asList("Text", "combo"));
		
		modelData.set("permissions", userDto.getEntityPermissionsMap().get(ModelDataEnum.Project.name().toLowerCase()));
		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transact = session.beginTransaction();
		try
		{
			List<ChapterName> lstChapterName = DataUtil.<ChapterName> getRawList("ChapterName");
			List<ModelData> lstModels = model.get("data");

			Mapper dozerMapper = HibernateUtil.getDozerMapper();

			for (ModelData modelData : lstModels)
			{
				Project hibObject = dozerMapper.map(modelData, Project.class);
				String chapterName = Util.safeToString(modelData.get("chapterName"));

				hibObject.setChapterName(findParent(lstChapterName, new ChapterName(chapterName)));

				if (hibObject.getId() <= 0) // new project object - find
				{
					hibObject.setName(modelData.get("projectName").toString());
					session.save(hibObject);
				} else
				{
					hibObject.setName(modelData.get("projectName").toString());
					session.saveOrUpdate(hibObject);
				}
			}
			transact.commit();
			session.flush();
			session.close();
			status = RpcStatusEnum.SUCCESS;
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
			if (session != null)
			{
				transact.rollback();
				session.close();
			}
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

	protected List<ModelData> applyPermission(UserDTO userDto, List<ModelData> modelList)
	{
		List<ModelData> lstModels = new ArrayList<ModelData>();
		if (!userDto.isAdmin())
		{
			List<UserProjectPermissionsDTO> lstChapterPermissions = userDto.getProjectPermissions();
			for (UserProjectPermissionsDTO userProjectPermissionsDTO : lstChapterPermissions)
			{
				if (userProjectPermissionsDTO.getRead().equalsIgnoreCase(YesNoDTO.YES.toString()))
				{
					String entityName = userProjectPermissionsDTO.getProjectName();

					ProjectDTO tempModel = new ProjectDTO();
					tempModel.set("projectName", entityName);

					int idx = modelList.indexOf(tempModel);
					if (idx >= 0)
						lstModels.add(modelList.get(idx));
				}
			}
		} else
			lstModels = modelList;
		return lstModels;
	}
}
