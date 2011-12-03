package com.varun.yfs.server.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.varun.yfs.server.common.HibernateUtil;

public class StatusReportContextListener implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		HibernateUtil.getSessionFactory().openSession().close();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}

}
