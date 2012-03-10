package com.varun.yfs.client.screening.imports.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;

public interface FileUploadProgressServiceAsync
{
	void getStatus(AsyncCallback<RpcStatusEnum> callback);
}
