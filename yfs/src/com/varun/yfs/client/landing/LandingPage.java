package com.varun.yfs.client.landing;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Element;

public class LandingPage extends LayoutContainer
{

	public LandingPage()
	{
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		LayoutContainer lp = new LayoutContainer();

		Label lblSplash = new Label();
		lblSplash.setText("Click on the actions to your left to create new Screening Data or Generate Reports");
		lp.add(lblSplash);
		lp.setLayout(new FlowLayout(50));

		setLayout(new FitLayout());

		add(lp);

	}

}
