package com.varun.yfs.server.screening.imports.extraction;

import java.util.List;

import org.apache.log4j.Logger;

import com.varun.yfs.dto.ClinicPatientDetailDTO;

public class ClinicPatientDetailExtractor extends SchoolPatientDataExtractor
{
	private static final Logger LOGGER = Logger.getLogger(ClinicPatientDetailExtractor.class);
	private static final int NO_OF_COLS = 8;
	
	public ClinicPatientDetailExtractor(List<String> errorRows)
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

		ClinicPatientDetailDTO patientDetailDTO = new ClinicPatientDetailDTO();

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

		int endErrorCount = errorRows.size();

		if (startErrorCount == endErrorCount)
			lstPatientDetails.add(patientDetailDTO);

		LOGGER.debug(processedRowCount + " -record conversion completed :" + (startErrorCount == endErrorCount));

		errorRows.add(processedRowCount + " - " + errorString.toString());
		processedRowCount += 1;

	}

}
