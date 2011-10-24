package com.varun.yfs.server.screening.imports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.screening.imports.PatientDataImportService;
import com.varun.yfs.dto.PatientDetailDTO;

public class PatientDataImportServiceImpl extends RemoteServiceServlet implements PatientDataImportService
{
	private static final long serialVersionUID = 152574210934616316L;
	private static Logger logger = Logger.getLogger(PatientDataImportServiceImpl.class);

	private final ArrayBlockingQueue<List<String>> excelRows = new ArrayBlockingQueue<List<String>>(1000);
	private final ExcelReader converter = new ExcelReader(excelRows);
	private final PatientDetailImporter patientDetailImporter = new PatientDetailImporter(excelRows);

	@Override
	public String startProcessing(final String path)
	{
		String statusMessage = RpcStatusEnum.SUCCESS.name();
		try
		{
			converter.validateFile(path);
		} catch (FileNotFoundException e)
		{
			statusMessage = "The uploaded file was not found. Please retry again.";
			logger.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (InvalidFormatException e)
		{
			statusMessage = "The uploaded file is invalid. Please save the file again as xls/xlsx and retry.";
			logger.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (IllegalArgumentException e)
		{
			statusMessage = "The uploaded file doesn't follow the set template. Please retry the import using the specified template." + e.getMessage();
			logger.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (IOException e)
		{
			statusMessage = "Internal Error has occured that prevents the file from being read/accessed. Details :" + e.getMessage();
			logger.error(statusMessage + e.getMessage());
			return statusMessage;
		}

		logger.debug("Starting the parse/import threads");
		startExcelParserThread(path);
		startPatientDetailImporterThread();

		return statusMessage;

	}

	@Override
	public String getProgress()
	{
		return patientDetailImporter.getProcessedRecordsCount() + "/" + converter.getMaxRecords();
	}

	@Override
	public List<PatientDetailDTO> getProcessedRecords()
	{
		return patientDetailImporter.getProcessedRecords();
	}

	private void startPatientDetailImporterThread()
	{
		Runnable runnable;
		runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					patientDetailImporter.convertRecords();
				} catch (Exception ex)
				{
					logger.error("Error encountered trying to convert excel rows to file contents." + ex.getMessage());
				}
			}
		};
		new Thread(runnable).start();
	}

	private void startExcelParserThread(final String path)
	{
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					converter.readContentsAsCSV(path);
				} catch (Exception ex)
				{
					logger.error("Error encountered trying to read the file contents." + ex.getMessage());
				}
			}
		};
		new Thread(runnable).start();
	}

}
