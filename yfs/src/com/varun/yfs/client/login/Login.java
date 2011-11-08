package com.varun.yfs.client.login;

import java.util.Date;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.dto.UserDTO;

public class Login extends LayoutContainer
{

	public Login()
	{
		setLayout(new CenterLayout());
		setSize("600", "600");

		FormPanel frmpnlLogin = new FormPanel();
		frmpnlLogin.setHeading("Login");

		final TextField<String> txtfldUserName = new TextField<String>();
		frmpnlLogin.add(txtfldUserName, new FormData("100%"));
		txtfldUserName.setFieldLabel("User Name");

		final TextField<String> txtfldPassword = new TextField<String>();
		txtfldPassword.setPassword(true);
		frmpnlLogin.add(txtfldPassword, new FormData("100%"));
		txtfldPassword.setFieldLabel("Password");

		Button btnLogin = new Button("Login");
		btnLogin.setIconAlign(IconAlign.RIGHT);
		frmpnlLogin.add(btnLogin, new FormData("50%"));
		btnLogin.setSize("100", "22");

		btnLogin.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				LoginService.Util.getInstance().loginServer(txtfldUserName.getValue(), txtfldPassword.getValue(), new AsyncCallback<UserDTO>()
				{
					@Override
					public void onSuccess(UserDTO result)
					{
						if (result.getLoggedIn())
						{
							RootPanel.get().clear();
							RootPanel.get().add(new IndexPage(result.getName()));

							String sessionID = result.getSessionId();
							final long DURATION = 1000 * 60 * 60 * 24 * 1;
							Date expires = new Date(System.currentTimeMillis() + DURATION);
							Cookies.setCookie("sid", sessionID, expires, null, "/", false);
						} else
						{
							Window.alert("Access Denied. Check your user-name and password.");
						}

					}

					@Override
					public void onFailure(Throwable caught)
					{
						Window.alert("Access Denied. Check your user-name and password.");
					}
				});
			}
		});
		add(frmpnlLogin);
	}
}
