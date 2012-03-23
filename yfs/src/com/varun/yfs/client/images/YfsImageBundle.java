package com.varun.yfs.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface YfsImageBundle extends ClientBundle
{
	public static final YfsImageBundle INSTANCE = GWT.create(YfsImageBundle.class);

	@Source("add.png")
	public ImageResource addButtonIcon();

	@Source("arrow_refresh.png")
	public ImageResource refreshButtonIcon();

	@Source("delete.png")
	public ImageResource deleteButtonIcon();

	@Source("export.png")
	public ImageResource exportButtonIcon();

	@Source("excelExport.png")
	public ImageResource excelExportIcon();

	@Source("home.png")
	public ImageResource homeButtonIcon();

	@Source("import.png")
	public ImageResource importButtonIcon();

	@Source("help.png")
	public ImageResource helpButtonIcon();

	@Source("exit.png")
	public ImageResource exitButtonIcon();

	@Source("dfslogo1.png")
	public ImageResource dfsLogoImage();

	@Source("yfslogotag.png")
	public ImageResource yfsLogoImage();

	@Source("userPassword.png")
	public ImageResource userPassword();

	@Source("world.png")
	public ImageResource worldIcon();

	@Source("settings.png")
	public ImageResource settingsIcon();

	@Source("report.png")
	public ImageResource reportIcon();

	@Source("reportIndividual.png")
	public ImageResource reportIndividualIcon();

	@Source("screeningIndividual.png")
	public ImageResource screeningIndividualIcon();

}
