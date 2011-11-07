package com.varun.yfs.client.help;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.varun.yfs.client.images.YfsImageBundle;
import com.extjs.gxt.ui.client.widget.Html;

public class HelpPage extends LayoutContainer
{

	public HelpPage()
	{
		setHeight("600");
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		TabPanel tabPanel = new TabPanel();
		tabPanel.setLayoutData(new FitData(5));
		tabPanel.setHeight("600");

		TabItem tbtmScreening = new TabItem("Screening & Referral");
		Image image = new Image(YfsImageBundle.INSTANCE.screeningHelpButtonImage());
		tbtmScreening.add(image);

		Html htmlnewhtmlcomponent = new Html(getHtmComponent());
		tbtmScreening.add(htmlnewhtmlcomponent);
		tabPanel.add(tbtmScreening);
		tbtmScreening.setHeight("600");
		tbtmScreening.setScrollMode(Scroll.AUTOY);

		TabItem tbtmReports = new TabItem("Reports");
		tabPanel.add(tbtmReports);
		tbtmReports.setHeight("600");

		TabItem tbtmAdministration = new TabItem("Administration");

		Image image_1 = new Image(YfsImageBundle.INSTANCE.adminHelpButtonImage());
		tbtmAdministration.add(image_1);
		tabPanel.add(tbtmAdministration);
		tbtmAdministration.setHeight("600");
		add(tabPanel);

	}

	private String getHtmComponent()
	{
		return "<p><b>How to create a new Screening Entry Online?</b></p>" + "<p><i>Open the 'Screening/Referrals' Panel. Click on the 'New' button to create a new Screening Entry Online. Enter the details related to the screening and click on the 'Save' button to complete the operation.</i></p>" + "<p><b>How to edit existing screening information?</b></p>" + "<p><i>To retrieve an existing screening detail- look under the 'chapter' and the' screening date'. Click on the node to access the saved details. Edit the details under them and 'save' when done.</i></p>" + "<p><b>How to create a new Screening Entry Offline?</b></p>" + "<p><i>Open the 'Screening/Referrals'Panel. Click on the 'New' button to create a new Screening Entry Online. Enter the details related to the screening. Use the' import' option available on the grid to the extreme right of the 'Patient Detail' grid to import the patient data. Click on the 'Save' button to complete the operation, if satisfied with the data.</i><b></b></p>"
				+ "<p><b>How to create a new Referral?</b></p>" + "<p><i>To retrieve an existing screening detail- look under the 'chapter' and the' screening date'. Click on the node to access the saved details. Look for the export button, Click on 'export" + "referrals' button. Use the file that is given by the system for further changes and import them back when done.</i></p>" + "<p><b>How to import the patient data after completion of referrals</b>?</p>" + "<p>Import can be done for new screening entries/ referrals. Click on the import button to the extreme right on the 'Patient Detail' grid. In the pop-up dialog that appears select the file that you wish to import." + "After the processing is completed, the preview is shown.</p>" + "<p>Note: If it is a referral import, the Id's of the records must match as given in the export, otherwise records will be overwritten / new records will be created unneccesarily. The software will read the Id's only if it is referral import - In case of 'new screening' the Id's eventhough present in the excel file will be ignored.</p>";

	}
}
