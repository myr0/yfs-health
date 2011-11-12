package com.varun.yfs.server.screening.school.rpc;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.screening.school.rpc.SchoolScreeningDetailService;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;
import com.varun.yfs.server.admin.rpc.ListModelDataEnum;
import com.varun.yfs.server.common.data.DataUtil;

public class SchoolScreeningDetailServiceImpl extends RemoteServiceServlet implements SchoolScreeningDetailService
{
	private static Logger logger = Logger.getLogger(SchoolScreeningDetailServiceImpl.class);
	private static final long serialVersionUID = 4397970043413666183L;
	
	@Override
	public ModelData getModel(String scrId)
	{
		ModelData modelData = new BaseModelData();
		if (scrId != null)
		{
			SchoolScreeningDetailDTO scrDto = DataUtil.getScreeningDetail(Long.valueOf(scrId));
			modelData.set("data", scrDto);
		}

		modelData.set("lstCountry", DataUtil.getModelList(ModelDataEnum.Country.name()));
		modelData.set("lstState", DataUtil.getModelList(ModelDataEnum.State.name()));
		modelData.set("lstCity", DataUtil.getModelList(ModelDataEnum.City.name()));
		modelData.set("lstTown", DataUtil.getModelList(ModelDataEnum.Town.name()));
		modelData.set("lstVillage", DataUtil.getModelList(ModelDataEnum.Village.name()));
		modelData.set("lstLocality", DataUtil.getModelList(ModelDataEnum.Locality.name()));

		modelData.set("lstChapterName", DataUtil.getModelList(ModelDataEnum.ChapterName.name()));
		modelData.set("lstReferralTypes", DataUtil.getModelList(ListModelDataEnum.ReferralType.name()));
		modelData.set("lstProcessType", DataUtil.getModelList(ListModelDataEnum.ProcessType.name()));
		modelData.set("lstTypeOfLocation", DataUtil.getModelList(ListModelDataEnum.TypeOfLocation.name()));
		modelData.set("lstVolunteers", DataUtil.getModelList(ListModelDataEnum.Volunteer.name()));
		modelData.set("lstDoctors", DataUtil.getModelList(ListModelDataEnum.Doctor.name()));

		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(String scrId, SchoolScreeningDetailDTO modelData)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			if (scrId != null)
				modelData.set("id", scrId);
			
			DataUtil.saveScreeningDetail(modelData);
		} catch (HibernateException ex)
		{
			logger.error("Encountered error trying to save the model." + ex.getCause());
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
