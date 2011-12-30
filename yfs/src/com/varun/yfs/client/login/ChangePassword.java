package com.varun.yfs.client.login;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangePassword extends LayoutContainer
{
	private String userName;

	public ChangePassword(String userName)
	{
		this.userName = userName;
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		FormPanel frmpnlLogin = new FormPanel();
		frmpnlLogin.setButtonAlign(HorizontalAlignment.CENTER);
		frmpnlLogin.setHeaderVisible(false);

		TableData td_cpPart3 = new TableData();
		td_cpPart3.setPadding(5);
		td_cpPart3.setStyle("padding-left: 20%");

		final Status status = new Status();
		frmpnlLogin.add(status, new FormData("100%"));
		status.setIconStyle("icon-wait");
		status.setText("");
		status.setVisible(false);
		add(frmpnlLogin, td_cpPart3);

		final TextField<String> txtfldUserName = new TextField<String>();
		frmpnlLogin.add(txtfldUserName, new FormData("100%"));
		txtfldUserName.setFieldLabel("User Name");
		txtfldUserName.setReadOnly(true);
		txtfldUserName.setValue(this.userName);

		final TextField<String> txtfldPassword = new TextField<String>();
		txtfldPassword.setPassword(true);
		frmpnlLogin.add(txtfldPassword, new FormData("100%"));
		txtfldPassword.setFieldLabel("New Password");

		final Button btnChangePassword = new Button("Change Password");
		btnChangePassword.setIconAlign(IconAlign.RIGHT);
		frmpnlLogin.add(btnChangePassword, new FormData("50%"));
		btnChangePassword.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (txtfldPassword.getValue() == null)
				{
					txtfldPassword.forceInvalid("Password cannot be empty.");
					return;
				}

				status.setVisible(true);
				txtfldPassword.clearInvalid();
				status.setBusy("Attempting password change...");
				LoginService.Util.getInstance().changePassword(userName, txtfldPassword.getValue(), new AsyncCallback<Boolean>()
				{
					@Override
					public void onFailure(Throwable caught)
					{
						status.clearStatus("Error in saving the Password. Please try again.");
					}

					@Override
					public void onSuccess(Boolean result)
					{
						status.clearStatus("Password change successful.");
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
					btnChangePassword.fireEvent(Events.Select);
				}
			}
		});
	}
}
