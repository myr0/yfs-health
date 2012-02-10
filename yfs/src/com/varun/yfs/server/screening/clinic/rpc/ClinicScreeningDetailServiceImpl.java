package com.varun.yfs.server.screening.clinic.rpc;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.screening.clinic.rpc.ClinicScreeningDetailService;
import com.varun.yfs.dto.ClinicPatientDetailDTO;
import com.varun.yfs.server.common.data.DataUtil;

public class ClinicScreeningDetailServiceImpl extends RemoteServiceServlet implements ClinicScreeningDetailService
{
	private static final Logger LOGGER = Logger.getLogger(ClinicScreeningDetailServiceImpl.class);
	private static final long serialVersionUID = 4397970043413666183L;

	@Override
	public ModelData getModel(String scrId)
	{
		ModelData modelData = new BaseModelData();
		if (scrId != null)
		{
			List<ClinicPatientDetailDTO> scrDto = DataUtil.getClinicPatientDetail(Long.valueOf(scrId));
			modelData.set("data", scrDto);
		}
		modelData.set("lstReferralTypes", DataUtil.getModelList(ModelDataEnum.ReferralType.name()));

		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(String clinicId, List<ClinicPatientDetailDTO> modelData)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			DataUtil.saveScreeningDetail(clinicId,modelData);
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error trying to save the model." + ex.getCause());
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
