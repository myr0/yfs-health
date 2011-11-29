package com.varun.yfs.server.screening.imports.extraction;

import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.store.ListStore;
import com.varun.yfs.dto.GenderDTO;
import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.dto.SchoolPatientDetailDTO;

public class SchoolPatientDataExtractor extends AbstractPatientDataExtractor
{
	private static final Logger LOGGER = Logger.getLogger(SchoolPatientDataExtractor.class);

	public SchoolPatientDataExtractor(List<String> errorRows)
	{
		super(errorRows);
	}

	@Override
	public void convertToPatientDetailDTO(List<String> lstCols, boolean processIds)
	{
		LOGGER.debug(processedRowCount + "- starting conversion.");
		errorString.trimToSize();

		if (lstCols.size() < 14)
		{
			LOGGER.debug(processedRowCount + " -record conversion aborted. Insufficient columns in record.");
			processedRowCount += 1;
			return;
		}

		int startErrorCount = errorRows.size();

		SchoolPatientDetailDTO patientDetailDTO = new SchoolPatientDetailDTO();
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

		String decodeMedicines = decodeYesNo(lstCols.get(13));
		patientDetailDTO.setMedicines(decodeMedicines);

		String decodeEmergency = decodeYesNo(lstCols.get(14));
		patientDetailDTO.setEmergency(decodeEmergency);

		String decodeSurgery = decodeYesNo(lstCols.get(15));
		patientDetailDTO.setSurgeryCase(decodeSurgery);

		String decodeCaseClosed = decodeYesNo(lstCols.get(16));
		patientDetailDTO.setCaseClosed(decodeCaseClosed);

		patientDetailDTO.setReferralUpdates(lstCols.get(17));

		int endErrorCount = errorRows.size();

		if (startErrorCount == endErrorCount)
			lstPatientDetails.add(patientDetailDTO);

		LOGGER.debug(processedRowCount + " -record conversion completed :" + (startErrorCount == endErrorCount));

		errorRows.add(processedRowCount + " - " + errorString.toString());
		processedRowCount += 1;

	}

	protected String decodeReferral(String string)
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
		LOGGER.debug("Decode for Referral Column failed. " + errorMessage);
		errorString.append(errorMessage);
		return null;
	}

	protected String decodeSexColumn(String string)
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
		LOGGER.debug("Decode for Sex Column failed. " + errorMessage);
		errorString.append(errorMessage);
		return null;
	}

}
