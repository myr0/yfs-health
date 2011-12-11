package com.varun.yfs.server.reports.rpc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.varun.yfs.client.reports.rpc.ImageService;

public class ImageServiceImpl extends RemoteServiceServlet implements ImageService
{
	private static final long serialVersionUID = -6737253680259121546L;

	public String getImageToken(String base64image)
	{
		byte[] imageBytes = Base64.decode(base64image);
		String token = getMd5(base64image);
		if (token == null)
			token = Math.round(Math.random()) + "";
		getThreadLocalRequest().getSession().setAttribute("img_" + token, imageBytes);
		return token;
	}

	private String getMd5(String base64image)
	{
		String token = null;
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(base64image.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			token = hash.toString(16);
		} catch (NoSuchAlgorithmException nsae)
		{
		}
		return token;
	}

}