package com.varun.yfs.server.reports;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.dto.ExportTableDataDTO;
import com.varun.yfs.server.models.data.DataUtil;

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
			generateClinicReport(params, model);
		} else if (ReportType.Events.equals(report))
		{
			generateEventsReport(params, model);
		} else if (ReportType.MedicalCamp.equals(report))
		{
			generateMedicalCampReport(params, model);
		} else if (ReportType.School.equals(report))
		{
			generateSchoolScreeningReport(params, model);
		}
		// else if (ReportType.Overall.equals(report))
		// {
		// }

		return model;

	}

	private void generateSchoolScreeningReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");

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

		Map<String, ExportTableDataDTO> referral2Model = buildBreakupSummaryModel(breakupOfTreatments);

		model.set("breakupOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
		model.set("statusOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
	}

	private void generateMedicalCampReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");

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

		Map<String, ExportTableDataDTO> referral2Model = buildBreakupSummaryModel(breakupOfTreatments);

		model.set("breakupOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
		model.set("statusOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
	}

	private void generateEventsReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");

		List<Object> processTypes = (List<Object>) DataUtil
				.executeQuery("select processtypeid, name from processtype p where p.deleted = 'N'");
		Map<String, String> mapProcessId2Name = new HashMap<String, String>();
		for (Object object : processTypes)
		{
			Object[] row = (Object[]) object;
			mapProcessId2Name.put(row[0].toString(), row[1].toString());
		}

		List<ExportTableDataDTO> results = genMedCampSummary(mapProcessId2Name);

		results.addAll(genSchoolScreeningSummary(mapProcessId2Name));

		model.set("eventsInfo", results);
	}

	private void generateClinicReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");
		Object clinicId = params.get("clinicId");
		model.set(
				"locationsCount",
				DataUtil.executeQuery("select count(*) from clinicpatientdetail cpd join clipatdet_clipathis cpdcph on cpd.clipatdetid = cpdcph.clipatdetid join clinicpatienthistory cph on cph.clipathisid = cpdcph.clipathisid where cpd.clinicid ="
						+ clinicId + " and cph.screeningdate >= " + fromDate + " and cph.screeningdate <= " + toDate));

		// Map<String, Integer> schRepType2CountTemp = new HashMap<String,
		// Integer>(schRepType2Count);
		List breakupOfTreatments = (List) DataUtil
				.executeQuery("select t.referral,sum(t.count1) as screened, case t.medicines when 'YES' then 1 else 0 end case as medicines, case t.caseclosed when 'YES' then 1 else 0 end case as caseclosed, case t.surgerycase when 'YES' then 1 else 0 end case surgerycase, t.screeningdate from ( select cph.referral1 as referral, count(cph.referral1) as count1, cph.medicines, cph.caseclosed, cph.surgerycase, cph.screeningdate from clinicpatientdetail cpd join clipatdet_clipathis cpdcph on cpd.clipatdetid = cpdcph.clipatdetid join clinicpatienthistory cph on cph.clipathisid = cpdcph.clipathisid where cpd.clinicid = "
						+ clinicId
						+ " and cph.referral1 is not null group by cph.referral1,cph.medicines, cph.caseclosed, cph.surgerycase , cph.screeningdate union select cph.referral2 as referral, count(cph.referral2) as count1, cph.medicines, cph.caseclosed, cph.surgerycase , cph.screeningdate from clinicpatientdetail cpd join clipatdet_clipathis cpdcph on cpd.clipatdetid = cpdcph.clipatdetid join clinicpatienthistory cph on cph.clipathisid = cpdcph.clipathisid where cpd.clinicid ="
						+ clinicId
						+ " and cph.referral2 is not null group by cph.referral2,cph.medicines, cph.caseclosed, cph.surgerycase, cph.screeningdate union select cph.referral2 as referral, count(*) as count1, cph.medicines, cph.caseclosed, cph.surgerycase , cph.screeningdate from clinicpatientdetail cpd join clipatdet_clipathis cpdcph on cpd.clipatdetid = cpdcph.clipatdetid join clinicpatienthistory cph on cph.clipathisid = cpdcph.clipathisid where cpd.clinicid ="
						+ clinicId
						+ " and cph.referral2 is null and cph.referral1 is null group by cph.referral2,cph.medicines, cph.caseclosed, cph.surgerycase, cph.screeningdate ) t  where t.screeningdate >= "
						+ fromDate
						+ " and t.screeningdate <= "
						+ toDate
						+ " group by t.referral, t.medicines, t.caseclosed, t.surgerycase , t.screeningdate");

		Map<String, ExportTableDataDTO> referral2Model = buildBreakupSummaryModel(breakupOfTreatments);

		model.set("breakupOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
		// model.set("statusOfTreatments", new
		// ArrayList<ExportTableDataDTO>(referral2Model.values()));
	}

	private List<ExportTableDataDTO> extractRowData(Map<String, List<String>> mapId2Docs,
			Map<String, List<String>> mapId2Volunteers, Map<String, String> mapProcessId2Name, List<Object> lstData)
	{
		Calendar cal = Calendar.getInstance();

		List<ExportTableDataDTO> results = new ArrayList<ExportTableDataDTO>();
		for (Object object : lstData)
		{
			Object[] obj = (Object[]) object;

			ExportTableDataDTO modelTemp = new ExportTableDataDTO();
			cal.setTimeInMillis(Long.parseLong(obj[0].toString()));
			modelTemp.set("date", cal.getTime());
			modelTemp.set("screeningType", mapProcessId2Name.get(obj[1].toString()));
			modelTemp.set("eventType", mapProcessId2Name.get(obj[1].toString()));
			modelTemp.set("eventLocation", obj[2].toString());
			modelTemp.set("noScreened", obj[3].toString());
			String scrId = obj[4].toString();
			modelTemp.set("scrId", scrId);
			modelTemp.set("volunteers", mapId2Volunteers.get(scrId));
			modelTemp.set("medicalTeam", mapId2Docs.get(scrId));

			results.add(modelTemp);
		}
		return results;
	}

	private List<ExportTableDataDTO> genSchoolScreeningSummary(Map<String, String> mapProcessId2Name)
	{
		Map<String, List<String>> mapId2Docs = null;
		Map<String, List<String>> mapId2Volunteers = null;
		List<ExportTableDataDTO> results = new ArrayList<ExportTableDataDTO>();

		List<Object> lstObjsSchool = (List<Object>) DataUtil
				.executeQuery("select sd.screeningdate as date , pt.processTypeId,sd.address as eventLocation, count(*) as noscreened, sd.schoolscreeningdetailid,d.name from schoolscreeningdetail sd join locality lo on sd.localityid = lo.localityid join schoolscrdet_patdet spd on spd.schscrid = sd.schoolscreeningdetailid join processtype pt on pt.processtypeid = sd.processtypeid join schscrdet_doct ssdd on sd.schoolscreeningdetailid = ssdd.schscrid join doctor d on d.doctorid = ssdd.docid group by sd.screeningdate,pt.processTypeId, sd.address,sd.schoolscreeningdetailid,d.name");
		List<Object> lstVolunteers = (List<Object>) DataUtil
				.executeQuery("select sd.screeningdate as date , pt.processTypeId,sd.address as eventLocation, count(*) as noscreened, sd.schoolscreeningdetailid,v.name from schoolscreeningdetail sd join locality lo on sd.localityid = lo.localityid join schoolscrdet_patdet spd on spd.schscrid = sd.schoolscreeningdetailid join processtype pt on pt.processtypeid = sd.processtypeid join schscrdet_volunt ssdv on sd.schoolscreeningdetailid = ssdv.schscrid join volunteer v on v.volunteerid = ssdv.volid group by sd.screeningdate,pt.processTypeId, sd.address,sd.schoolscreeningdetailid,v.name");

		if (lstObjsSchool != null)
			mapId2Docs = buildMapping(lstObjsSchool);

		if (lstVolunteers != null)
			mapId2Volunteers = buildMapping(lstVolunteers);

		if (lstObjsSchool != null)
			results = extractRowData(mapId2Docs, mapId2Volunteers, mapProcessId2Name, lstObjsSchool);

		return results;
	}

	private List<ExportTableDataDTO> genMedCampSummary(Map<String, String> mapProcessId2Name)
	{
		Map<String, List<String>> mapId2Docs = null;
		Map<String, List<String>> mapId2Volunteers = null;
		List<ExportTableDataDTO> results = new ArrayList<ExportTableDataDTO>();

		List<Object> lstObjsCamp = (List<Object>) DataUtil
				.executeQuery("select cd.screeningdate as date ,  pt.processTypeId , cd.address as eventLocation, count(*) as noscreened, cd.campscreeningdetailid, d.name from campscreeningdetail cd join locality lo on cd.localityid = lo.localityid join campscrdet_patdet cpd on cpd.camscrid = cd.campscreeningdetailid join processtype pt on pt.processtypeid = cd.processtypeid join campscrdet_doct csdd on cd.campscreeningdetailid = csdd.camscrid join doctor d on d.doctorid = csdd.docid group by cd.screeningdate,pt.processTypeId, cd.address,cd.campscreeningdetailid,d.name");
		List<Object> lstVolunteers = (List<Object>) DataUtil
				.executeQuery("select cd.screeningdate as date , pt.processTypeId , cd.address as eventLocation, count(*) as noscreened, cd.campscreeningdetailid, v.name from campscreeningdetail cd join locality lo on cd.localityid = lo.localityid join campscrdet_patdet cpd on cpd.camscrid = cd.campscreeningdetailid join processtype pt on pt.processtypeid = cd.processtypeid join campscrdet_volunt csdv on cd.campscreeningdetailid = csdv.camscrid join volunteer v on v.volunteerid = csdv.volid group by cd.screeningdate,pt.processTypeId, cd.address,cd.campscreeningdetailid, v.name");

		if (lstObjsCamp != null)
			mapId2Docs = buildMapping(lstObjsCamp);

		if (lstVolunteers != null)
			mapId2Volunteers = buildMapping(lstVolunteers);

		if (lstObjsCamp != null)
			results = extractRowData(mapId2Docs, mapId2Volunteers, mapProcessId2Name, lstObjsCamp);

		return results;
	}

	private Map<String, List<String>> buildMapping(List lstObjs1)
	{
		Map<String, List<String>> mapId2Docs = new HashMap<String, List<String>>();
		for (Object object : lstObjs1) // build id to docs mapping
		{
			Object[] obj = (Object[]) object;

			String key = obj[4].toString();
			String value = obj[5].toString();
			if (mapId2Docs.containsKey(key))
			{
				mapId2Docs.get(key).add(value);
			} else
			{
				List<String> lstData = new ArrayList<String>();
				lstData.add(value);
				mapId2Docs.put(key, lstData);
			}
		}
		return mapId2Docs;
	}

	private Map<String, ExportTableDataDTO> buildBreakupSummaryModel(List breakupOfTreatments)
	{
		Map<String, Integer> schRepType2CountTemp = new HashMap<String, Integer>(schRepType2Count);
		Map<String, ExportTableDataDTO> referral2Model = new HashMap<String, ExportTableDataDTO>();
		ExportTableDataDTO modelTemp;
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
				modelTemp = new ExportTableDataDTO();
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
		return referral2Model;
	}
}
