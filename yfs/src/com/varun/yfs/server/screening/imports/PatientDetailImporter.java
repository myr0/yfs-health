package com.varun.yfs.server.screening.imports;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;

import com.varun.yfs.client.screening.imports.ImportType;
import com.varun.yfs.server.screening.imports.extraction.AbstractPatientDataExtractor;
import com.varun.yfs.server.screening.imports.extraction.CampPatientDataExtractor;
import com.varun.yfs.server.screening.imports.extraction.ClinicPatientDetailExtractor;
import com.varun.yfs.server.screening.imports.extraction.ClinicPatientHistoryExtractor;
import com.varun.yfs.server.screening.imports.extraction.SchoolPatientDataExtractor;
import com.varun.yfs.server.screening.imports.rpc.PatientDataImportServiceImpl;

public class PatientDetailImporter
{
	private static final Logger LOGGER = Logger.getLogger(PatientDetailImporter.class);

	private ArrayBlockingQueue<List<String>> excelRows;
	private List<String> errorRows;

	private AbstractPatientDataExtractor extractor;
	private ImportType importType;

	public PatientDetailImporter(ArrayBlockingQueue<List<String>> excelRows, List<String> errorRows)
	{
		this.excelRows = excelRows;
		this.errorRows = errorRows;
	}

	public void convertRecords(boolean processIds, PatientDataImportServiceImpl patientDataImportServiceImpl)
	{
		LOGGER.debug("Conversion to Patient Detail has Started");
		List<String> lstCols = null;
		while (true)
		{
			try
			{
				lstCols = this.excelRows.take();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			if (lstCols.equals(Collections.EMPTY_LIST))
			{
				LOGGER.debug("Conversion to Patient Detail Completed.");
				patientDataImportServiceImpl.onImportCompleteCallback();
				break;
			}

			extractor.convertToPatientDetailDTO(lstCols, processIds);

			if (extractor.getPatientData().size() > 0)
				LOGGER.debug("Current row processed: "
						+ extractor.getPatientData().get(extractor.getPatientData().size() - 1));
		}
	}

	public int getProcessedRecordsCount()
	{
		return extractor.getProcessedCount();
	}

	@SuppressWarnings("rawtypes")
	public List getProcessedRecords()
	{
		return extractor.getPatientData();
	}

	public void reinit()
	{
		initExtractor();
		extractor.reinit();
	}

	public void setImportType(ImportType importType)
	{
		this.importType = importType;
	}

	private void initExtractor()
	{
		if (importType.equals(ImportType.CAMP))
			extractor = new CampPatientDataExtractor(errorRows);
		else if (importType.equals(ImportType.SCHOOL))
			extractor = new SchoolPatientDataExtractor(errorRows);
		else if (importType.equals(ImportType.CLINICPATIENTDETAIL))
			extractor = new ClinicPatientDetailExtractor(errorRows);
		else if (importType.equals(ImportType.CLINICPATIENTHISTORY))
			extractor = new ClinicPatientHistoryExtractor(errorRows);

	}

	public void clearRecords()
	{
		extractor.clearRecords();
	}

}
