package com.varun.yfs.server.screening.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import au.com.bytecode.opencsv.CSVReader;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.varun.yfs.client.screening.export.ExportService;
import com.varun.yfs.dto.ExportTableDTO;
import com.varun.yfs.server.screening.imports.ExcelReader;

public class ExportServiceImpl extends RemoteServiceServlet implements ExportService
{
	private static final long serialVersionUID = -2853390238324491312L;
	private static final Logger LOGGER = Logger.getLogger(ExportServiceImpl.class);
	private static final int START_CELL_NO = 2;
	
	@Override
	public String createExportFile(List<ExportTableDTO> exportTables, String base64Image)
	{
		HSSFWorkbook myWorkBook = new HSSFWorkbook();
		HSSFSheet mySheet = myWorkBook.createSheet(ExcelReader.WORKSHEET_NAME);
		HSSFRow myRow = null;
		HSSFCell myCell = null;
		int rowNum = START_CELL_NO;

		LOGGER.debug("Writing image contents");
		if (base64Image != null)
		{
			int index = myWorkBook.addPicture(Base64.decode(base64Image), Workbook.PICTURE_TYPE_PNG);

			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) START_CELL_NO, rowNum,
					(short) (START_CELL_NO + 5), rowNum + 15);
			HSSFPatriarch patriarch = mySheet.createDrawingPatriarch();
			patriarch.createPicture(anchor, index);
			anchor.setAnchorType(2);

			rowNum = rowNum + 15;
		}

		LOGGER.debug("Writing table data contents");
		for (ExportTableDTO exportTableDTO : exportTables)
		{
			List<String> colHeaders = exportTableDTO.getColHeaders();
			List<String> lstAddlData = exportTableDTO.getAddlData();
			int cellNum = START_CELL_NO;

			if (lstAddlData != null)
			{
				myRow = mySheet.createRow(rowNum++);
				for (String string : lstAddlData)
				{
					myCell = myRow.createCell(START_CELL_NO);
					myCell.setCellValue(string);

					// empty row
					myRow = mySheet.createRow(rowNum++);
				}
				// empty row
				myRow = mySheet.createRow(rowNum++);
			}

			cellNum = START_CELL_NO;
			myRow = mySheet.createRow(rowNum++);
			for (String header : colHeaders)
			{
				myCell = myRow.createCell(cellNum++);
				myCell.setCellValue(header);

				CellStyle style = myWorkBook.createCellStyle();
				style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				myCell.setCellStyle(style);
			}

			List<? extends ModelData> lstData = exportTableDTO.getLstData();
			CSVReader reader;
			for (ModelData e : lstData)
			{
				myRow = mySheet.createRow(rowNum++);
				reader = new CSVReader(new StringReader(e.toString()));
				String[] split = null;
				try
				{
					split = reader.readNext();
					cellNum = START_CELL_NO;
					for (String cell : split)
					{
						myCell = myRow.createCell(cellNum++);
						myCell.setCellValue(cell);

						mySheet.autoSizeColumn(cellNum);
					}
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}

			// empty row
			myRow = mySheet.createRow(rowNum++);

		}
		
		LOGGER.debug("Writing data contents to a file before export.");

		String tmpDir = System.getProperty("java.io.tmpdir");
		String fileName = UUID.randomUUID().toString() + ".xls";
		String filePath = tmpDir + File.separator + fileName;
		try
		{
			FileOutputStream out = new FileOutputStream(filePath);
			myWorkBook.write(out);
			out.close();
			LOGGER.debug("Created export file at location =" + filePath);
		} catch (Exception e)
		{
			LOGGER.error("Export file creation failed." + e.getMessage());
		}

		return filePath;
	}
}
