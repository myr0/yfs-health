package com.varun.yfs.server.screening.imports.extraction;

import java.util.List;

import org.apache.log4j.Logger;

import com.varun.yfs.dto.CampPatientDetailDTO;

public class CampPatientDataExtractor extends SchoolPatientDataExtractor
{
	private static final Logger LOGGER = Logger.getLogger(CampPatientDataExtractor.class);
	private static final int NO_OF_COLS = 18;

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

		normalize(lstCols, NO_OF_COLS);
		int startErrorCount = errorRows.size();

		CampPatientDetailDTO patientDetailDTO = new CampPatientDetailDTO();

		patientDetailDTO.setDeleted("N");

		try
		{
			if (!lstCols.get(0).isEmpty() && processIds)
				patientDetailDTO.setId(Long.parseLong(lstCols.get(0)));

			patientDetailDTO.setName(lstCols.get(1));

			String decodeSexColumn = decodeSexColumn(lstCols.get(2));
			patientDetailDTO.setSex(decodeSexColumn);

			patientDetailDTO.setOccupation(lstCols.get(3));
			patientDetailDTO.setAge(lstCols.get(4).equalsIgnoreCase("") ? 0 : Double.valueOf(
					Double.parseDouble(lstCols.get(4))).intValue());
			patientDetailDTO.setAddress(lstCols.get(5));
			patientDetailDTO.setContactNo(lstCols.get(6).equalsIgnoreCase("") ? 0 : Integer.parseInt(lstCols.get(6)));

			patientDetailDTO.setHeight(lstCols.get(7).equalsIgnoreCase("") ? 0 : Double.valueOf(
					Double.parseDouble(lstCols.get(7))).intValue());

			patientDetailDTO.setWeight(lstCols.get(8).equalsIgnoreCase("") ? 0 : Double.valueOf(
					Double.parseDouble(lstCols.get(8))).intValue());

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
		} catch (Exception ex)
		{
			LOGGER.debug(processedRowCount + " -record conversion aborted :" + ex);
			errorRows.add(processedRowCount + " - " + errorString.toString());
		}
		int endErrorCount = errorRows.size();

		if (startErrorCount == endErrorCount)
		{
			lstPatientDetails.add(patientDetailDTO);
			LOGGER.debug(processedRowCount + " -record conversion completed.");
		}

		processedRowCount++;

	}

}
