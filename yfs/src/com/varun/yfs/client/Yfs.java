package com.varun.yfs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.login.Login;
import com.varun.yfs.client.login.LoginService;
import com.varun.yfs.dto.UserDTO;

public class Yfs implements EntryPoint
{
	public void onModuleLoad()
	{
		String sessionID = Cookies.getCookie("sid");
		if (sessionID == null)
		{
			displayLoginWindow();
		} else
		{
			checkWithServerIfSessionIdIsStillLegal(sessionID);
		}
	}

	private void checkWithServerIfSessionIdIsStillLegal(String sessionID)
	{
		LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserDTO>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				displayLoginWindow();
			}

			@Override
			public void onSuccess(UserDTO result)
			{
				if (result == null)
				{
					displayLoginWindow();
				} else
				{
					if (result.getLoggedIn())
					{
						IndexPage w = new IndexPage(result.getName());
						RootPanel.get().add(w);
					} else
					{
						displayLoginWindow();
					}
				}
			}

		});
	}

	private void displayLoginWindow()
	{
		Login w = new Login();
		RootPanel.get().add(w);
	}
}
