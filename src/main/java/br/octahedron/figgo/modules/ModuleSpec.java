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
package br.octahedron.figgo.modules;

import java.util.Set;

import br.octahedron.cotopaxi.eventbus.Subscriber;

/**
 * It contains all module's information that is need by different parts of the system.
 * 
 * Each module should implement this interface, and add the module to the {@link Module} enum.
 * 
 * @author Danilo Queiroz
 */
public interface ModuleSpec {
	
	/**
	 * Indicates the module Type
	 */
	public enum Type {
		/**
		 * Indicates that the module is an Application Module and has a global scope.
		 * 
		 * An application module is an internal module to the application and provides features
		 * needed by the system. This kind of module can't be enabled/disabled.
		 */
		APPLICATION_GLOBAL,
		/**
		 * Indicates that the module is an Application Module and has domain scope.
		 * 
		 * An application module is an internal module to the application and provides features
		 * needed by the system. This kind of module can't be enabled/disabled.
		 */
		APPLICATION_DOMAIN,
		/**
		 * Indicates that the module is a domain module, and provides functionalities specific for a
		 * domain needs. This kind of module can be enable/disabled per domain.
		 */
		DOMAIN;
	}

	/**
	 * @return The {@link Module.Type}
	 */
	public Type getModuleType();


	/**
	 * @return <code>true</code> if this module has any subscriber interested in any kind of event
	 *         or <code>false</code> if not.
	 */
	public boolean hasSubscribers();

	/**
	 * @return A set with all subscribers that this module has, if, and only if, the
	 *         {@link ModuleSpec#hasSubscribers()} returns <code>true</code>. If the
	 *         {@link ModuleSpec#hasSubscribers()} returns <code>false</code> it returns null.
	 */
	public Set<Class<? extends Subscriber>> getSubscribers();
}
