/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.straight.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.commons.database.NamespaceCommons;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author Danilo Queiroz
 */
public class ControllerFilter implements Filter {

	private static final Logger logger = Logger.getLogger(ControllerFilter.class.getName());
	private static final String APPENGINE_COMMAND = "_";
	private static final String TEST_APPLICATION_DOMAIN = "localhost";
	protected static final String BARRA = "barra";
	protected static final String APPLICATION_DOMAIN = "www";

	private static final UserService userService = UserServiceFactory.getUserService();

	private ControllerChecker checker = new ControllerChecker();
	private String appDomain;
	private String userCreate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.appDomain = config.getInitParameter("APPLICATION_URL");
		this.userCreate = config.getInitParameter("USER_CREATE_PAGE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String requestURI = request.getRequestURI();

		String domain = request.getServerName().split("\\.")[0].toLowerCase();
		if (TEST_APPLICATION_DOMAIN.equals(domain)) {
			domain = APPLICATION_DOMAIN;
		}

		String module = (requestURI.length() > 1) ? requestURI.split("/")[1].toUpperCase() : BARRA;
		String action = requestURI.substring(1).replace('/', '_').toUpperCase();
		User user = userService.getCurrentUser();
		String email = (user != null) ? user.getEmail() : null;

		try {
			logger.info(requestURI);
			if (!action.startsWith(APPENGINE_COMMAND)) {
				this.checker.check(domain, email, module, action);
			}
			// everything is ok now, go ahead!
			logger.info(">>>>> chain");
			chain.doFilter(req, resp);
		} catch (NotLoggedException e) {
			e.printStackTrace();
			response.sendRedirect(userService.createLoginURL(request.getRequestURL().toString()));
		} catch (InexistentAccountException e) {
			e.printStackTrace();
			logger.info(this.appDomain + this.userCreate);
			response.sendRedirect(this.appDomain + this.userCreate);
		} catch (NotFoundException e) {
			e.printStackTrace();
			response.sendRedirect(this.appDomain + "/404");
		} catch (NotAuthorizedException e) {
			e.printStackTrace();
			response.sendRedirect(this.appDomain + "/403");
		} finally {
			NamespaceCommons.backToPreviousNamespace();
		}
	}

}
