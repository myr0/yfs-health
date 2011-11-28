package com.varun.yfs.server.screening.imports.extraction;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.server.admin.rpc.ListModelDataEnum;
import com.varun.yfs.server.common.data.DataUtil;

public abstract class AbstractPatientDataExtractor
{
	private static  final Logger LOGGER = Logger.getLogger(AbstractPatientDataExtractor.class);

	protected List<ReferralTypeDTO> referralTypes = DataUtil.getModelList(ListModelDataEnum.ReferralType.name());
	protected List lstPatientDetails = new ArrayList();
	protected StringBuilder errorString = new StringBuilder();
	protected final List<String> errorRows;
	protected int processedRowCount;

	public AbstractPatientDataExtractor(List<String> errorRows)
	{
		this.errorRows = errorRows;
	}

	public List getPatientData()
	{
		return this.lstPatientDetails;
	}

	public int getProcessedCount()
	{
		return this.processedRowCount;
	}

	public void reinit()
	{
		lstPatientDetails.clear();
		this.processedRowCount = 0;
	}

	abstract public void convertToPatientDetailDTO(List<String> lstCols, boolean processIds);

}
