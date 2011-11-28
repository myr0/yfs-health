package com.varun.yfs.server.screening.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import au.com.bytecode.opencsv.CSVReader;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.screening.export.ExportService;
import com.varun.yfs.server.screening.imports.ExcelReader;

public class ExportServiceImpl extends RemoteServiceServlet implements ExportService
{
	private static final long serialVersionUID = -2853390238324491312L;
	private static final Logger LOGGER = Logger.getLogger(ExportServiceImpl.class);

	@Override
	public String createExportFile(List<String> colHeaders, List<? extends ModelData> lstData)
	{
		HSSFWorkbook myWorkBook = new HSSFWorkbook();
		HSSFSheet mySheet = myWorkBook.createSheet(ExcelReader.WORKSHEET_NAME);
		HSSFRow myRow = null;
		HSSFCell myCell = null;

		int rowNum = 0;
		int cellNum = 0;
		myRow = mySheet.createRow(rowNum++);
		for (String header : colHeaders)
		{
			myCell = myRow.createCell(cellNum++);
			myCell.setCellValue(header);

			CellStyle style = myWorkBook.createCellStyle();
			style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			myCell.setCellStyle(style);
			mySheet.autoSizeColumn(cellNum);
		}

		CSVReader reader;
		for (ModelData e : lstData)
		{
			myRow = mySheet.createRow(rowNum++);
			reader = new CSVReader(new StringReader(e.toString()));
			String[] split = null;
			try
			{
				split = reader.readNext();
				cellNum = 0;
				for (String cell : split)
				{
					myCell = myRow.createCell(cellNum++);
					myCell.setCellValue(cell);
				}
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}

		}

		String tmpDir = System.getProperty("java.io.tmpdir");
		String fileName = tmpDir + File.separator + UUID.randomUUID().toString();
		try
		{
			FileOutputStream out = new FileOutputStream(fileName);
			myWorkBook.write(out);
			out.close();
			LOGGER.debug("Created export file at location =" + fileName);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return fileName;
	}
}
