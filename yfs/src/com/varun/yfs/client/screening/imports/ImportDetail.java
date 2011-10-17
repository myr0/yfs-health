package com.varun.yfs.client.screening.imports;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.varun.yfs.client.index.IndexPage;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.TableData;

public class ImportDetail extends LayoutContainer
{
	private FlowPanel panelImages = new FlowPanel();

	public ImportDetail()
	{
		setSize("600", "575");
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		ContentPanel mainContainerPanel = new ContentPanel();
		mainContainerPanel.setHeading("Import Screening Data");
		add(mainContainerPanel);
		mainContainerPanel.setSize("600px", "");

		LayoutContainer lcUploadComponent = buildUploadComponent();
		LayoutContainer lcTemplateComponent = buildTemplateComponent();

		mainContainerPanel.add(lcUploadComponent, new FitData(5));
		lcTemplateComponent_1.setLayout(new FitLayout());
		mainContainerPanel.add(lcTemplateComponent, new FitData(5));
		lcTemplateComponent_1.setSize("600", "");
		add(mainContainerPanel);
	}

	private LayoutContainer buildTemplateComponent()
	{
		lcTemplateComponent_1 = new LayoutContainer();

		ContentPanel mainTemplateSettingsPanel = new ContentPanel();
		mainTemplateSettingsPanel.setHeading("Template Settings");
		mainTemplateSettingsPanel.setLayout(new TableLayout(3));

		FormPanel templateSettings1 = new FormPanel();
		templateSettings1.setLayout(new FormLayout());
		templateSettings1.setHeaderVisible(false);
		templateSettings1.setHeading("New FormPanel");
		templateSettings1.setSize("200", "330");

		NumberField firstDataRow = new NumberField();
		firstDataRow.setMaxLength(2);
		templateSettings1.add(firstDataRow, new FormData("100%"));
		firstDataRow.setFieldLabel("First Data Row");

		NumberField nameColumn = new NumberField();
		nameColumn.setMaxLength(2);
		templateSettings1.add(nameColumn, new FormData("100%"));
		nameColumn.setFieldLabel("Name Column");

		NumberField ageColumn = new NumberField();
		ageColumn.setMaxLength(2);
		ageColumn.setFieldLabel("Age Column");
		templateSettings1.add(ageColumn, new FormData("50%"));

		NumberField sexColumn = new NumberField();
		sexColumn.setFieldLabel("Sex Column");
		templateSettings1.add(ageColumn, new FormData("100%"));

		NumberField standardColumn = new NumberField();
		standardColumn.setMaxLength(2);
		standardColumn.setFieldLabel("Standard Column");
		templateSettings1.add(standardColumn, new FormData("100%"));

		NumberField heightColumn = new NumberField();
		heightColumn.setMaxLength(2);
		heightColumn.setFieldLabel("Height Column");
		templateSettings1.add(heightColumn, new FormData("100%"));

		NumberField weightColumn = new NumberField();
		weightColumn.setMaxLength(2);
		weightColumn.setFieldLabel("Weight Column");
		templateSettings1.add(weightColumn, new FormData("100%"));

		NumberField addressColumn = new NumberField();
		addressColumn.setMaxLength(2);
		addressColumn.setFieldLabel("Address Column");
		templateSettings1.add(addressColumn, new FormData("100%"));

		FormPanel templateSettings2 = new FormPanel();
		templateSettings2.setSize("200", "330");
		templateSettings2.setHeaderVisible(false);
		templateSettings2.setHeading("New FormPanel");
		templateSettings2.setCollapsible(true);

		FieldSet fldstPaediatric = new FieldSet();
		fldstPaediatric.setLayout(new FormLayout());
		templateSettings2.add(fldstPaediatric, new FormData("100%"));
		fldstPaediatric.setHeading("Paediatric ");
		fldstPaediatric.setCollapsible(true);
		buildScreeningDetailTemplate(fldstPaediatric);

		FieldSet fldstDental = new FieldSet();
		fldstDental.setLayout(new FormLayout());
		templateSettings2.add(fldstDental, new FormData("100%"));
		fldstDental.setHeading("Dental");
		fldstDental.setCollapsible(true);
		buildScreeningDetailTemplate(fldstDental);

		FieldSet fldstEye = new FieldSet();
		fldstEye.setLayout(new FormLayout());
		templateSettings2.add(fldstEye, new FormData("100%"));
		fldstEye.setHeading("Eye");
		fldstEye.setCollapsible(true);
		buildScreeningDetailTemplate(fldstEye);


		FormPanel templateSettings3 = new FormPanel();
		templateSettings3.setSize("200", "260");
		templateSettings3.setHeaderVisible(false);
		templateSettings3.setHeading("New FormPanel");
		templateSettings3.setCollapsible(true);

		FieldSet fldstSkin = new FieldSet();
		templateSettings3.add(fldstSkin, new FormData("100%"));
		fldstSkin.setLayout(new FormLayout());
		fldstSkin.setHeading("Skin");
		fldstSkin.setCollapsible(true);
		buildScreeningDetailTemplate(fldstSkin);

		FieldSet fldstEnt = new FieldSet();
		templateSettings3.add(fldstEnt, new FormData("100%"));
		fldstEnt.setLayout(new FormLayout());
		fldstEnt.setHeading("ENT");
		fldstEnt.setCollapsible(true);
		buildScreeningDetailTemplate(fldstEnt);

		FieldSet fldstOther = new FieldSet();
		templateSettings3.add(fldstOther, new FormData("100%"));
		fldstOther.setLayout(new FormLayout());
		fldstOther.setHeading("Other");
		fldstOther.setCollapsible(true);
		buildScreeningDetailTemplate(fldstOther);
		
		mainTemplateSettingsPanel.add(templateSettings1);
		mainTemplateSettingsPanel.add(templateSettings2);
		mainTemplateSettingsPanel.add(templateSettings3);
		mainTemplateSettingsPanel.setSize("700", "400");
		lcTemplateComponent_1.add(mainTemplateSettingsPanel);
		return lcTemplateComponent_1;
	}

	private void buildScreeningDetailTemplate(FieldSet fieldSet)
	{

		NumberField staartColumn = new NumberField();
		staartColumn.setMaxLength(2);
		staartColumn.setFieldLabel("Start Column");
		fieldSet.add(staartColumn, new FormData("100%"));

		NumberField endColumn = new NumberField();
		endColumn.setMaxLength(2);
		endColumn.setFieldLabel("End Column");
		fieldSet.add(endColumn, new FormData("100%"));

	}

	private LayoutContainer buildUploadComponent()
	{
		LayoutContainer lcUploadComponent = new LayoutContainer();
		lcUploadComponent.setLayout(new TableLayout(2));

		LabelField lblFileImport = new LabelField("Select a file to Import:");
		lblFileImport.setSize("120", "20");

		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setValidExtensions("xls", "xlsx");
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.setMaximumFiles(1);
		defaultUploader.setAvoidRepeatFiles(true);
		defaultUploader.setTitle("Select a file to upload:");

		lcUploadComponent.add(lblFileImport);
		lcUploadComponent.add(defaultUploader);
		lcUploadComponent.setWidth("400");
		return lcUploadComponent;
	}

	public void initialize(String title, String scrId)
	{
		IndexPage.unmaskCenterComponent();
	}

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler()
	{
		public void onFinish(IUploader uploader)
		{
			if (uploader.getStatus() == Status.SUCCESS)
			{
				new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				System.out.println("Server message " + info.message);

				final MessageBox box = MessageBox.progress("Please wait", "Processing records...", "");
				final ProgressBar bar = box.getProgressBar();
				final Timer t = new Timer()
				{
					float i;

					@Override
					public void run()
					{
						bar.updateProgress(i / 100, (int) i + "% Complete");
						i += 5;
						if (i > 105)
						{
							cancel();
							box.close();
							Info.display("Message", "Items were loaded", "");
						}
					}
				};
				t.scheduleRepeating(500);
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler()
	{
		public void onLoad(PreloadedImage image)
		{
			image.setWidth("75px");
			panelImages.add(image);
		}
	};
	private LayoutContainer lcTemplateComponent_1;
}
