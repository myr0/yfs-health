package com.varun.yfs.server.screening.imports.rpc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.screening.imports.ImportType;
import com.varun.yfs.client.screening.imports.rpc.PatientDataImportService;
import com.varun.yfs.dto.ProgressDTO;
import com.varun.yfs.server.screening.imports.ExcelReader;
import com.varun.yfs.server.screening.imports.FileUploadProgressListener;
import com.varun.yfs.server.screening.imports.PatientDetailImporter;

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

	// private final OnImportCompleteCallback onImportCallback = new
	// OnImportCompleteCallback(status);

	public void onImportCompleteCallback()
	{
		this.status = RpcStatusEnum.COMPLETED;
	}

	class ExcelParserThread implements Runnable
	{
		private String path;
		private PatientDataImportServiceImpl patientDataImportServiceImpl;

		public ExcelParserThread(String path, PatientDataImportServiceImpl patientDataImportServiceImpl)
		{
			this.path = path;
			this.patientDataImportServiceImpl = patientDataImportServiceImpl;
		}

		@Override
		public void run()
		{
			try
			{
				LOGGER.debug("Starting excel parsing.");
				
				status = RpcStatusEnum.RUNNING;
				excelConverter.readContentsAsCSV(path);
				
				LOGGER.debug("Completed excel parse");
			} catch (Exception ex)
			{
				String errorMessage = "Error encountered trying to read the file contents." + ex.getMessage();
				LOGGER.error(errorMessage);
				errorRows.add(errorMessage);
				status = RpcStatusEnum.FAILURE;
			}
		}

	}

	class PatientDetailImportThread implements Runnable
	{
		private boolean processIds;
		private PatientDataImportServiceImpl patientDataImportServiceImpl;

		public PatientDetailImportThread(boolean processIds, PatientDataImportServiceImpl patientDataImportServiceImpl)
		{
			this.processIds = processIds;
			this.patientDataImportServiceImpl = patientDataImportServiceImpl;
		}

		@Override
		public void run()
		{
			try
			{
				LOGGER.debug("Starting patient detail importer");
				
				status = RpcStatusEnum.RUNNING;
				// hack to let the UI init properly
				try
				{
					Thread.sleep(200);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				patientDetailImporter.convertRecords(processIds, patientDataImportServiceImpl);
				
				LOGGER.debug("Completed patient detail import");
			} catch (Exception ex)
			{
				String errorMesssage = "Error encountered trying to convert excel rows to file contents."
						+ ex.getMessage();
				LOGGER.error(errorMesssage);
				errorRows.add(errorMesssage);
				status = RpcStatusEnum.FAILURE;
			}
		}
	}

	@Override
	public String startProcessing(ImportType importType, boolean processIds)
	{
		String statusMessage = RpcStatusEnum.SUCCESS.name();
		patientDetailImporter.setImportType(importType);
		reinit();

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		if (session == null)
		{
			statusMessage = "The uploaded file was not available in the session. Please retry again.";
			LOGGER.error(statusMessage);
			return statusMessage;
		}

		FileUploadProgressListener testProgressListener = (FileUploadProgressListener) session
				.getAttribute("progressListener");
		if (testProgressListener == null)
		{
			statusMessage = "The uploaded file was not found. Please retry again.";
			LOGGER.error(statusMessage);
			return statusMessage;
		}

		try
		{
			excelConverter.validateFile(testProgressListener.getFilePath());
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
			statusMessage = "The uploaded file doesn't follow the set template. Please retry the import using the specified template."
					+ e.getMessage();
			LOGGER.error(statusMessage + e.getMessage());
			return statusMessage;
		} catch (IOException e)
		{
			statusMessage = "Internal Error has occured that prevents the file from being read/accessed. Details :"
					+ e.getMessage();
			LOGGER.error(statusMessage + e.getMessage());
			return statusMessage;
		}

		LOGGER.debug("Starting the parse/import threads");
		startExcelParserThread(testProgressListener.getFilePath());
		startPatientDetailImporterThread(processIds);

		return statusMessage;

	}

	@Override
	public ProgressDTO getProgress()
	{
		progressDto.setStatus(status);

		progressDto
				.setProgress(patientDetailImporter.getProcessedRecordsCount() + "/" + excelConverter.getMaxRecords());
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
		patientDetailImporterThread = new Thread(new PatientDetailImportThread(processIds, this));
		patientDetailImporterThread.start();
	}

	private void startExcelParserThread(final String path)
	{
		excelConverterThread = new Thread(new ExcelParserThread(path, this));
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
