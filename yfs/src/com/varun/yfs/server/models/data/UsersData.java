package com.varun.yfs.server.models.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.dto.UserDTO;

public class UsersData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(UsersData.class);
	private ChapterNameData chapData = new ChapterNameData();
	private ProjectData projData = new ProjectData();
	private ClinicData clinicData = new ClinicData();

	@Override
	public ModelData getModel(UserDTO user)
	{
		LOGGER.debug("Attempting data load");
		
		ModelData modelData = new BaseModelData();
		List<ModelData> modelList = DataUtil.<ModelData> getModelList(ModelDataEnum.User.name());

		modelData.set("users", modelList);

		List<ModelData> chapDat = chapData.getModel(user).get("data");
		modelData.set("lstChapterNames", getNames(chapDat));

		List<ModelData> projDat = projData.getModel(user).get("data");
		modelData.set("lstProjects", getNames(projDat));

		List<ModelData> clinicDat = clinicData.getModel(user).get("data");
		modelData.set("lstClinicNames", getNames(clinicDat));

		modelData.set("lstReportNames", ReportType.getValues());
		modelData.set("lstEntityNames", DataUtil.getEntitiesList());
		
		modelData.set("permissions", user.getEntityPermissionsMap().get(ModelDataEnum.User.name().toLowerCase()));
		
		LOGGER.debug("Data load complete.");
		return modelData;
	}

	protected List<String> getNames(List<ModelData> chapDat)
	{
		List<String> lstChaps = new ArrayList<String>();
		for (ModelData modelData2 : chapDat)
		{
			lstChaps.add(modelData2.toString());
		}
		return lstChaps;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		LOGGER.debug("Attempting to save model");
		
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
		try
		{
			DataUtil.saveUserDetail(model);
			status = RpcStatusEnum.SUCCESS;
			LOGGER.debug("Save model completed successfully.");
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
		}
		return status;
	}
}
