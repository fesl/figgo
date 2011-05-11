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

import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import org.apache.velocity.app.VelocityEngine;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * @author vitoravelino
 * 
 */
public class VelocityTemplateEngine extends TemplateEngine {

	private static final Logger logger = Logger.getLogger(VelocityTemplateEngine.class.getName());
	private static final long serialVersionUID = -6755680559427788645L;
	private static final VelocityEngine engine = new VelocityEngine();

	static {
		engine.init();
	}

	@Override
	public Template createTemplate(String filename) throws CompilationFailedException, ClassNotFoundException, IOException {
		logger.info("Criando template wrapper... " + filename);
		return new VelocityTemplateWrapper(engine.getTemplate(filename));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see groovy.text.TemplateEngine#createTemplate(java.io.Reader)
	 */
	@Override
	public Template createTemplate(Reader reader) throws CompilationFailedException, ClassNotFoundException, IOException {
		throw new RuntimeException("Never say never!");
	}

}
