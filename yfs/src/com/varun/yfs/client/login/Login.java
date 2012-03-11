package com.varun.yfs.client.login;

import java.util.Date;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.custom.ThemeSelector;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.login.rpc.LoginService;
import com.varun.yfs.dto.UserDTO;

public class Login extends LayoutContainer
{

	public Login()
	{
		setLayout(new TableLayout(2));

		Image yfsLogo = new Image();
		yfsLogo.setResource(YfsImageBundle.INSTANCE.yfsLogoImage());
		TableData td_cpPart1 = new TableData();
		td_cpPart1.setPadding(20);
		td_cpPart1.setColspan(2);
		td_cpPart1.setStyle("padding-left: 50%");
		add(yfsLogo, td_cpPart1);
		yfsLogo.setHeight("300");

		Image dfsLogo = new Image(YfsImageBundle.INSTANCE.dfsLogoImage());
		TableData td_cpPart2 = new TableData();
		td_cpPart2.setPadding(5);
		td_cpPart2.setStyle("padding-left: 30%");
		add(dfsLogo, td_cpPart2);
		dfsLogo.setWidth("300");

		final FormPanel frmpnlLogin = new FormPanel();
		frmpnlLogin.setLabelAlign(LabelAlign.RIGHT);
		frmpnlLogin.setLabelWidth(110);
		frmpnlLogin.setButtonAlign(HorizontalAlignment.CENTER);
		frmpnlLogin.setHeading("Patient Management System");
		frmpnlLogin.setWidth("300px");

		TableData td_cpPart3 = new TableData();
		td_cpPart3.setPadding(5);
		td_cpPart3.setStyle("padding-left: 20%");
		add(frmpnlLogin, td_cpPart3);

		final TextField<String> txtfldUserName = new TextField<String>();
		frmpnlLogin.add(txtfldUserName, new FormData("100%"));
		txtfldUserName.setFieldLabel("User Name");
		txtfldUserName.setWidth(125);

		final TextField<String> txtfldPassword = new TextField<String>();
		txtfldPassword.setPassword(true);
		frmpnlLogin.add(txtfldPassword, new FormData("100%"));
		txtfldPassword.setFieldLabel("Password");
		txtfldPassword.setWidth(125);

		ThemeSelector selector = new ThemeSelector();
		selector.setFieldLabel("Select a Theme");
		selector.setWidth(125);
		frmpnlLogin.add(selector, new FormData("100%"));

		final Button btnLogin = new Button("Login");
		frmpnlLogin.setButtonAlign(HorizontalAlignment.RIGHT);
		frmpnlLogin.addButton(btnLogin);

		btnLogin.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				frmpnlLogin.mask("Authenticating..");
				if (txtfldUserName.getValue() == null)
				{
					txtfldUserName.forceInvalid("User Name cannot be empty.");
					frmpnlLogin.unmask();
					return;
				}

				if (txtfldPassword.getValue() == null)
				{
					txtfldPassword.forceInvalid("Password cannot be empty.");
					frmpnlLogin.unmask();
					return;
				}
				
				frmpnlLogin.mask("Loading..");
				LoginService.Util.getInstance().loginServer(txtfldUserName.getValue(), txtfldPassword.getValue(),
						new AsyncCallback<UserDTO>()
						{
							@Override
							public void onSuccess(UserDTO result)
							{
								if (result.getLoggedIn())
								{
									RootPanel.get().clear();
									RootPanel.get().add(new IndexPage(result));

									String sessionID = result.getSessionId();
									final long DURATION = 1000 * 60 * 60 * 24 * 1;
									Date expires = new Date(System.currentTimeMillis() + DURATION);
									Cookies.setCookie("sid", sessionID, expires, null, "/", false);
									frmpnlLogin.unmask();
								} else
								{
									Window.alert("Access Denied. Check your user-name and password.");
									frmpnlLogin.unmask();
								}

							}

							@Override
							public void onFailure(Throwable caught)
							{
								Window.alert("Access Denied. Check your user-name and password.");
								frmpnlLogin.unmask();
							}
						});
			}
		});

		txtfldPassword.addKeyListener(new KeyListener()
		{
			@Override
			public void componentKeyPress(ComponentEvent event)
			{
				super.componentKeyPress(event);
				if (event.getKeyCode() == KeyCodes.KEY_ENTER)
				{
					btnLogin.fireEvent(Events.Select);
				}
			}
		});

	}
}
