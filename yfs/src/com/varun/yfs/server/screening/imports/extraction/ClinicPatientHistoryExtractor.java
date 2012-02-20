package com.varun.yfs.server.screening.imports.extraction;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.varun.yfs.dto.ClinicPatientHistoryDTO;

public class ClinicPatientHistoryExtractor extends SchoolPatientDataExtractor
{
	private final static Calendar CURRENTDATE = Calendar.getInstance();
	private static final Logger LOGGER = Logger.getLogger(ClinicPatientHistoryExtractor.class);

	public ClinicPatientHistoryExtractor(List<String> errorRows)
	{
		super(errorRows);
		CURRENTDATE.setTime(new Date());

		CURRENTDATE.set(Calendar.HOUR_OF_DAY, 0);
		CURRENTDATE.set(Calendar.MINUTE, 0);
		CURRENTDATE.set(Calendar.SECOND, 0);
		CURRENTDATE.set(Calendar.MILLISECOND, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void convertToPatientDetailDTO(List<String> lstCols, boolean processIds)
	{
		LOGGER.debug(processedRowCount + "- starting conversion.");
		errorString.trimToSize();

		if (lstCols.size() < 8)
		{
			LOGGER.debug(processedRowCount + " -record conversion aborted. Insufficient columns in record.");
			processedRowCount += 1;
			return;
		}

		int startErrorCount = errorRows.size();

		ClinicPatientHistoryDTO patientDetailDTO = new ClinicPatientHistoryDTO();

		patientDetailDTO.setDeleted("N");

		if (!lstCols.get(0).isEmpty() && processIds)
			patientDetailDTO.setId(Long.parseLong(lstCols.get(0)));

		patientDetailDTO.setFindings(lstCols.get(1));
		patientDetailDTO.setTreatment(lstCols.get(2));

		patientDetailDTO.setReferral1(decodeReferral(lstCols.get(3)));

		patientDetailDTO.setReferral2(decodeReferral(lstCols.get(4)));

		patientDetailDTO.setMedicines(decodeYesNo(lstCols.get(5)));
		patientDetailDTO.setEmergency(decodeYesNo(lstCols.get(6)));
		patientDetailDTO.setSurgeryCase(decodeYesNo(lstCols.get(7)));

		patientDetailDTO.setCaseClosed(decodeYesNo(lstCols.get(8)));

		patientDetailDTO.setScreeningDate(CURRENTDATE.getTimeInMillis());

		int endErrorCount = errorRows.size();

		if (startErrorCount == endErrorCount)
			lstPatientDetails.add(patientDetailDTO);

		LOGGER.debug(processedRowCount + " -record conversion completed :" + (startErrorCount == endErrorCount));

		errorRows.add(processedRowCount + " - " + errorString.toString());
		processedRowCount += 1;

	}

}
