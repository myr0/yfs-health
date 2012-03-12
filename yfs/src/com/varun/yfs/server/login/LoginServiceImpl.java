package com.varun.yfs.server.login;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.login.rpc.LoginService;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.models.data.DataUtil;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
	private static final long serialVersionUID = 4456105400553118785L;
	private static final Logger LOGGER = Logger.getLogger(LoginServiceImpl.class);

	@Override
	public UserDTO loginServer(String name, String password)
	{
		LOGGER.debug("Attempting login for user: " + name);

		if (name == null || password == null)
			return null;

		List<UserDTO> modelList = DataUtil.<UserDTO> getModelList("User");
		UserDTO user = new UserDTO(name, password);
		int usrIdx = modelList.indexOf(user);

		if (usrIdx >= 0) // user-name exists
		{
			UserDTO userDbModel = modelList.get(usrIdx);
			if (userDbModel.getPassword().equalsIgnoreCase(password))
			{
				user = new UserDTO(userDbModel.getProperties());
				user.setName(name);
				user.setPassword(password);

				user.setLoggedIn(true);
				user.setSessionId(this.getThreadLocalRequest().getSession().getId());

				storeUserInSession(user);

				LOGGER.info("User: " + name + " logged in successfully. Session id: " + user.getSessionId());
				return user;
			}
			LOGGER.info("Login for user: " + name + " failed. Password for given UserName doesn't match.");
		}

		if (usrIdx <= 0)
			LOGGER.info("Login for user: " + name + " failed. UserName doesnt exist.");

		return user;
	}

	@Override
	public UserDTO loginFromSessionServer()
	{
		return getUserAlreadyFromSession();
	}

	@Override
	public void logout()
	{

		deleteUserFromSession();

	}

	@Override
	public boolean changePassword(String name, String newPassword)
	{
		LOGGER.debug("Attempting password change for User: " + name);

		List<UserDTO> modelList = DataUtil.<UserDTO> getModelList("User");
		UserDTO user = new UserDTO(name, "");
		int usrIdx = modelList.indexOf(user);

		if (usrIdx >= 0) // user-name exists
		{
			UserDTO userDTO = modelList.get(usrIdx);

			userDTO.setPassword(newPassword);

			DataUtil.saveUser(userDTO);

			LOGGER.info("Password change for User: " + name + " successful.");
			return true;
		}

		LOGGER.debug("Password change for User: " + name + " failed.");
		return false;
	}

	private UserDTO getUserAlreadyFromSession()
	{
		UserDTO user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("user");

		LOGGER.debug("Attempting re-login for existing user");

		if (userObj != null && userObj instanceof UserDTO)
		{
			user = (UserDTO) userObj;
			LOGGER.info("Obtained reference for an existing user.");
			return user;
		}

		LOGGER.debug("Re-login for existing user not possible. No user exists in the session.");
		return user;
	}

	private void storeUserInSession(UserDTO user)
	{
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession(true);
		session.setAttribute("user", user);
	}

	private void deleteUserFromSession()
	{
		LOGGER.debug("Attempting logout change for User");

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute("user");

		LOGGER.info("Logout User sucessfull.");

	}

}
