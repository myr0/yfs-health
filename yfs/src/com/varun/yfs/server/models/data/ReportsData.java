package com.varun.yfs.server.models.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.dto.PermissionTypeEnum;
import com.varun.yfs.dto.UserDTO;

public class ReportsData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(ReportsData.class);
	
	@Override
	public ModelData getModel(UserDTO userDto)
	{
		LOGGER.debug("Attempting data load");
		
		List<String> lstReportPermissions = userDto.getReportWithPermission(PermissionTypeEnum.READ);

		List<ModelData> arrayList = new ArrayList<ModelData>();

		ModelData model = new BaseModelData();
		model.set("data", arrayList);

		ModelData m1 = newItem("Reports", "");
		arrayList.add(m1);

		List<ModelData> child = new ArrayList<ModelData>();
		m1.set("children", child);

		List<String> repsWithRead = null;
		if (userDto.isAdmin())
			repsWithRead = ReportType.getValues();
		else
			repsWithRead = lstReportPermissions;

		for (String repName : repsWithRead)
		{
			child.add(newItem(repName, "reportIndividual"));
		}

		// child.add(newItem(ReportType.School.getValue(), "reportIndividual"));
		// child.add(newItem(ReportType.MedicalCamp.getValue(),
		// "reportIndividual"));
		// child.add(newItem(ReportType.Clinic.getValue(), "reportIndividual"));
		// child.add(newItem(ReportType.Events.getValue(), "reportIndividual"));
		LOGGER.debug("Data load complete.");
		return model;
	}

	private ModelData newItem(String text, String iconStyle)
	{
		ModelData m = new BaseModelData();
		m.set("name", text);
		m.set("icon", iconStyle);
		return m;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		return RpcStatusEnum.SUCCESS;
	}
}
