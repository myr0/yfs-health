package com.varun.yfs.client.login;

import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class Login extends LayoutContainer
{

	public Login()
	{
		setLayout(new CenterLayout());

		FormPanel frmpnlLogin = new FormPanel();
		frmpnlLogin.setHeading("Login");

		TextField<String> txtfldUserName = new TextField<String>();
		frmpnlLogin.add(txtfldUserName, new FormData("100%"));
		txtfldUserName.setFieldLabel("User Name");

		TextField<String> txtfldPassword = new TextField<String>();
		txtfldPassword.setPassword(true);
		frmpnlLogin.add(txtfldPassword, new FormData("100%"));
		txtfldPassword.setFieldLabel("Password");

		Button btnLogin = new Button("Login");
		btnLogin.setIconAlign(IconAlign.RIGHT);
		frmpnlLogin.add(btnLogin, new FormData("-145"));
		btnLogin.setSize("100", "22");

		btnLogin.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
			}
		});
		add(frmpnlLogin);
	}

}
