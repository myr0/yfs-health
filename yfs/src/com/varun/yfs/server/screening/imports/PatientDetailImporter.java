package com.varun.yfs.server.screening.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.store.ListStore;
import com.varun.yfs.dto.GenderDTO;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.dto.YesNoDTO;
import com.varun.yfs.server.admin.rpc.ListModelDataEnum;
import com.varun.yfs.server.common.data.DataUtil;

public class PatientDetailImporter
{
	private static Logger logger = Logger.getLogger(PatientDetailImporter.class);

	private int processedRowCount;
	private ArrayBlockingQueue<List<String>> excelRows;
	private List<String> errorRows;
	private List<PatientDetailDTO> patientDetails = new ArrayList<PatientDetailDTO>();
	private List<ReferralTypeDTO> referralTypes = DataUtil.getModelList(ListModelDataEnum.ReferralType.name());
	private StringBuilder errorString = new StringBuilder();

	public PatientDetailImporter(ArrayBlockingQueue<List<String>> excelRows, List<String> errorRows)
	{
		this.excelRows = excelRows;
		this.errorRows = errorRows;
	}

	public void convertRecords(boolean processIds)
	{
		logger.debug("Conversion to Patient Detail has Started");
		while (true)
		{
			List<String> lstCols = null;
			try
			{
				lstCols = this.excelRows.take();
				processedRowCount++;
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if (lstCols.equals(Collections.EMPTY_LIST))
			{
				logger.debug("Conversion to Patient Detail Completed.");
				break;
			}

			convertToPatientDetailDTO(lstCols, processIds);

			if (this.patientDetails.size() > 0)
				logger.debug("Current row processed: " + this.patientDetails.get(this.patientDetails.size() - 1));
		}
	}

	public int getProcessedRecordsCount()
	{
		return processedRowCount;
	}

	public List<PatientDetailDTO> getProcessedRecords()
	{
		return patientDetails;
	}

	private void convertToPatientDetailDTO(List<String> lstCols, boolean processIds)
	{
		logger.debug(processedRowCount + "- starting conversion.");
		errorString.trimToSize();

		if (lstCols.size() < 14)
		{
			logger.debug(processedRowCount + " -record conversion aborted. Insufficient columns in record.");
			return;
		}

		int startErrorCount = errorRows.size();

		PatientDetailDTO patientDetailDTO = new PatientDetailDTO();
		// patientDetailDTO.setCaseClosed(lstCols.get(4));
		// patientDetailDTO.setReferral3(lstCols.get());

		patientDetailDTO.setDeleted("N");

		if (!lstCols.get(0).isEmpty() && processIds)
			patientDetailDTO.setId(Long.parseLong(lstCols.get(0)));

		patientDetailDTO.setName(lstCols.get(1));

		String decodeSexColumn = decodeSexColumn(lstCols.get(2));
		patientDetailDTO.setSex(decodeSexColumn);

		patientDetailDTO.setStandard(lstCols.get(3));
		patientDetailDTO.setAge(lstCols.get(4));
		patientDetailDTO.setAddress(lstCols.get(5));
		patientDetailDTO.setContactNo(lstCols.get(6));
		patientDetailDTO.setHeight(lstCols.get(7));
		patientDetailDTO.setWeight(lstCols.get(8));
		patientDetailDTO.setFindings(lstCols.get(9));
		patientDetailDTO.setTreatment(lstCols.get(10));

		String decodeReferral = decodeReferral(lstCols.get(11));
		patientDetailDTO.setReferral1(decodeReferral);

		String decodeReferral2 = decodeReferral(lstCols.get(12));
		patientDetailDTO.setReferral2(decodeReferral2);

		String decodeEmergency = decodeEmergency(lstCols.get(13));
		patientDetailDTO.setEmergency(decodeEmergency);

		String decodeSurgery = decodeSurgery(lstCols.get(14));
		patientDetailDTO.setSurgeryCase(decodeSurgery);

		int endErrorCount = errorRows.size();

		if (startErrorCount == endErrorCount)
			patientDetails.add(patientDetailDTO);

		logger.debug(processedRowCount + " -record conversion completed :" + (startErrorCount == endErrorCount));

		errorRows.add(processedRowCount + " - " + errorString.toString());
	}

	private String decodeSurgery(String string)
	{
		return decodeEmergency(string);
	}

	private String decodeEmergency(String string)
	{
		if (string == null)
			return null;
		if (string.isEmpty())
			return null;

		String emergency = string.toLowerCase();
		ListStore<YesNoDTO> yesNoDTO = YesNoDTO.getValues();
		for (YesNoDTO yesNoDTO1 : yesNoDTO.getModels())
		{
			if (yesNoDTO1.toString().toLowerCase().equalsIgnoreCase(emergency))
				return yesNoDTO1.toString();
		}
		String errorMessage = " Unable to decode. No matching value for " + string + " found in database.";
		logger.debug("Decode for Emergency/Surgery Column failed. " + errorMessage);
		errorString.append(errorMessage);
		return null;
	}

	private String decodeReferral(String string)
	{
		if (string == null)
			return null;
		if (string.isEmpty())
			return null;

		String referral = string.toLowerCase();

		for (ReferralTypeDTO referralTypeDTO : referralTypes)
		{
			if (referralTypeDTO.toString().equalsIgnoreCase(referral))
			{
				return referralTypeDTO.toString();
			}
		}
		String errorMessage = " Unable to decode. No matching value for " + string + " found in database.";
		logger.debug("Decode for Referral Column failed. " + errorMessage);
		errorString.append(errorMessage);
		return null;
	}

	private String decodeSexColumn(String string)
	{
		if (string == null)
			return null;
		if (string.isEmpty())
			return null;

		String sexColumn = string.toLowerCase();
		ListStore<GenderDTO> gender = GenderDTO.getValues();
		for (GenderDTO gender1 : gender.getModels())
		{
			if (gender1.toString().equalsIgnoreCase(sexColumn))
				return gender1.toString();
		}
		String errorMessage = " Unable to decode. No matching value for " + string + " found in database.";
		logger.debug("Decode for Sex Column failed. " + errorMessage);
		errorString.append(errorMessage);
		return null;
	}

	public void reinit()
	{
		processedRowCount = 0;
		patientDetails.clear();
		this.referralTypes = DataUtil.getModelList(ListModelDataEnum.ReferralType.name());
	}
}
