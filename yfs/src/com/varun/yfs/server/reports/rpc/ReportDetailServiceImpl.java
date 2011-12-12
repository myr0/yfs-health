package com.varun.yfs.server.reports.rpc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.server.common.data.DataUtil;

public class ReportDetailServiceImpl extends RemoteServiceServlet implements ReportDetailService
{
	private static final long serialVersionUID = -8632087746514887014L;
	private final Map<String, Integer> schRepType2Count = new HashMap<String, Integer>();
	private final Map<String, String> schRepCol2Type = new HashMap<String, String>();

	public ReportDetailServiceImpl()
	{
		schRepType2Count.put("surgeryCasesClosed", 0);
		schRepType2Count.put("medicineCasesClosed", 0);
		schRepType2Count.put("pendingSurgeryCases", 0);
		schRepType2Count.put("followUpMedicines", 0);
		schRepType2Count.put("closedSurgeryCases", 0);
		schRepType2Count.put("caseClosed", 0);
		schRepType2Count.put("referredToHospital", 0);
		schRepType2Count.put("pendingCases", 0);

		schRepCol2Type.put("000", "pendingCases");
		schRepCol2Type.put("001", "referredToHospital");
		schRepCol2Type.put("010", "caseClosed");
		schRepCol2Type.put("011", "closedSurgeryCases");
		schRepCol2Type.put("100", "followUpMedicines");
		schRepCol2Type.put("101", "pendingSurgeryCases");
		schRepCol2Type.put("110", "medicineCasesClosed");
		schRepCol2Type.put("111", "surgeryCasesClosed");
	}

	@Override
	public ModelData getModel(ReportType report, ModelData params)
	{
		ModelData model = new BaseModelData();
		if (ReportType.Clinic.equals(report))
		{
			model.set("locationsCount", DataUtil.executeQuery("select count(*) from campscreeningdetail cd join locality lo on cd.localityid = lo.localityid join campscrdet_patdet cpd on cpd.campid = cd.campscreeningdetailid"));
		} else if (ReportType.Events.equals(report))
		{

		} else if (ReportType.MedicalCamp.equals(report))
		{
			Map<String, Integer> schRepType2CountTemp = new HashMap<String, Integer>(schRepType2Count);

			model.set("locationsCount", DataUtil.executeQuery("select count(*) from campscreeningdetail cd join locality lo on cd.localityid = lo.localityid join campscrdet_patdet cpd on cpd.campid = cd.campscreeningdetailid"));
			model.set("locationsList", DataUtil.executeQuery("select distinct ld.localityname from campscreeningdetail cd join locality ld on cd.localityid = ld.localityid"));
			List breakupOfTreatments = (List) DataUtil
					.executeQuery("select t.referral,sum(t.count1) as screened, case t.medicines when 'YES' then 1 else 0 end case as medicines,case t.caseclosed when 'YES' then 1 else 0 end case as caseclosed, case t.surgerycase when 'YES' then 1 else 0 end case surgerycase from (select referral1 as referral,count(referral1) as count1,medicines, count(medicines) as countmedicines, caseclosed, count(caseclosed) as countcases, surgerycase, count(surgerycase) as countsurgerycase from camppatientdetail spd join referraltype rt on (spd.referral1 = rt.name) where referral1 is not null group by referral1,medicines,caseclosed,surgerycase UNION select referral2 as referral,count(referral2) as count1,medicines, count(medicines) as countmedicines, caseclosed, count(caseclosed) as countcases, surgerycase, count(surgerycase) as countsurgerycase from camppatientdetail spd join referraltype rt on (spd.referral2 = rt.name) where referral2 is not null group by referral2,medicines,caseclosed,surgerycase) t group by t.referral, t.medicines, t.caseclosed, t.surgerycase");
			Map<String, ModelData> referral2Model = new HashMap<String, ModelData>();
			for (Object object : breakupOfTreatments)
			{
				ModelData modelTemp;
				Object[] obj = (Object[]) object;

				String referralType = obj[0].toString();
				if (referral2Model.containsKey(referralType))
				{
					modelTemp = referral2Model.get(referralType);

					BigInteger count = (BigInteger) obj[1];
					Integer screenedCnt = modelTemp.get("screened");
					modelTemp.set("screened", count.intValue() + screenedCnt);
				} else
				{
					modelTemp = new BaseModelData();
					modelTemp.set("breakUpOfTreatment", referralType);
					modelTemp.set("screened", ((BigInteger) obj[1]).intValue());
					referral2Model.put(referralType, modelTemp);
				}

				String medicines = String.valueOf(obj[2]);
				String caseClosed = String.valueOf(obj[3]);
				String surgeryCase = String.valueOf(obj[4]);
				String key2RepType = medicines.concat(caseClosed).concat(surgeryCase); // 000

				String repColType = schRepCol2Type.get(key2RepType); // colType
				Integer screenedCnt = schRepType2CountTemp.get(repColType); // cur-type
				BigInteger count = (BigInteger) obj[1]; // act count

				modelTemp.set(repColType, count.intValue() + screenedCnt);

			}
			model.set("breakupOfTreatments", new ArrayList<ModelData>(referral2Model.values()));
			model.set("statusOfTreatments", new ArrayList<ModelData>(referral2Model.values()));

		} else if (ReportType.Overall.equals(report))
		{

		} else if (ReportType.School.equals(report))
		{
			// Long fromDate = params.get("dateFrom");
			// Long toDate = params.get("dateTo");

			Map<String, Integer> schRepType2CountTemp = new HashMap<String, Integer>(schRepType2Count);

			model.set("locationsCount", DataUtil.executeQuery("select count(*) from schoolscreeningdetail sd join locality lo on sd.localityid = lo.localityid join schoolscrdet_patdet spd on spd.schid = sd.schoolscreeningdetailid"));
			model.set("locationsList", DataUtil.executeQuery("select distinct ld.localityname from schoolscreeningdetail sd join locality ld on sd.localityid = ld.localityid"));
			List breakupOfTreatments = (List) DataUtil
					.executeQuery("select t.referral,sum(t.count1) as screened, case t.medicines when 'YES' then 1 else 0 end case as medicines,case t.caseclosed when 'YES' then 1 else 0 end case as caseclosed,case t.surgerycase when 'YES' then 1 else 0 end case surgerycase from (select referral1 as referral,count(referral1) as count1,medicines, count(medicines) as countmedicines, caseclosed, count(caseclosed) as countcases, surgerycase, count(surgerycase) as countsurgerycase from schoolpatientdetail spd join referraltype rt on (spd.referral1 = rt.name) where referral1 is not null group by referral1,medicines,caseclosed,surgerycase UNION select referral2 as referral,count(referral2) as count1,medicines, count(medicines) as countmedicines, caseclosed, count(caseclosed) as countcases, surgerycase, count(surgerycase) as countsurgerycase from schoolpatientdetail spd join referraltype rt on (spd.referral2 = rt.name) where referral2 is not null group by referral2,medicines,caseclosed,surgerycase) t group by t.referral, t.medicines, t.caseclosed, t.surgerycase");
			Map<String, ModelData> referral2Model = new HashMap<String, ModelData>();
			for (Object object : breakupOfTreatments)
			{
				ModelData modelTemp;
				Object[] obj = (Object[]) object;

				String referralType = obj[0].toString();
				if (referral2Model.containsKey(referralType))
				{
					modelTemp = referral2Model.get(referralType);

					BigInteger count = (BigInteger) obj[1];
					Integer screenedCnt = modelTemp.get("screened");
					modelTemp.set("screened", count.intValue() + screenedCnt);
				} else
				{
					modelTemp = new BaseModelData();
					modelTemp.set("breakUpOfTreatment", referralType);
					modelTemp.set("screened", ((BigInteger) obj[1]).intValue());
					referral2Model.put(referralType, modelTemp);
				}

				String medicines = String.valueOf(obj[2]);
				String caseClosed = String.valueOf(obj[3]);
				String surgeryCase = String.valueOf(obj[4]);
				String key2RepType = medicines.concat(caseClosed).concat(surgeryCase); // 000

				String repColType = schRepCol2Type.get(key2RepType); // colType
				Integer screenedCnt = schRepType2CountTemp.get(repColType); // cur-type
				BigInteger count = (BigInteger) obj[1]; // act count

				modelTemp.set(repColType, count.intValue() + screenedCnt);

			}
			model.set("breakupOfTreatments", new ArrayList<ModelData>(referral2Model.values()));
			model.set("statusOfTreatments", new ArrayList<ModelData>(referral2Model.values()));
		}

		return model;

	}
}
