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

	@Source("cancel-upld.gif")
	public ImageResource cancelUploadButtonIcon();

	@Source("cancel-upld-hover.gif")
	public ImageResource cancelUploadHoverButtonIcon();

	@Source("delete.png")
	public ImageResource deleteButtonIcon();

	@Source("export.png")
	public ImageResource exportButtonIcon();

	@Source("home.png")
	public ImageResource homeButtonIcon();

	@Source("import.png")
	public ImageResource importButtonIcon();

	@Source("help.png")
	public ImageResource helpButtonIcon();

	@Source("screeningHelp.png")
	public ImageResource screeningHelpButtonImage();

	@Source("adminHelp.png")
	public ImageResource adminHelpButtonImage();
}
