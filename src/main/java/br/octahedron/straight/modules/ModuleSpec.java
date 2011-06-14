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
package br.octahedron.straight.modules;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfiguration;

/**
 * It contains all module's information that is need by different parts of the system.
 *  
 * Each module should implement this interface, and add the module to the {@link Module} enum.
 * 
 * @author Danilo Queiroz
 */
public interface ModuleSpec {
	
	/**
	 * @return <code>true</code> if this module has configurations parameter specific for each
	 *         domain or <code>false</code> if not.
	 */
	public boolean hasDomainSpecificConfiguration();

	/**
	 * @return The {@link DomainSpecificModuleConfiguration} for this module, if, and only if, the
	 *         {@link ModuleSpec#hasDomainSpecificConfiguration()} returns <code>true</code>. If the
	 *         {@link ModuleSpec#hasDomainSpecificConfiguration()} returns <code>false</code> it returns null.
	 */
	public DomainSpecificModuleConfiguration getDomainSpecificModuleConfiguration();

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

	/**
	 * @return <code>true</code> if module is a domain specific module, <code>false</code> if it is an global module
	 */
	public boolean isDomainSpecificModule();

	/**
	 * Suggestion: for implementation use {@link Arrays#asList(Object...)}
	 * 
	 * @return A {@link Collection} with all module's activities.
	 */
	public Set<String> getModuleActions();

	/**
	 * Suggestion: for implementation use {@link Arrays#asList(Object...)}
	 * 
	 * @return A sub-set of the all module activities {@link Collection}, with all activities that
	 *         should require administrative privileges.
	 */
	public Set<String> getModuleAdministrativeActions();
	
	// TODO what about binds and facades. Let's do that here?! =]

}
