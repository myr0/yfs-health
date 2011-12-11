package com.varun.yfs.client.reports.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ImageService")
public interface ImageService extends RemoteService
{
	public String getImageToken(String base64image);
}