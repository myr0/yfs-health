package com.varun.yfs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.varun.yfs.client.index.IndexPage;

public class Yfs implements EntryPoint
{
	public void onModuleLoad()
	{
		IndexPage w = new IndexPage();
		RootPanel.get().add(w);
	}
}
