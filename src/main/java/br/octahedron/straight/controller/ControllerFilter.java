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
import br.octahedron.straight.view.VelocityTemplateRender;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * @author Danilo Queiroz
 */
public class ControllerFilter implements Filter {

	private static final Logger logger = Logger.getLogger(ControllerFilter.class.getName());
	private static final String APPENGINE_COMMAND = "_AH";
	private static final String BLOB_MODULE = "BLOB";
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
			if (!action.startsWith(APPENGINE_COMMAND) && !module.equals(BLOB_MODULE)) {
				this.checker.check(domain, email, module, action);
			}
			// everything is ok now, go ahead!
			request.setAttribute("user", user);
			request.setAttribute("login_url", userService.createLoginURL(this.appDomain));
			request.setAttribute("logout_url", userService.createLogoutURL(this.appDomain));
			chain.doFilter(req, resp);
		} catch (NotLoggedException e) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURL().toString()));
		} catch (InexistentAccountException e) {
			response.sendRedirect(this.appDomain + this.userCreate);
		} catch (NotFoundException e) {
			notFoundHandler(request, response);
		} catch (NotAuthorizedException e) {
			notAuthorizedHandler(request, response);
		} catch (Throwable t) {
			genericErrorHandler(request, response, t);
		} finally {
			NamespaceCommons.backToPreviousNamespace();
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param t
	 */
	private void genericErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable t) {
		try {
			response.setStatus(500);
			request.setAttribute("errorMessage", t.getMessage());
			VelocityTemplateRender.render("error.vm", request, response);
		} catch (Exception e) {
			// we expect this never happen!
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	private void notAuthorizedHandler(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setStatus(403);
			VelocityTemplateRender.render("notauthorized.vm", request, response);
		} catch (Exception e) {
			// we expect this never happen!
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	private void notFoundHandler(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setStatus(404);
			VelocityTemplateRender.render("notfound.vm", request, response);
		} catch (Exception e) {
			// we expect this never happen!
		}
		
	}

}
