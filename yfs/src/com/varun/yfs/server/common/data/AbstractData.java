package com.varun.yfs.server.common.data;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;

public abstract class AbstractData
{
	protected <E> E findParent(List<E> lst, E searchSeed)
	{
		int cntIndex = lst.indexOf(searchSeed);
		if (cntIndex < 0)
			return null;
		return lst.get(cntIndex);
	}

	public abstract ModelData getModel();

	public abstract RpcStatusEnum saveModel(ModelData model);
}
