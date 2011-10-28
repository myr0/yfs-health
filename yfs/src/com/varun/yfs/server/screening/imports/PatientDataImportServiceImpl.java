package com.varun.yfs.server.screening.imports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.screening.imports.PatientDataImportService;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProgressDTO;

public class PatientDataImportServiceImpl extends RemoteServiceServlet implements PatientDataImportService
{
	private static final long serialVersionUID = 152574210934616316L;
	private static Logger logger = Logger.getLogger(PatientDataImportServiceImpl.class);

	private final ArrayBlockingQueue<List<String>> excelRows = new ArrayBlockingQueue<List<String>>(1000);
	private final List<String> errorRows = Collections.synchronizedList(new ArrayList<String>(1000));

	private final ExcelReader converter = new ExcelReader(excelRows, errorRows);
	private Thread excelConverterThread;

	private final PatientDetailImporter patientDetailImporter = new PatientDetailImporter(excelRows, errorRows);
	private Thread patientDetailImporterThread;

	private final ProgressDTO progressDto = new ProgressDTO();
	private RpcStatusEnum status = RpcStatusEnum.COMPLETED;

	@Override
	public String startProcessing(final String path)
	{
		converter.reinit();
		patientDetailImporter.reinit();
		excelRows.clear();
		errorRows.clear();
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
	public ProgressDTO getProgress()
	{
		if (patientDetailImporterThread.isAlive())
			progressDto.setStatus(RpcStatusEnum.RUNNING);
		else
			progressDto.setStatus(status);
		progressDto.setProgress(patientDetailImporter.getProcessedRecordsCount() + "/" + converter.getMaxRecords());
		return progressDto;
	}

	@Override
	public List<PatientDetailDTO> getProcessedRecords()
	{
		return patientDetailImporter.getProcessedRecords();
	}

	@Override
	public List<String> getErrorRecords()
	{
		return errorRows;
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
					String errorMesssage = "Error encountered trying to convert excel rows to file contents." + ex.getMessage();
					logger.error(errorMesssage);
					errorRows.add(errorMesssage);
					status = RpcStatusEnum.FAILURE;
				}
			}
		};
		patientDetailImporterThread = new Thread(runnable);
		patientDetailImporterThread.start();
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
					String errorMessage = "Error encountered trying to read the file contents." + ex.getMessage();
					logger.error(errorMessage);
					errorRows.add(errorMessage);
					status = RpcStatusEnum.FAILURE;
				}
			}
		};
		excelConverterThread = new Thread(runnable);
		excelConverterThread.start();
	}

}
