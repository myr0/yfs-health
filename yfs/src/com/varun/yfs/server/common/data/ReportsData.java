package com.varun.yfs.server.common.data;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.dto.UserReportPermissionsDTO;
import com.varun.yfs.dto.YesNoDTO;

public class ReportsData extends AbstractData
{
	public ModelData getModel(UserDTO userDto)
	{
		List<UserReportPermissionsDTO> lstChapterPermissions = userDto.getReportPermissions();
		List<String> reportsWithRead = new ArrayList<String>();
		for (UserReportPermissionsDTO userReportPermissionsDTO : lstChapterPermissions)
		{
			if (userReportPermissionsDTO.getRead().equalsIgnoreCase(YesNoDTO.YES.toString()))
				reportsWithRead.add(userReportPermissionsDTO.getReportName());
		}

		List<ModelData> arrayList = new ArrayList<ModelData>();

		ModelData model = new BaseModelData();
		model.set("data", arrayList);

		ModelData m1 = newItem("Reports", "");
		arrayList.add(m1);

		List<ModelData> child = new ArrayList<ModelData>();
		m1.set("children", child);

		for (String repName : reportsWithRead)
		{
			child.add(newItem(repName, "reportIndividual"));
		}

		// child.add(newItem(ReportType.School.getValue(), "reportIndividual"));
		// child.add(newItem(ReportType.MedicalCamp.getValue(),
		// "reportIndividual"));
		// child.add(newItem(ReportType.Clinic.getValue(), "reportIndividual"));
		// child.add(newItem(ReportType.Events.getValue(), "reportIndividual"));

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
