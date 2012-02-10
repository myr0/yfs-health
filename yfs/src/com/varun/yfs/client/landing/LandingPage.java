package com.varun.yfs.client.landing;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.varun.yfs.client.images.YfsImageBundle;

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
		lp.setLayout(new TableLayout(1));

		Image image = new Image(YfsImageBundle.INSTANCE.dfsLogoImage());
		TableData td_cpPart2 = new TableData();
		td_cpPart2.setPadding(5);
		td_cpPart2.setStyle("padding-left: 50%");
		lp.add(image, td_cpPart2);

		Label lblSplash = new Label();
		TableData td_cpPart1 = new TableData();
		td_cpPart1.setPadding(5);
		td_cpPart1.setStyle("padding-left: 10%");
		lblSplash
				.setText("Click on the corresponding accordion panel(s) to your left to create/access Patient Health Data and View Reports");
		lp.add(lblSplash, td_cpPart1);

		setLayout(new FitLayout());

		add(lp);

	}

}
