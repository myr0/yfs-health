package com.varun.yfs.server.common.data;

import java.util.List;

public abstract class AbstractData
{
	protected <E> E findParent(List<E> lst, E searchSeed)
	{
		int cntIndex = lst.indexOf(searchSeed);
		if (cntIndex < 0)
			return null;
		return lst.get(cntIndex);
	}
}
