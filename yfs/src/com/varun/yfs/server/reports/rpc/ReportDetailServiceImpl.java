package com.varun.yfs.server.reports.rpc;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.server.common.data.DataUtil;

public class ReportDetailServiceImpl extends RemoteServiceServlet implements ReportDetailService
{

	private static final long serialVersionUID = 4564920713040175887L;

	@Override
	public ModelData getModel(ReportType report, ModelData params)
	{
		ModelData model = new BaseModelData();
		if (ReportType.Clinic.equals(report))
		{

		} else if (ReportType.Events.equals(report))
		{

		} else if (ReportType.Medical.equals(report))
		{

		} else if (ReportType.Overall.equals(report))
		{

		} else if (ReportType.School.equals(report))
		{
			String fromDate = model.get("dateFrom");
			String toDate = model.get("dateTo");

			model.set("locationsList", 	DataUtil.executeQuery(""));
		}

		return null;

	}
}
