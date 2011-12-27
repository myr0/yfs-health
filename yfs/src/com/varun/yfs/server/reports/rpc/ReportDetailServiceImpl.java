package com.varun.yfs.server.reports.rpc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static final String NON_REFERRALS = "Non Referrals";
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
			Long fromDate = params.get("dateFrom");
			Long toDate = params.get("dateTo");
			Object clinicId = params.get("clinicId");
			model.set(
					"locationsCount",
					DataUtil.executeQuery("select count(*) from clinicpatientdetail cpd where cpd.clinicid ="
							+ clinicId));

			Map<String, Integer> schRepType2CountTemp = new HashMap<String, Integer>(schRepType2Count);
			List breakupOfTreatments = (List) DataUtil
					.executeQuery("select t.referral,sum(t.count1) as screened, case t.medicines when 'YES' then 1 else 0 end case as medicines, case t.caseclosed when 'YES' then 1 else 0 end case as caseclosed, case t.surgerycase when 'YES' then 1 else 0 end case surgerycase, t.screeningdate from ( select cph.referral1 as referral, count(cph.referral1) as count1, cph.medicines, cph.caseclosed, cph.surgerycase, cph.screeningdate from clinicpatientdetail cpd join clipatdet_clipathis cpdcph on cpd.clipatdetid = cpdcph.clipatdetid join clinicpatienthistory cph on cph.clipathisid = cpdcph.clipathisid where cpd.clinicid = "
							+ clinicId
							+ " and cph.referral1 is not null group by cph.referral1,cph.medicines, cph.caseclosed, cph.surgerycase , cph.screeningdate union select cph.referral2 as referral, count(cph.referral2) as count1, cph.medicines, cph.caseclosed, cph.surgerycase , cph.screeningdate from clinicpatientdetail cpd join clipatdet_clipathis cpdcph on cpd.clipatdetid = cpdcph.clipatdetid join clinicpatienthistory cph on cph.clipathisid = cpdcph.clipatdetid where cpd.clinicid ="
							+ clinicId
							+ " and cph.referral2 is not null group by cph.referral2,cph.medicines, cph.caseclosed, cph.surgerycase, cph.screeningdate ) t  where t.screeningdate >= "
							+ fromDate
							+ " and t.screeningdate <= "
							+ toDate
							+ " group by t.referral, t.medicines, t.caseclosed, t.surgerycase , t.screeningdate");
			Map<String, ModelData> referral2Model = new HashMap<String, ModelData>();
			String referralType;
			ModelData modelTemp;
			for (Object object : breakupOfTreatments)
			{
				Object[] obj = (Object[]) object;

				if (obj[0] != null)
					referralType = obj[0].toString();
				else
					referralType = NON_REFERRALS;

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
		} else if (ReportType.Events.equals(report))
		{
			Long fromDate = params.get("dateFrom");
			Long toDate = params.get("dateTo");

			List results = new ArrayList();
			List lstObjs1 = (List) DataUtil
					.executeQuery("select cd.screeningdate as date , 'Camp Screening', pt.name as eventType, cd.address as eventLocation, count(*) as noscreened from campscreeningdetail cd join locality lo on cd.localityid = lo.localityid join campscrdet_patdet cpd on cpd.camscrid = cd.campscreeningdetailid join processtype pt on pt.processtypeid = cd.processtypeid group by cd.screeningdate,pt.name, cd.address");

			if (lstObjs1 != null)
			{
				for (Object object : lstObjs1)
				{
					Object[] obj = (Object[]) object;

					String[] str = new String[obj.length];
					for (int i = 0; i < obj.length; i++)
					{
						str[i] = obj[i].toString();
					}
					results.addAll(Arrays.asList(str));
				}
			}

			List lstObjs2 = (List) DataUtil
					.executeQuery("select sd.screeningdate as date , 'School Screening', pt.name as eventType,sd.address as eventLocation, count(*) as noscreened from schoolscreeningdetail sd join locality lo on sd.localityid = lo.localityid join schoolscrdet_patdet spd on spd.schscrid = sd.schoolscreeningdetailid join processtype pt on pt.processtypeid = sd.processtypeid group by sd.screeningdate,pt.name, sd.address");

			if (lstObjs2 != null)
			{
				for (Object object : lstObjs2)
				{
					Object[] obj = (Object[]) object;
					
					String[] str = new String[obj.length];
					for (int i = 0; i < obj.length; i++)
					{
						str[i] = obj[i].toString();
					}
					results.addAll(Arrays.asList(str));
				}
			}

			model.set("eventsInfo", results);

		} else if (ReportType.MedicalCamp.equals(report))
		{
			Long fromDate = params.get("dateFrom");
			Long toDate = params.get("dateTo");
			Map<String, Integer> schRepType2CountTemp = new HashMap<String, Integer>(schRepType2Count);

			model.set(
					"locationsCount",
					DataUtil.executeQuery("select count(*) from campscreeningdetail cd join locality lo on cd.localityid = lo.localityid join campscrdet_patdet cpd on cpd.camscrid = cd.campscreeningdetailid where cd.screeningdate >= "
							+ fromDate + " and cd.screeningdate <= " + toDate));
			model.set(
					"locationsList",
					DataUtil.executeQuery("select distinct ld.localityname from campscreeningdetail cd join locality ld on cd.localityid = ld.localityid where cd.screeningdate >= "
							+ fromDate + " and cd.screeningdate <= " + toDate));
			List breakupOfTreatments = (List) DataUtil
					.executeQuery("select t.referral1, sum(t.cnt), case t.medicines when 'YES' then 1 else 0 end case as medicines,case t.caseclosed when 'YES' then 1 else 0 end case as caseclosed, case t.surgerycase when 'YES' then 1 else 0 end case surgerycase , t.screeningdate from ( select cpd.referral1,count(cpd.referral1) as cnt, cpd.medicines, cpd.caseclosed, cpd.surgerycase, cpd.emergency, csd.screeningdate from camppatientdetail cpd join referraltype rt on (cpd.referral1 = rt.name ) join campscrdet_patdet csdpd on csdpd.patid = cpd.campatdetid join campscreeningdetail csd on csd.campscreeningdetailid = csdpd.camscrid group by referral1, medicines, caseclosed, surgerycase, emergency, csd.screeningdate union all select cpd.referral2, count(cpd.referral2) as cnt, cpd.medicines, cpd.caseclosed, cpd.surgerycase, cpd.emergency, csd.screeningdate from camppatientdetail cpd join referraltype rt on (cpd.referral2 = rt.name ) join campscrdet_patdet csdpd on csdpd.patid = cpd.campatdetid join campscreeningdetail csd on csd.campscreeningdetailid = csdpd.camscrid group by referral2, medicines, caseclosed, surgerycase, emergency, csd.screeningdate union all select cpd.referral2,count(*) as cnt, cpd.medicines, cpd.caseclosed, cpd.surgerycase, cpd.emergency, csd.screeningdate from camppatientdetail cpd join campscrdet_patdet csdpd on csdpd.patid = cpd.campatdetid join campscreeningdetail csd on csd.campscreeningdetailid = csdpd.camscrid where cpd.referral1 is null and cpd.referral2 is null group by referral2, medicines, caseclosed, surgerycase, emergency , csd.screeningdate ) t where t.screeningdate >="
							+ fromDate
							+ " and t.screeningdate <="
							+ toDate
							+ " group by t.referral1, t.medicines, t.caseclosed, t.surgerycase, t.emergency, t.screeningdate");
			Map<String, ModelData> referral2Model = new HashMap<String, ModelData>();
			String referralType;
			ModelData modelTemp;
			for (Object object : breakupOfTreatments)
			{
				Object[] obj = (Object[]) object;

				if (obj[0] != null)
					referralType = obj[0].toString();
				else
					referralType = NON_REFERRALS;

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
			Long fromDate = params.get("dateFrom");
			Long toDate = params.get("dateTo");

			Map<String, Integer> schRepType2CountTemp = new HashMap<String, Integer>(schRepType2Count);

			model.set(
					"locationsCount",
					DataUtil.executeQuery("select count(*) from schoolscreeningdetail sd join locality lo on sd.localityid = lo.localityid join schoolscrdet_patdet spd on spd.schScrId = sd.schoolscreeningdetailid where sd.screeningdate >= "
							+ fromDate + " and sd.screeningdate <= " + toDate));
			model.set(
					"locationsList",
					DataUtil.executeQuery("select distinct ld.localityname from schoolscreeningdetail sd join locality ld on sd.localityid = ld.localityid where sd.screeningdate >= "
							+ fromDate + " and sd.screeningdate <= " + toDate));
			List breakupOfTreatments = (List) DataUtil
					.executeQuery("select t.referral1 as referral, sum(t.cnt) as screened, case t.medicines when 'YES' then 1 else 0 end case as medicines, case t.caseclosed when 'YES' then 1 else 0 end case as caseclosed, case t.surgerycase when 'YES' then 1 else 0 end case surgerycase, t.screeningdate from ( select spd.referral1,count(spd.referral1) as cnt, spd.medicines, spd.caseclosed, spd.surgerycase,spd.emergency, ssd.screeningdate from schoolpatientdetail spd join referraltype rt on (spd.referral1 = rt.name ) join schoolscrdet_patdet ssdpd on ssdpd.patid = spd.schpatdetid join schoolscreeningdetail ssd on ssd.schoolscreeningdetailid = ssdpd.schScrid group by referral1, medicines, caseclosed, surgerycase, emergency, ssd.screeningdate union all select spd.referral2, count(spd.referral2) as cnt, spd.medicines, spd.caseclosed, spd.surgerycase, spd.emergency, ssd.screeningdate from schoolpatientdetail spd join referraltype rt on (spd.referral2 = rt.name ) join schoolscrdet_patdet ssdpd on ssdpd.patid = spd.schpatdetid join schoolscreeningdetail ssd on ssd.schoolscreeningdetailid = ssdpd.schScrid group by referral2, medicines, caseclosed, surgerycase, emergency, ssd.screeningdate union all select spd.referral2,count(*) as snt, spd.medicines,spd.caseclosed, spd.surgerycase, spd.emergency, ssd.screeningdate from schoolpatientdetail spd join schoolscrdet_patdet ssdpd on ssdpd.patid = spd.schpatdetid join schoolscreeningdetail ssd on ssd.schoolscreeningdetailid = ssdpd.schScrid where spd.referral1 is null and spd.referral2 is null group by referral2, medicines, caseclosed, surgerycase, emergency, ssd.screeningdate ) t where t.screeningdate >="
							+ fromDate
							+ " and t.screeningdate <= "
							+ toDate
							+ " group by t.referral1, t.medicines, t.caseclosed, t.surgerycase, t.emergency, t.screeningdate");
			Map<String, ModelData> referral2Model = new HashMap<String, ModelData>();
			ModelData modelTemp;
			String referralType;
			for (Object object : breakupOfTreatments)
			{
				Object[] obj = (Object[]) object;

				if (obj[0] != null)
					referralType = obj[0].toString();
				else
					referralType = NON_REFERRALS;

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
