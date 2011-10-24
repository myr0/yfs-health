package com.varun.yfs.server.screening.imports;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.screening.imports.PatientDataImportService;
import com.varun.yfs.dto.PatientDetailDTO;

public class PatientDataImportServiceImpl extends RemoteServiceServlet implements PatientDataImportService
{
	private static final long serialVersionUID = 152574210934616316L;

	private final ArrayBlockingQueue<List<String>> excelRows = new ArrayBlockingQueue<List<String>>(1000);
	private final ExcelReader converter = new ExcelReader(excelRows);
	private final PatientDetailImporter patientDetailImporter = new PatientDetailImporter(excelRows);

	@Override
	public boolean startProcessing(final String path)
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
					System.out.println("Caught an: " + ex.getClass().getName());
					System.out.println("Message: " + ex.getMessage());
					System.out.println("Stacktrace follows:.....");
					ex.printStackTrace(System.out);
				}
			}
		};
		new Thread(runnable).start();

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
					ex.printStackTrace(System.out);
				}
			}
		};
		new Thread(runnable).start();

		return true;
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
}
