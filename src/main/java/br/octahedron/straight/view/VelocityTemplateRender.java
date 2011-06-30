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
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * VelocityTemplateRender is responsible for rendering velocity templates.
 * 
 * Generally used on controllers to render the attributes of request.
 * 
 * @author Vítor Avelino
 */
public class VelocityTemplateRender {

	private static final long serialVersionUID = -6755680559427788645L;
	private static final Logger logger = Logger.getLogger(VelocityTemplateRender.class.getName());
	private static final VelocityEngine engine = new VelocityEngine();

	private static final String TEMPLATE_FOLDER = "templates/";
	private static final String CONTENT_TYPE = "text/html";

	static {
		engine.init();
	}

	public static void render(String templatePath, ServletRequest req, ServletResponse res) throws ResourceNotFoundException, ParseErrorException,
			MethodInvocationException, IOException {
		logger.fine("Getting template from " + TEMPLATE_FOLDER + templatePath);
		Template template = engine.getTemplate(TEMPLATE_FOLDER + templatePath, "utf-8");
		Enumeration<?> attributesName = req.getAttributeNames();
		VelocityContext context = new VelocityContext();
		while (attributesName.hasMoreElements()) {
			String key = attributesName.nextElement().toString();
			Object object = req.getAttribute(key);
			if (object != null) {
				context.put(key, object);
			}
		}
		res.setContentType(CONTENT_TYPE);
		template.merge(context, res.getWriter());
		logger.fine("Written template in response writer");
		res.flushBuffer();
	}
}
