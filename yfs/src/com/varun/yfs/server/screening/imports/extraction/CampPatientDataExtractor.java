package com.varun.yfs.server.screening.imports.extraction;

import java.util.List;

import org.apache.log4j.Logger;

import com.varun.yfs.dto.CampPatientDetailDTO;

public class CampPatientDataExtractor extends SchoolPatientDataExtractor
{
	private static final Logger LOGGER = Logger.getLogger(CampPatientDataExtractor.class);

	public CampPatientDataExtractor(List<String> errorRows)
	{
		super(errorRows);
	}

	@SuppressWarnings("unchecked")
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

		CampPatientDetailDTO patientDetailDTO = new CampPatientDetailDTO();
		// patientDetailDTO.setCaseClosed(lstCols.get(4));
		// patientDetailDTO.setReferral3(lstCols.get());

		patientDetailDTO.setDeleted("N");

		if (!lstCols.get(0).isEmpty() && processIds)
			patientDetailDTO.setId(Long.parseLong(lstCols.get(0)));

		patientDetailDTO.setName(lstCols.get(1));

		String decodeSexColumn = decodeSexColumn(lstCols.get(2));
		patientDetailDTO.setSex(decodeSexColumn);

		patientDetailDTO.setOccupation(lstCols.get(3));
		patientDetailDTO.setAge(lstCols.get(4));
		patientDetailDTO.setAddress(lstCols.get(5));
		patientDetailDTO.setContactNo(lstCols.get(6));

		patientDetailDTO.setHeight(lstCols.get(7));
		patientDetailDTO.setWeight(lstCols.get(8));
		patientDetailDTO.setBloodPressure(lstCols.get(9));

		patientDetailDTO.setFindings(lstCols.get(10));
		patientDetailDTO.setTreatment(lstCols.get(11));

		String decodeReferral = decodeReferral(lstCols.get(12));
		patientDetailDTO.setReferral1(decodeReferral);

		String decodeReferral2 = decodeReferral(lstCols.get(13));
		patientDetailDTO.setReferral2(decodeReferral2);

		String decodeMedicines = decodeYesNo(lstCols.get(14));
		patientDetailDTO.setMedicines(decodeMedicines);

		String decodeEmergency = decodeYesNo(lstCols.get(15));
		patientDetailDTO.setEmergency(decodeEmergency);

		String decodeSurgery = decodeYesNo(lstCols.get(16));
		patientDetailDTO.setSurgeryCase(decodeSurgery);

		String decodeCaseClosed = decodeYesNo(lstCols.get(17));
		patientDetailDTO.setCaseClosed(decodeCaseClosed);

		patientDetailDTO.setReferralUpdates(lstCols.get(18));

		int endErrorCount = errorRows.size();

		if (startErrorCount == endErrorCount)
			lstPatientDetails.add(patientDetailDTO);

		LOGGER.debug(processedRowCount + " -record conversion completed :" + (startErrorCount == endErrorCount));

		errorRows.add(processedRowCount + " - " + errorString.toString());
		processedRowCount += 1;

	}

}
