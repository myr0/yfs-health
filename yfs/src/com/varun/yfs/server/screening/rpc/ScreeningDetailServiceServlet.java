package com.varun.yfs.server.screening.rpc;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.screening.rpc.ScreeningDetailService;
import com.varun.yfs.dto.ScreeningDetailDTO;
import com.varun.yfs.server.admin.rpc.ListModelDataEnum;
import com.varun.yfs.server.common.data.DataUtil;

public class ScreeningDetailServiceServlet extends RemoteServiceServlet implements ScreeningDetailService
{
	private static Logger logger = LoggerFactory.getLogger(ScreeningDetailServiceServlet.class);
	private static final long serialVersionUID = 4397970043413666183L;

	@Override
	public ModelData getModel(String entityName)
	{
		ModelData modelData = new BaseModelData();
		modelData.set("lstCountry", DataUtil.getModelList(ModelDataEnum.Country.name()));
		modelData.set("lstState", DataUtil.getModelList(ModelDataEnum.State.name()));
		modelData.set("lstCity", DataUtil.getModelList(ModelDataEnum.City.name()));
		modelData.set("lstTown", DataUtil.getModelList(ModelDataEnum.Town.name()));
		modelData.set("lstVillage", DataUtil.getModelList(ModelDataEnum.Village.name()));
		modelData.set("lstLocality", DataUtil.getModelList(ModelDataEnum.Locality.name()));

		modelData.set("lstProcessType", DataUtil.getModelList(ListModelDataEnum.ProcessType.name()));
		modelData.set("lstTypeOfLocation", DataUtil.getModelList(ListModelDataEnum.TypeOfLocation.name()));
		modelData.set("lstVolunteers", DataUtil.getModelList(ListModelDataEnum.Volunteer.name()));
		modelData.set("lstDoctors", DataUtil.getModelList(ListModelDataEnum.Doctor.name()));

		return modelData;
	}

	@Override
	public String saveModel(String entityName, ScreeningDetailDTO modelData)
	{
		// ScreeningDetailDTO screeningDetailDTO = new ScreeningDetailDTO();
		// screeningDetailDTO.setCountry((CountryDTO) modelData.get("country"));
		// screeningDetailDTO.setState((StateDTO) modelData.get("state"));
		// screeningDetailDTO.setCity((CityDTO) modelData.get("city"));
		// screeningDetailDTO.setTown((TownDTO) modelData.get("town"));
		// screeningDetailDTO.setVillage((VillageDTO) modelData.get("village"));
		// screeningDetailDTO.setLocality((LocalityDTO)
		// modelData.get("locality"));
		//
		// screeningDetailDTO.setProcessType((ProcessTypeDTO)
		// modelData.get("processType"));
		// screeningDetailDTO.setTypeOfLocation((TypeOfLocationDTO)
		// modelData.get("typeOfLocation"));
		// screeningDetail.setSetVolunteers((Set<VolunteerDTO>)
		// modelData.get("volunteers"));
		// screeningDetail.setSetDoctors((Set<DoctorDTO>)
		// modelData.get("doctors"));
		String status = "Sucess";
		try
		{
			DataUtil.saveScreeningDetail(modelData);
		} catch (HibernateException ex)
		{
			logger.error("Encountered error trying to save the model" + ex.getCause());
			status = "Failed";
		}
		return status;
	}

}
