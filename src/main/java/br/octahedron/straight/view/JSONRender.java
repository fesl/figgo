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
package br.octahedron.straight.view;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import flexjson.JSONSerializer;

/**
 * JSONRender is responsible for rendering java objects into JSON format.
 * 
 * Generally used on controllers to render the attributes of request.
 * 
 * @author Vítor Avelino
 */
public class JSONRender {

	private static final long serialVersionUID = -6755680559427788645L;
	private static final Logger logger = Logger.getLogger(JSONRender.class.getName());
	
	private static final String CONTENT_TYPE = "application/json";

	public static void render(Object object, ServletRequest req, ServletResponse res) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		res.setContentType(CONTENT_TYPE);
		res.getWriter().write(new JSONSerializer().prettyPrint(true).serialize(object));
		logger.fine("Written json in response writer");
		res.flushBuffer();
	}
}
