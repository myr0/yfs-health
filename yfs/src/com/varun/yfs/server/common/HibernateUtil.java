package com.varun.yfs.server.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

import com.varun.yfs.server.models.ChapterName;
import com.varun.yfs.server.models.City;
import com.varun.yfs.server.models.Clinic;
import com.varun.yfs.server.models.Country;
import com.varun.yfs.server.models.Doctor;
import com.varun.yfs.server.models.Entities;
import com.varun.yfs.server.models.Locality;
import com.varun.yfs.server.models.ProcessType;
import com.varun.yfs.server.models.ReferralType;
import com.varun.yfs.server.models.State;
import com.varun.yfs.server.models.Town;
import com.varun.yfs.server.models.TypeOfLocation;
import com.varun.yfs.server.models.User;
import com.varun.yfs.server.models.Village;
import com.varun.yfs.server.models.Volunteer;

public class HibernateUtil
{
	private static final SessionFactory SESSIONFACTORY;
	private static DozerBeanMapper mapper;
	private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);

	static
	{
		try
		{
//			PropertyConfigurator.configure("log4j.properties");

			AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();
			SESSIONFACTORY = annotationConfiguration.configure().buildSessionFactory();
			LOGGER.debug("The application is booting...");

			mapper = new DozerBeanMapper();
			
			String hbm2ddl = annotationConfiguration.getProperty("hbm2ddl.auto");
			if (hbm2ddl.equalsIgnoreCase("create"))
			{
				insertReferenceData();
				LOGGER.debug("The application has finished booting.The reference data insertion is complete.");
			}
			LOGGER.debug("The application has finished booting.");

		} catch (Throwable ex)
		{
			LOGGER.error("Initial SessionFactory creation failed. No database connections available. " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return SESSIONFACTORY;
	}

	public static Mapper getDozerMapper()
	{
		return mapper;
	}

	public static void main(String[] args)
	{

		Session session = HibernateUtil.getSessionFactory().openSession();
		try
		{

		} catch (HibernateException e)
		{

		} finally
		{
			session.close();
		}
	}

	private static void insertReferenceData()
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		insertEntities(session);
		LOGGER.info("Initial load of Entities completed");

		insertCountry(session);
		LOGGER.info("Initial load of Geographical Data / Clinic completed");

		insertChapterNames(session);
		LOGGER.info("Initial load of Chapter Names completed");

		insertUsers(session);
		LOGGER.info("Initial load of Users completed");

		insertDoctor(session);
		LOGGER.info("Initial load of Doctors completed");

		insertProcessType(session);
		LOGGER.info("Initial load of Process Types completed");

		insertTypeOfLocation(session);
		LOGGER.info("Initial load of TypeOfLocation completed");

		insertVolunteer(session);
		LOGGER.info("Initial load of Volunteer completed");

		insertReferralTypes(session);
		LOGGER.info("Initial load of ReferralTypes completed");

		transaction.commit();
		session.close();
	}

	private static void insertReferralTypes(Session session)
	{
		session.save(new ReferralType("Paediatric"));
		session.save(new ReferralType("Pediatric"));
		session.save(new ReferralType("Paediatrician"));
		session.save(new ReferralType("Pediatrician"));
		session.save(new ReferralType("Others"));
		session.save(new ReferralType("Eye"));
		session.save(new ReferralType("Gynec"));
		session.save(new ReferralType("Skin"));
		session.save(new ReferralType("ENT"));
		session.save(new ReferralType("Dental"));
		session.save(new ReferralType("Orthopaedic"));
		session.flush();
	}

	private static void insertUsers(Session session)
	{
		Criteria criteria = session.createCriteria(Village.class);
		criteria.add(Restrictions.eq("deleted", "N"));
		// List<Project> lstEntities = criteria.list();

		User users = new User("Rama", "pass");
		// users.setProjects(lstEntities);
		session.save(users);

		session.save(new User("Krishna", "pass"));

		session.flush();
	}

	private static void insertVolunteer(Session session)
	{
		session.save(new Volunteer("Rama"));
		session.save(new Volunteer("Krishna"));
		session.flush();
	}

	private static void insertTypeOfLocation(Session session)
	{
		session.save(new TypeOfLocation("Urban School"));
		session.save(new TypeOfLocation("Rural School"));
		session.save(new TypeOfLocation("Centre for Children"));
		session.save(new TypeOfLocation("Slum"));
		session.save(new TypeOfLocation("Rural area"));
		session.save(new TypeOfLocation("Centre for Adults"));
		session.save(new TypeOfLocation("Clinic"));
		session.save(new TypeOfLocation("Other"));
		session.flush();
	}

	private static void insertProcessType(Session session)
	{
		session.save(new ProcessType("Volunteer screening (general)"));
		session.save(new ProcessType("Volunteer screening (eye)"));
		session.save(new ProcessType("Medical screening (comprehensive)"));
		session.save(new ProcessType("Medical screening (general)"));
		session.save(new ProcessType("Medical screening (eye)"));
		session.save(new ProcessType("Specialist screening"));
		session.flush();
	}

	private static void insertDoctor(Session session)
	{
		session.save(new Doctor("Rama"));
		session.save(new Doctor("Krishna"));
		session.flush();
	}

	private static void insertCountry(Session session)
	{
		Country country = new Country("India");
		session.save(country);
		session.flush();

		Set<State> lstStates = new HashSet<State>();

		State state1 = new State("Karnataka", country);
		lstStates.add(state1);

		Village village = new Village("Thayur");
		village.setState(state1);

		Town town = new Town("Kolar");
		town.setState(state1);

		City city = new City("B'lore");
		city.setState(state1);

		Locality locality = new Locality("Jayanagar");
		locality.setCity(city);

		State state2 = new State("Andhra Pradesh", country);
		lstStates.add(state2);

		session.save(state1);
		session.save(state2);
		session.save(new State("Tamil Nadu", country));
		session.save(new State("Maharastra", country));
		session.flush();

		session.save(city);
		session.flush();

		session.save(locality);
		session.flush();

		session.save(village);
		session.flush();

		session.save(town);
		session.flush();

		state1.setCities(new HashSet<City>(Arrays.asList(city)));
		state1.setTowns(new HashSet<Town>(Arrays.asList(town)));
		state1.setVillages(new HashSet<Village>(Arrays.asList(village)));
		session.saveOrUpdate(state1);

		country.setStates(lstStates);
		session.saveOrUpdate(country);
		session.flush();

		session.save(new Clinic("Sanjivini Free clinic", city));
		session.flush();
	}

	private static void insertChapterNames(Session session)
	{
		session.save(new ChapterName("Bangalore"));
		session.save(new ChapterName("Hyderabad"));
		session.save(new ChapterName("Pune"));
		session.save(new ChapterName("Bhopal"));
		session.save(new ChapterName("Mumbai"));
		session.save(new ChapterName("Mysore"));
		session.save(new ChapterName("Shivamogga"));
		session.save(new ChapterName("Coimbatore"));
		session.flush();
	}

	private static void insertEntities(Session session)
	{
		session.save(new Entities("Doctor", "General Screening"));
		session.save(new Entities("Volunteer", "General Screening"));
		session.save(new Entities("Chapter Name", "General Screening"));

		session.save(new Entities("Project", "School Screening"));
		session.save(new Entities("Process Type", "School Screening"));
		session.save(new Entities("Type Of Location", "School Screening"));
		session.save(new Entities("Referral Type", "School Screening"));

		session.save(new Entities("City", "Geographical"));
		session.save(new Entities("Country", "Geographical"));
		session.save(new Entities("Locality", "Geographical"));
		session.save(new Entities("State", "Geographical"));
		session.save(new Entities("Town", "Geographical"));
		session.save(new Entities("Village", "Geographical"));

		session.save(new Entities("Clinic", "Clinic Screening"));

		session.save(new Entities("Users", "Roles"));

		session.flush();
	}
}
