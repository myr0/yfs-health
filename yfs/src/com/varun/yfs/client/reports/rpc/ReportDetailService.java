package com.varun.yfs.client.reports.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ReportDetailService")
public interface ReportDetailService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ReportDetailServiceAsync instance;
		public static ReportDetailServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ReportDetailService.class);
			}
			return instance;
		}
	}
	
	public ModelData getModel(ReportType report, ModelData params);
}
