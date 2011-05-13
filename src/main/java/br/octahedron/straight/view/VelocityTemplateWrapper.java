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

import groovy.lang.Writable;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

/**
 * @author vitoravelino
 * 
 */
public class VelocityTemplateWrapper implements groovy.text.Template {

	private static final Logger logger = Logger.getLogger(VelocityTemplateWrapper.class.getName());

	private final Template template;
	private VelocityContext context;

	public VelocityTemplateWrapper(Template template) {
		this.template = template;
	}

	public Writable make() {
		return this.make(null);
	}

	@SuppressWarnings("unchecked")
	public Writable make(final Map map) {
		logger.fine("Parsing request parameters to velocity template pages");
		return new Writable() {
			public Writer writeTo(Writer out) throws IOException {
				HttpServletRequest request = (HttpServletRequest) map.get("request");
				Enumeration<?> attributesName = request.getAttributeNames();
				VelocityTemplateWrapper.this.context = new VelocityContext();
				while (attributesName.hasMoreElements()) {
					String key = attributesName.nextElement().toString();
					Object object = request.getAttribute(key);
					if (object != null) {
						VelocityTemplateWrapper.this.context.put(key, object);
					}
				}
				VelocityTemplateWrapper.this.template.merge(VelocityTemplateWrapper.this.context, out);
				return out;
			}
		};
	}

}