package com.varun.yfs.server.common.data;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.dto.UserDTO;

public class UsersData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(UsersData.class);

	@SuppressWarnings("unchecked")
	public ModelData getModel(UserDTO userDto)
	{
		ModelData modelData = new BaseModelData();
		List<ModelData> modelList = DataUtil.<ModelData> getModelList("User");
		List<String> lstChapterNames = (List<String>) DataUtil.executeQuery("select name from ChapterName");
		List<String> lstProjects = (List<String>) DataUtil.executeQuery("select name from Project");
		List<String> lstClinics = (List<String>) DataUtil.executeQuery("select clinicName from Clinic");

		modelData.set("users", modelList);
		modelData.set("lstChapterNames", lstChapterNames);
		modelData.set("lstProjects", lstProjects);
		modelData.set("lstClinicNames", lstClinics);
		modelData.set("lstReportNames", Arrays.asList(ReportType.values()));
		modelData.set("lstEntityNames", DataUtil.getEntitiesList());
		return modelData;
	}

	public RpcStatusEnum saveModel(ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
		try
		{
			DataUtil.saveUserDetail(model);
			status = RpcStatusEnum.SUCCESS;
		} catch (HibernateException ex)
		{
			status = RpcStatusEnum.FAILURE;
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
		}
		return status;
	}
}
