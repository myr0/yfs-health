package com.varun.yfs.server.screening.camp.rpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.screening.camp.rpc.CampScreeningDetailService;
import com.varun.yfs.dto.CampScreeningDetailDTO;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.models.data.ChapterNameData;
import com.varun.yfs.server.models.data.DataUtil;
import com.varun.yfs.server.models.data.ProjectData;

public class CampScreeningDetailServiceImpl extends RemoteServiceServlet implements CampScreeningDetailService
{
	private static final Logger LOGGER = Logger.getLogger(CampScreeningDetailServiceImpl.class);
	private static final long serialVersionUID = 4397970043413666183L;
	private ChapterNameData chapData = new ChapterNameData();
	private ProjectData projData = new ProjectData();

	@Override
	public ModelData getModel(String scrId)
	{
		UserDTO user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("user");
		if (userObj != null && userObj instanceof UserDTO)
		{
			user = (UserDTO) userObj;
		}

		ModelData modelData = new BaseModelData();
		if (scrId != null)
		{
			CampScreeningDetailDTO scrDto = DataUtil.getCampScreeningDetail(Long.valueOf(scrId));
			modelData.set("data", scrDto);
		}

		modelData.set("lstCountry", DataUtil.getModelList(ModelDataEnum.Country.name()));
		modelData.set("lstState", DataUtil.getModelList(ModelDataEnum.State.name()));
		modelData.set("lstCity", DataUtil.getModelList(ModelDataEnum.City.name()));
		modelData.set("lstTown", DataUtil.getModelList(ModelDataEnum.Town.name()));
		modelData.set("lstVillage", DataUtil.getModelList(ModelDataEnum.Village.name()));
		modelData.set("lstLocality", DataUtil.getModelList(ModelDataEnum.Locality.name()));

		// modelData.set("lstChapterName",
		// DataUtil.getModelList(ModelDataEnum.ChapterName.name()));
		modelData.set("lstChapterName", chapData.getModel(user).get("data"));

		// modelData.set("lstProjectName",
		// DataUtil.getModelList(ModelDataEnum.Project.name()));
		modelData.set("lstProjectName", projData.getModel(user).get("data"));

		modelData.set("lstReferralTypes", DataUtil.getModelList(ModelDataEnum.ReferralType.name()));
		modelData.set("lstProcessType", DataUtil.getModelList(ModelDataEnum.ProcessType.name()));
		modelData.set("lstTypeOfLocation", DataUtil.getModelList(ModelDataEnum.TypeOfLocation.name()));
		modelData.set("lstVolunteers", DataUtil.getModelList(ModelDataEnum.Volunteer.name()));
		modelData.set("lstDoctors", DataUtil.getModelList(ModelDataEnum.Doctor.name()));

		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(String scrId, CampScreeningDetailDTO modelData)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			if (scrId != null)
				modelData.set("id", scrId);

			DataUtil.saveScreeningDetail(modelData);
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error trying to save the model." + ex.getCause());
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
