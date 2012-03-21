package com.varun.yfs.server.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
	private static final Logger LOGGER = Logger.getLogger(ReportDetailServiceImpl.class);

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
		LOGGER.debug("Beginning report creation.");

		ModelData model = new BaseModelData();
		try
		{
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
		} catch (Exception ex)
		{
			LOGGER.debug("Encountered error generating report." + ex);
		}

		LOGGER.info("Report creation completed.");
		return model;

	}

	@SuppressWarnings("rawtypes")
	private void generateSchoolScreeningReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");

		model.set(
				"locationsCount",
				DataUtil.executeQuery("select count(*) from SchoolScreeningDetail sd join Locality lo on sd.localityId = lo.localityId join SchoolScrDet_PatDet spd on spd.schScrId = sd.schoolScreeningDetailId where sd.screeningDate >= "
						+ fromDate + " and sd.screeningDate <= " + toDate));
		model.set(
				"locationsList",
				DataUtil.executeQuery("select distinct ld.localityName from SchoolScreeningDetail sd join Locality ld on sd.localityId = ld.localityId where sd.screeningDate >= "
						+ fromDate + " and sd.screeningDate <= " + toDate));
		List breakupOfTreatments = (List) DataUtil
				.executeQuery("select t.referral1 as referral, sum(t.cnt) as screened, "
						+ " t.medicines, "
						+ " t.caseClosed,"
						+ " t.surgeryCase, t.screeningDate "
						+ "from "
						+ "( "
						+ "select spd.referral1,count(spd.referral1) as cnt, spd.medicines, spd.caseClosed, "
						+ "spd.surgeryCase,spd.emergency, ssd.screeningDate "
						+ "from SchoolPatientDetail spd join ReferralType rt on spd.referral1 = rt.name "
						+ "join SchoolScrDet_Patdet ssdpd on ssdpd.patId = spd.schPatDetId "
						+ "join SchoolScreeningDetail ssd on ssd.schoolScreeningDetailId = ssdpd.schScrid "
						+ "group by referral1, medicines, caseClosed, surgeryCase, emergency, ssd.screeningDate "
						+ "union all "
						+ "select spd.referral2, count(spd.referral2) as cnt, spd.medicines, spd.caseClosed, "
						+ "spd.surgeryCase, spd.emergency, ssd.screeningDate "
						+ "from SchoolPatientDetail spd join ReferralType rt on spd.referral2 = rt.name "
						+ "join SchoolScrDet_PatDet ssdpd on ssdpd.patId = spd.SchPatDetId "
						+ "join SchoolScreeningDetail ssd on ssd.schoolScreeningDetailId = ssdpd.schScrid "
						+ "group by referral2, medicines, caseClosed, surgeryCase, emergency, ssd.screeningDate "
						+ "union all select spd.referral2,count(*) as snt, spd.medicines,spd.caseClosed, spd.surgeryCase, "
						+ "spd.emergency, ssd.screeningDate "
						+ "from SchoolPatientDetail spd join SchoolScrDet_PatDet ssdpd on ssdpd.patId = spd.schPatDetId "
						+ "join SchoolScreeningDetail ssd on ssd.schoolScreeningDetailId = ssdpd.schScrid "
						+ "where spd.referral1 is null and spd.referral2 is null "
						+ "group by referral2, medicines, caseClosed, surgeryCase, emergency, ssd.screeningDate "
						+ " ) t "
						+ "where t.screeningDate >="
						+ fromDate
						+ " and t.screeningDate <= "
						+ toDate
						+ " "
						+ "group by t.referral1, t.medicines, t.caseClosed, t.surgeryCase, t.emergency, t.screeningDate");

		Map<String, ExportTableDataDTO> referral2Model = buildBreakupSummaryModel(breakupOfTreatments);

		model.set("breakupOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
		model.set("statusOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
	}

	@SuppressWarnings("rawtypes")
	private void generateMedicalCampReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");

		model.set(
				"locationsCount",
				DataUtil.executeQuery("select count(*) from CampScreeningDetail cd join Locality lo on cd.localityId = lo.localityId join CampScrDet_PatDet cpd on cpd.camScrId = cd.campScreeningDetailId where cd.screeningDate >= "
						+ fromDate + " and cd.screeningDate <= " + toDate));
		model.set(
				"locationsList",
				DataUtil.executeQuery("select distinct ld.localityName from CampScreeningDetail cd join Locality ld on cd.localityId = ld.localityId where cd.screeningDate >= "
						+ fromDate + " and cd.screeningDate <= " + toDate));
		List breakupOfTreatments = (List) DataUtil
				.executeQuery("select t.referral1, sum(t.cnt), "
						+ " t.medicines,"
						+ " t.caseClosed, "
						+ " t.surgeryCase, "
						+ " t.screeningDate from ( "
						+ " select cpd.referral1,count(cpd.referral1) as cnt, cpd.medicines, "
						+ " cpd.caseClosed, cpd.surgeryCase, cpd.emergency, csd.screeningDate "
						+ " from CampPatientDetail cpd join ReferralType rt on cpd.referral1 = rt.name "
						+ " join CampScrDet_PatDet csdpd on csdpd.patId = cpd.camPatDetId "
						+ " join CampScreeningdetail csd on csd.campScreeningDetailId = csdpd.camScrId "
						+ " group by referral1, medicines, caseClosed, surgeryCase, emergency, csd.screeningDate "
						+ " union all "
						+ " select cpd.referral2, count(cpd.referral2) as cnt, cpd.medicines, cpd.caseClosed, "
						+ " cpd.surgeryCase, cpd.emergency, csd.screeningDate "
						+ " from CampPatientDetail cpd join ReferralType rt on cpd.referral2 = rt.name"
						+ " join CampScrDet_PatDet csdpd on csdpd.patId = cpd.CamPatDetId join "
						+ " CampScreeningDetail csd on csd.campScreeningDetailId = csdpd.camScrId "
						+ " group by referral2, medicines, caseClosed, surgeryCase, emergency, csd.screeningDate "
						+ " union all "
						+ " select cpd.referral2,count(*) as cnt, cpd.medicines, cpd.caseClosed, "
						+ " cpd.surgeryCase, cpd.emergency, csd.screeningDate from CampPatientDetail cpd "
						+ " join CampScrDet_PatDet csdpd on csdpd.patId = cpd.camPatDetId "
						+ " join CampScreeningDetail csd on csd.campScreeningDetailId = csdpd.camScrId where cpd.referral1 is null and cpd.referral2 is null "
						+ " group by referral2, medicines, caseClosed, surgeryCase, emergency , csd.screeningDate ) t "
						+ " where t.screeningDate >="
						+ fromDate
						+ " and t.screeningDate <="
						+ toDate
						+ " group by t.referral1, t.medicines, t.caseClosed, t.surgeryCase, t.emergency, t.screeningDate");

		Map<String, ExportTableDataDTO> referral2Model = buildBreakupSummaryModel(breakupOfTreatments);

		model.set("breakupOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
		model.set("statusOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
	}

	@SuppressWarnings("unchecked")
	private void generateEventsReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");

		List<Object> processTypes = (List<Object>) DataUtil
				.executeQuery("select processTypeId, name from ProcessType p where p.deleted = 'N'");
		Map<String, String> mapProcessId2Name = new HashMap<String, String>();
		for (Object object : processTypes)
		{
			Object[] row = (Object[]) object;
			mapProcessId2Name.put(row[0].toString(), row[1].toString());
		}

		List<ExportTableDataDTO> results = genMedCampSummary(mapProcessId2Name, fromDate, toDate);

		results.addAll(genSchoolScreeningSummary(mapProcessId2Name, fromDate, toDate));

		model.set("eventsInfo", results);
	}

	@SuppressWarnings("rawtypes")
	private void generateClinicReport(ModelData params, ModelData model)
	{
		Long fromDate = params.get("dateFrom");
		Long toDate = params.get("dateTo");
		Object clinicId = params.get("clinicId");
		model.set(
				"locationsCount",
				DataUtil.executeQuery("select count(*) from ClinicPatientDetail cpd join CliPatDet_CliPatHis cpdcph on cpd.cliPatDetId = cpdcph.CliPatDetId join ClinicPatientHistory cph on cph.cliPatHisId = cpdcph.cliPatHisId where cpd.clinicid ="
						+ clinicId + " and cph.screeningDate >= " + fromDate + " and cph.screeningDate <= " + toDate));

		List breakupOfTreatments = (List) DataUtil
				.executeQuery("select t.referral,sum(t.count1) as screened, t.medicines, t.caseClosed, t.surgeryCase, t.screeningDate from ( select cph.referral1 as referral, count(cph.referral1) as count1, cph.medicines, cph.caseClosed, cph.surgeryCase, cph.screeningDate from ClinicPatientDetail cpd join CliPatDet_CliPatHis cpdcph on cpd.cliPatDetId = cpdcph.cliPatDetId join ClinicPatientHistory cph on cph.cliPatHisId = cpdcph.cliPatHisId where cpd.clinicid = "
						+ clinicId
						+ " and cph.referral1 is not null group by cph.referral1,cph.medicines, cph.caseClosed, cph.surgeryCase , cph.screeningDate union select cph.referral2 as referral, count(cph.referral2) as count1, cph.medicines, cph.caseClosed, cph.surgeryCase , cph.screeningDate from ClinicPatientDetail cpd join CliPatDet_CliPatHis cpdcph on cpd.cliPatDetId = cpdcph.cliPatDetId join ClinicPatientHistory cph on cph.cliPatHisId = cpdcph.cliPatHisId where cpd.clinicid ="
						+ clinicId
						+ " and cph.referral2 is not null group by cph.referral2,cph.medicines, cph.caseClosed, cph.surgeryCase, cph.screeningDate union select cph.referral2 as referral, count(*) as count1, cph.medicines, cph.caseClosed, cph.surgeryCase , cph.screeningDate from ClinicPatientDetail cpd join CliPatDet_CliPatHis cpdcph on cpd.cliPatDetId = cpdcph.cliPatDetId join ClinicPatientHistory cph on cph.cliPatHisId = cpdcph.cliPatHisId where cpd.clinicid ="
						+ clinicId
						+ " and cph.referral2 is null and cph.referral1 is null group by cph.referral2,cph.medicines, cph.caseClosed, cph.surgeryCase, cph.screeningDate ) t  where t.screeningDate >= "
						+ fromDate
						+ " and t.screeningDate <= "
						+ toDate
						+ " group by t.referral, t.medicines, t.caseClosed, t.surgeryCase , t.screeningDate");

		Map<String, ExportTableDataDTO> referral2Model = buildBreakupSummaryModel(breakupOfTreatments);

		model.set("breakupOfTreatments", new ArrayList<ExportTableDataDTO>(referral2Model.values()));
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

	@SuppressWarnings("unchecked")
	private List<ExportTableDataDTO> genSchoolScreeningSummary(Map<String, String> mapProcessId2Name, Long fromDate,
			Long toDate)
	{
		Map<String, List<String>> mapId2Docs = null;
		Map<String, List<String>> mapId2Volunteers = null;
		List<ExportTableDataDTO> results = new ArrayList<ExportTableDataDTO>();

		List<Object> lstObjsSchool = (List<Object>) DataUtil
				.executeQuery("select sd.screeningDate as date , pt.processTypeId,sd.address as eventLocation, count(*) as noscreened, sd.schoolScreeningDetailId,d.name from SchoolScreeningDetail sd join Locality lo on sd.localityId = lo.localityId join SchoolScrDet_PatDet spd on spd.schScrId = sd.schoolScreeningDetailId join ProcessType pt on pt.processTypeId = sd.processTypeId join SchScrDet_Doct ssdd on sd.schoolScreeningDetailId = ssdd.schScrId join Doctor d on d.doctorId = ssdd.docId where sd.screeningDate >= "
						+ fromDate
						+ " and sd.screeningDate <= "
						+ toDate
						+ " group by sd.screeningDate,pt.processTypeId, sd.address,sd.schoolScreeningDetailId,d.name");
		List<Object> lstVolunteers = (List<Object>) DataUtil
				.executeQuery("select sd.screeningDate as date , pt.processTypeId,sd.address as eventLocation, count(*) as noscreened, sd.schoolScreeningDetailId,v.name from SchoolScreeningDetail sd join Locality lo on sd.localityId = lo.localityId join SchoolScrDet_PatDet spd on spd.schScrId = sd.schoolScreeningDetailId join ProcessType pt on pt.processTypeId = sd.processTypeId join SchScrDet_Volunt ssdv on sd.schoolScreeningDetailId = ssdv.schScrId join Volunteer v on v.volunteerId = ssdv.volId where sd.screeningDate >= "
						+ fromDate
						+ " and sd.screeningDate <= "
						+ toDate
						+ " group by sd.screeningDate,pt.processTypeId, sd.address,sd.schoolScreeningDetailId,v.name");

		if (lstObjsSchool != null)
			mapId2Docs = buildMapping(lstObjsSchool);

		if (lstVolunteers != null)
			mapId2Volunteers = buildMapping(lstVolunteers);

		if (lstObjsSchool != null)
			results = extractRowData(mapId2Docs, mapId2Volunteers, mapProcessId2Name, lstObjsSchool);

		return results;
	}

	@SuppressWarnings("unchecked")
	private List<ExportTableDataDTO> genMedCampSummary(Map<String, String> mapProcessId2Name, Long fromDate, Long toDate)
	{
		Map<String, List<String>> mapId2Docs = null;
		Map<String, List<String>> mapId2Volunteers = null;
		List<ExportTableDataDTO> results = new ArrayList<ExportTableDataDTO>();

		List<Object> lstObjsCamp = (List<Object>) DataUtil
				.executeQuery("select cd.screeningDate as date ,  pt.processTypeId , cd.address as eventLocation, count(*) as noscreened, cd.campScreeningDetailId, d.name from CampScreeningDetail cd join Locality lo on cd.localityId = lo.localityId join CampScrDet_PatDet cpd on cpd.camScrId = cd.campScreeningDetailId join ProcessType pt on pt.processTypeId = cd.processTypeId join CampScrDet_Doct csdd on cd.campScreeningDetailId = csdd.camScrId join Doctor d on d.doctorId = csdd.docId where cd.screeningDate >= "
						+ fromDate
						+ " and cd.screeningDate <= "
						+ toDate
						+ "  group by cd.screeningDate,pt.processTypeId, cd.address,cd.campScreeningDetailId,d.name");
		List<Object> lstVolunteers = (List<Object>) DataUtil
				.executeQuery("select cd.screeningDate as date , pt.processTypeId , cd.address as eventLocation, count(*) as noscreened, cd.campScreeningDetailId, v.name from CampScreeningDetail cd join Locality lo on cd.localityId = lo.localityId join CampScrDet_PatDet cpd on cpd.camScrId = cd.campScreeningDetailId join ProcessType pt on pt.processTypeId = cd.processTypeId join CampScrDet_Volunt csdv on cd.campScreeningDetailId = csdv.camScrId join Volunteer v on v.volunteerId = csdv.volId where cd.screeningDate >= "
						+ fromDate
						+ " and cd.screeningDate <= "
						+ toDate
						+ " group by cd.screeningDate,pt.processTypeId, cd.address,cd.campScreeningDetailId, v.name");

		if (lstObjsCamp != null)
			mapId2Docs = buildMapping(lstObjsCamp);

		if (lstVolunteers != null)
			mapId2Volunteers = buildMapping(lstVolunteers);

		if (lstObjsCamp != null)
			results = extractRowData(mapId2Docs, mapId2Volunteers, mapProcessId2Name, lstObjsCamp);

		return results;
	}

	@SuppressWarnings("rawtypes")
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

	@SuppressWarnings("rawtypes")
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

				BigDecimal count = (BigDecimal) obj[1];
				Integer screenedCnt = modelTemp.get("screened");
				modelTemp.set("screened", count.intValue() + screenedCnt);
			} else
			{
				modelTemp = new ExportTableDataDTO();
				modelTemp.set("breakUpOfTreatment", referralType);
				modelTemp.set("screened", ((BigDecimal) obj[1]).intValue());
				referral2Model.put(referralType, modelTemp);
			}

			String medicines = String.valueOf(obj[2]).equalsIgnoreCase("YES") ? "1" : "0";
			String caseClosed = String.valueOf(obj[3]).equalsIgnoreCase("YES") ? "1" : "0";
			String surgeryCase = String.valueOf(obj[4]).equalsIgnoreCase("YES") ? "1" : "0";
			String key2RepType = medicines.concat(caseClosed).concat(surgeryCase); // 000

			String repColType = schRepCol2Type.get(key2RepType); // colType
			Integer screenedCnt = schRepType2CountTemp.get(repColType); // cur-type
			BigDecimal count = (BigDecimal) obj[1]; // act count

			modelTemp.set(repColType, count.intValue() + screenedCnt);

		}
		return referral2Model;
	}
}
