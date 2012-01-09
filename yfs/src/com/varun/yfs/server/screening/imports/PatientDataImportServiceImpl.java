package com.varun.yfs.server.screening.imports;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.screening.imports.ImportType;
import com.varun.yfs.client.screening.imports.PatientDataImportService;
import com.varun.yfs.dto.ProgressDTO;

public class PatientDataImportServiceImpl extends RemoteServiceServlet implements PatientDataImportService
{
	private static final long serialVersionUID = 152574210934616316L;
	private static final Logger LOGGER = Logger.getLogger(PatientDataImportServiceImpl.class);

	private final ArrayBlockingQueue<List<String>> excelRows = new ArrayBlockingQueue<List<String>>(1000);
	private final List<String> errorRows = Collections.synchronizedList(new ArrayList<String>(1000));

	private final ExcelReader excelConverter = new ExcelReader(excelRows, errorRows);
	private Thread excelConverterThread;

	private final PatientDetailImporter patientDetailImporter = new PatientDetailImporter(excelRows, errorRows);
	private Thread patientDetailImporterThread;

	private final ProgressDTO progressDto = new ProgressDTO();
	private RpcStatusEnum status = RpcStatusEnum.COMPLETED;

	@Override
	public String startProcessing(ImportType importType, final String path, boolean processIds)
	{
		patientDetailImporter.setImportType(importType);
		reinit();

		String statusMessage = RpcStatusEnum.SUCCESS.name();
		try
		{
			excelConverter.validateFile(path);
		} catch (FileNotFoundException e)
		{
			statusMessage = "The uploaded file was not found. Please retry again.";
			LOGGER.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (InvalidFormatException e)
		{
			statusMessage = "The uploaded file is invalid. Please save the file again as xls/xlsx and retry.";
			LOGGER.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (IllegalArgumentException e)
		{
			statusMessage = "The uploaded file doesn't follow the set template. Please retry the import using the specified template." + e.getMessage();
			LOGGER.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (IOException e)
		{
			statusMessage = "Internal Error has occured that prevents the file from being read/accessed. Details :" + e.getMessage();
			LOGGER.error(statusMessage + e.getMessage());
			return statusMessage;
		}

		LOGGER.debug("Starting the parse/import threads");
		startExcelParserThread(path);
		startPatientDetailImporterThread(processIds);

		return statusMessage;

	}

	@Override
	public ProgressDTO getProgress()
	{
		if (patientDetailImporterThread.isAlive())
			progressDto.setStatus(RpcStatusEnum.RUNNING);
		else
			progressDto.setStatus(status);
		progressDto.setProgress(patientDetailImporter.getProcessedRecordsCount() + "/" + excelConverter.getMaxRecords());
		return progressDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends BaseModelData> getProcessedRecords()
	{
		return patientDetailImporter.getProcessedRecords();
	}

	@Override
	public List<String> getErrorRecords()
	{
		return errorRows;
	}

	private void startPatientDetailImporterThread(final boolean processIds)
	{
		Runnable runnable;
		runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// hack to let the UI init properly
					try
					{
						Thread.sleep(200);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					patientDetailImporter.convertRecords(processIds);
				} catch (Exception ex)
				{
					String errorMesssage = "Error encountered trying to convert excel rows to file contents." + ex.getMessage();
					LOGGER.error(errorMesssage);
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
					excelConverter.readContentsAsCSV(path);
				} catch (Exception ex)
				{
					String errorMessage = "Error encountered trying to read the file contents." + ex.getMessage();
					LOGGER.error(errorMessage);
					errorRows.add(errorMessage);
					status = RpcStatusEnum.FAILURE;
				}
			}
		};
		excelConverterThread = new Thread(runnable);
		excelConverterThread.start();
	}

	private void reinit()
	{
		excelConverter.reinit();
		patientDetailImporter.reinit();
		excelRows.clear();
		errorRows.clear();
	}

}
