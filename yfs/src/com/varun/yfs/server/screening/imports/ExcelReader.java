package com.varun.yfs.server.screening.imports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/*based on this example : http://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/ss/examples/ToCSV.java*/
public class ExcelReader
{
	private static Logger logger = Logger.getLogger(ExcelReader.class);

	private Workbook workbook = null;
	private DataFormatter formatter = null;
	private FormulaEvaluator evaluator = null;

	private int maxRowWidth = 0;
	private int lastRowNum;
	private int processedRowCount;
	private ArrayBlockingQueue<List<String>> excelRows;

	class ExcelFilenameFilter implements FilenameFilter
	{
		public boolean accept(File file, String name)
		{
			return (name.endsWith(".xls") || name.endsWith(".xlsx"));
		}
	}

	public ExcelReader(ArrayBlockingQueue<List<String>> excelRows)
	{
		this.excelRows = excelRows;
	}

	public void readContentsAsCSV(String strSource) throws FileNotFoundException, IOException, IllegalArgumentException, InvalidFormatException
	{
		this.processedRowCount = 0;
		File source = new File(strSource);

		if (!source.exists())
		{
			throw new IllegalArgumentException("The source for the Excel " + "file(s) cannot be found.");
		}

		if (source.isDirectory())
		{
			// filesList = source.listFiles(new ExcelFilenameFilter());
			throw new IllegalArgumentException("The source for the Excel " + "file(s) seems to be a directory.");
		}

		openWorkbook(source);

		extractContents();
	}

	public int getMaxRecords()
	{
		return this.lastRowNum;
	}

	public int getProcessedCount()
	{
		return this.processedRowCount;
	}

	private void openWorkbook(File file) throws FileNotFoundException, IOException, InvalidFormatException
	{
		FileInputStream fis = null;
		try
		{
			logger.debug("Opening workbook [" + file.getName() + "]");

			fis = new FileInputStream(file);

			this.workbook = WorkbookFactory.create(fis);
			this.evaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
			this.formatter = new DataFormatter();
		} finally
		{
			if (fis != null)
			{
				fis.close();
			}
		}
	}

	private void extractContents()
	{
		Sheet sheet = null;
		Row row = null;

		logger.debug("Converting files contents to CSV string");

		sheet = this.workbook.getSheet("Consolidated");

		if (sheet != null)
		{
			if (sheet.getPhysicalNumberOfRows() > 0)
			{
				lastRowNum = sheet.getLastRowNum();
				for (int j = 0; j <= lastRowNum; j++)
				{
					row = sheet.getRow(j);
					try
					{
						Thread.currentThread().sleep(50);
					} catch (InterruptedException e)
					{
						logger.error("Thread interrupted while attempting to read excel rows.");
						e.printStackTrace();
					}
					this.rowToCSV(row);

					if (j == lastRowNum)
					{
						this.excelRows.add(Collections.EMPTY_LIST);
						logger.debug("Excel Reader encountered EOF.");
					}
				}
			}
		}

	}

	private void rowToCSV(Row row)
	{
		Cell cell = null;
		int lastCellNum = 0;
		ArrayList<String> csvLine = new ArrayList<String>();

		if (row != null)
		{
			lastCellNum = row.getLastCellNum();
			for (int i = 0; i <= lastCellNum; i++)
			{
				cell = row.getCell(i);
				if (cell == null)
				{
					csvLine.add("");
				} else
				{
					if (cell.getCellType() != Cell.CELL_TYPE_FORMULA)
					{
						csvLine.add(this.formatter.formatCellValue(cell));
					} else
					{
						csvLine.add(this.formatter.formatCellValue(cell, this.evaluator));
					}
				}
			}
			if (lastCellNum > this.maxRowWidth)
			{
				this.maxRowWidth = lastCellNum;
			}
		}
		try
		{
			excelRows.put(csvLine);
			processedRowCount++;
		} catch (InterruptedException e)
		{
			logger.error("Thread interrupted while attempting to put in the excel rows to the pipe.");
			e.printStackTrace();
		}
	}

}