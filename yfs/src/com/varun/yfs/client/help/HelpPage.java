package com.varun.yfs.client.help;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.varun.yfs.client.icons.YfsImageBundle;

public class HelpPage extends LayoutContainer
{

	public HelpPage()
	{
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		TabPanel tabPanel = new TabPanel();

		TabItem tbtmScreening = new TabItem("Screening/Referral");

		Image image = new Image(YfsImageBundle.INSTANCE.screeningHelpButtonImage());
		tbtmScreening.add(image);
		tabPanel.add(tbtmScreening);

		TabItem tbtmReports = new TabItem("Reports");
		tabPanel.add(tbtmReports);

		TabItem tbtmAdministration = new TabItem("Administration");
		tabPanel.add(tbtmAdministration);
		add(tabPanel);

	}

}
