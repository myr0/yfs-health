package com.varun.yfs.server.reports.rpc;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.server.common.data.DataUtil;

public class ReportDetailServiceImpl extends RemoteServiceServlet implements ReportDetailService
{
	private static final long serialVersionUID = -8632087746514887014L;

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
			Long fromDate = params.get("dateFrom");
			Long toDate = params.get("dateTo");

			model.set("locationsCount", DataUtil.executeQuery("select count(*) from schoolscreeningdetail sd join locality ld on sd.schoolscreeningdetailid = ld.localityid"));
			model.set("locationsList", DataUtil.executeQuery("select ld.* from schoolscreeningdetail sd join locality ld on sd.schoolscreeningdetailid = ld.localityid"));
			model.set("breakupOfTreatments", DataUtil.executeQuery("select t.referral,sum(t.count1) from (select referral1 as referral,count(referral1) as count1 from schoolpatientdetail spd join referraltype rt on (spd.referral1 = rt.name) " + "where referral1 is not null group by referral1 union select referral2 as referral,count(referral2) as count1 from schoolpatientdetail spd join referraltype rt on (spd.referral2 = rt.name) " + " where referral2 is not null group by referral2) t group by t.referral "));
		}

		return null;

	}
}
