package com.varun.yfs.dto;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

public class ExportTableDTO extends com.extjs.gxt.ui.client.data.BaseModelData
{
	private static final long serialVersionUID = -2321083646393418489L;
	private List<String> colHeaders;
	private List<String> colHeaderTags;
	private List<? extends ModelData> lstData;
	private List<String> addlData;

	public ExportTableDTO()
	{
	}

	public void setColHeaders(List<String> colHeaders)
	{
		this.colHeaders = colHeaders;
	}

	public List<String> getColHeaders()
	{
		return colHeaders;
	}

	public void setLstData(List<? extends ModelData> lstData)
	{
		this.lstData = lstData;
	}

	public List<? extends ModelData> getLstData()
	{
		return lstData;
	}

	public void setColHeaderTags(List<String> colHeaderTags)
	{
		this.colHeaderTags = colHeaderTags;
		if (lstData != null)
			for (ModelData exp : lstData)
			{
				exp.set("colHeadersTags", colHeaderTags);
			}
	}

	public List<String> getColHeaderTags()
	{
		return colHeaderTags;
	}

	public void setAddlData(List<String> addlData)
	{
		this.addlData = addlData;
	}

	public List<String> getAddlData()
	{
		return addlData;
	}

}
