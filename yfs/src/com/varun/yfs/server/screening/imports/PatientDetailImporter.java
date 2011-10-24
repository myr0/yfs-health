package com.varun.yfs.server.screening.imports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.varun.yfs.dto.PatientDetailDTO;

public class PatientDetailImporter
{
	private static Logger logger = Logger.getLogger(PatientDetailImporter.class);
	private ArrayBlockingQueue<List<String>> excelRows;
	private List<PatientDetailDTO> patientDetails = new ArrayList<PatientDetailDTO>();

	public PatientDetailImporter(ArrayBlockingQueue<List<String>> excelRows)
	{
		this.excelRows = excelRows;
	}

	public void convertRecords()
	{
		logger.debug("Conversion to Patient Detail has Started");
		while (true)
		{
			List<String> lstCols = null;
			try
			{
				lstCols = this.excelRows.take();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			if (lstCols.equals(Collections.EMPTY_LIST))
			{
				logger.debug("Conversion to Patient Detail Completed.");
				break;
			}

			convertToPatientDetailDTO(lstCols);
			logger.debug("Current row processed: " + this.patientDetails.get(this.patientDetails.size() - 1));
		}
	}

	public int getProcessedRecordsCount()
	{
		return patientDetails.size();
	}

	public List<PatientDetailDTO> getProcessedRecords()
	{
		return patientDetails;
	}

	private void convertToPatientDetailDTO(List<String> lstCols)
	{
		PatientDetailDTO patientDetailDTO = new PatientDetailDTO();
		// patientDetailDTO.setCaseClosed(lstCols.get(4));
		// patientDetailDTO.setReferral3(lstCols.get());

		patientDetailDTO.setDeleted("N");
		patientDetailDTO.setName(lstCols.get(1));
		patientDetailDTO.setSex(lstCols.get(2));
		patientDetailDTO.setStandard(lstCols.get(3));
		patientDetailDTO.setAge(lstCols.get(4));
		patientDetailDTO.setAddress(lstCols.get(5));
		patientDetailDTO.setContactNo(lstCols.get(6));
		patientDetailDTO.setHeight(lstCols.get(7));
		patientDetailDTO.setWeight(lstCols.get(8));
		patientDetailDTO.setFindings(lstCols.get(9));
		patientDetailDTO.setTreatment(lstCols.get(10));
		patientDetailDTO.setReferral1(lstCols.get(11));
		patientDetailDTO.setReferral2(lstCols.get(12));
		patientDetailDTO.setEmergency(lstCols.get(13));
		patientDetailDTO.setSurgeryCase(lstCols.get(14));

		patientDetails.add(patientDetailDTO);
	}
}
