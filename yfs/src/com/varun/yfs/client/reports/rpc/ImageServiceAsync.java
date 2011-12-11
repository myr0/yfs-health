package com.varun.yfs.client.reports.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImageServiceAsync
{
	public void getImageToken(String base64image, AsyncCallback<String> callback);
}