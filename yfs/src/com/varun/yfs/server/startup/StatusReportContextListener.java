package com.varun.yfs.server.startup;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;

import com.varun.yfs.server.common.HibernateUtil;

public class StatusReportContextListener implements ServletContextListener
{

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.close();
	}

}
