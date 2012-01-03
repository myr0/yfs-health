package com.varun.yfs.dto;

import java.util.List;
import java.util.Map;

import com.varun.yfs.client.util.Util;

public class ExportTableDataDTO extends com.extjs.gxt.ui.client.data.BaseModelData
{
	private static final long serialVersionUID = -730097783798966551L;

	public ExportTableDataDTO()
	{
	}

	@Override
	public String toString()
	{
		List<String> colHeadersTags = get("colHeadersTags");
		String retString = "";
		Map<String, Object> map = getProperties();
		for (String header : colHeadersTags)
		{
			retString += (Util.safeCsvString(map.get(header)) + ",");
		}
		return retString;
	}

}
