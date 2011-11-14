package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.varun.yfs.client.common.RpcStatusEnum;

public class ProgressDTO extends BaseModelData
{

	private static final long serialVersionUID = 6540521956469070280L;
	private RpcStatusEnum status;

	public ProgressDTO()
	{
	}

	public RpcStatusEnum getStatus()
	{
		return status;
	}

	public void setStatus(RpcStatusEnum status)
	{
		set("status", status);
		this.status = status;
	}

	public String getProgress()
	{
		return get("progress");
	}

	public void setProgress(String progress)
	{
		set("progress", progress);
	}

	@Override
	public String toString()
	{
		return getStatus().name();
	}

}
