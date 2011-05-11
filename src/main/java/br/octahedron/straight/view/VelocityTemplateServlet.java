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

import groovy.servlet.TemplateServlet;
import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.io.File;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * @author vitoravelino
 * 
 */
public class VelocityTemplateServlet extends TemplateServlet {

	private static final Logger logger = Logger.getLogger(VelocityTemplateServlet.class.getName());
	private static final long serialVersionUID = -6755680559427788645L;
	private static final String TEMPLATE_FOLDER = "templates/";

	private TemplateEngine engine = new VelocityTemplateEngine();

	/*
	 * (non-Javadoc)
	 * 
	 * @see groovy.servlet.TemplateServlet#getTemplate(java.io.File)
	 */
	@Override
	protected Template getTemplate(File file) throws ServletException {
		try {
			logger.fine("Creating groovy template object from '" + file.getName() + "'");
			return this.engine.createTemplate(TEMPLATE_FOLDER + file.getName());
		} catch (Exception e) {
			throw new ServletException("Creation of template failed " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see groovy.servlet.TemplateServlet#initTemplateEngine(javax.servlet.ServletConfig)
	 */
	@Override
	protected TemplateEngine initTemplateEngine(ServletConfig config) {
		return this.engine;
	}
}
