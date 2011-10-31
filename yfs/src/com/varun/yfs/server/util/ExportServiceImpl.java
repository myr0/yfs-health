package com.varun.yfs.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.util.ExportService;

public class ExportServiceImpl extends RemoteServiceServlet implements ExportService
{
	private static final long serialVersionUID = -2853390238324491312L;
	private static Logger logger = Logger.getLogger(ExportServiceImpl.class);

	@Override
	public String createExportFile(List<String> colHeaders, List<? extends ModelData> lstData)
	{
		HSSFWorkbook myWorkBook = new HSSFWorkbook();
		HSSFSheet mySheet = myWorkBook.createSheet();
		HSSFRow myRow = null;
		HSSFCell myCell = null;

		int row = 0;
		for (ModelData e : lstData)
		{
			myRow = mySheet.createRow(row++);
			String[] split = e.toString().split(",");

			int cellNum = 0;
			for (String cell : split)
			{
				myCell = myRow.createCell(cellNum++);
				myCell.setCellValue(cell);
			}
		}

		String tmpDir = System.getProperty("java.io.tmpdir");
		String fileName = tmpDir + File.separator + UUID.randomUUID().toString();
		try
		{
			FileOutputStream out = new FileOutputStream(fileName);
			myWorkBook.write(out);
			out.close();
			logger.debug("Created export file at location =" + fileName);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return fileName;
	}
}
