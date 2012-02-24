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
package br.octahedron.figgo;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;
import br.octahedron.util.Log;

/**
 * This interceptor only allows the controller to be executed if the subdomain isn't equals to
 * 'www'. If it is, it fires a not found.
 * 
 * To use it just added the {@link OnlyForNamespace} annotation to {@link Controller} class/method
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class OnlyForNamespaceControllerInterceptor extends ControllerInterceptor {
	
	private static final Log log = new Log(OnlyForNamespaceControllerInterceptor.class); 

	@Retention(value = RetentionPolicy.RUNTIME)
	@Target(value = {ElementType.METHOD, ElementType.TYPE})
	public @interface OnlyForNamespace {
	}

	@Override
	public void execute(Annotation arg0) {
		if (this.subDomain().equalsIgnoreCase("www")) {
			log.debug("Invalid address for www");
			this.notFound();
		}
	}

	@Override
	public Class<? extends Annotation> getInterceptorAnnotation() {
		return OnlyForNamespace.class;
	}
}
