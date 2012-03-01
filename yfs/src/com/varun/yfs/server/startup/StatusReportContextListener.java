package com.varun.yfs.server.startup;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

import com.varun.yfs.server.models.data.HibernateUtil;

public class StatusReportContextListener implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		PropertyConfigurator.configure(new File(arg0.getServletContext().getRealPath("/"), "WEB-INF/log4j.properties")
				.toString());
		HibernateUtil.getSessionFactory().openSession().close();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}

}
