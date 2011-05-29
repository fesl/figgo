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
package br.octahedron.straight.modules.configuration;

import java.util.Arrays;
import java.util.Collection;

import br.octahedron.straight.modules.configuration.data.ModuleConfiguration;

/**
 * A builder for {@link ModuleConfiguration}.
 * 
 * All Modules should implements this class, and the builder should have a constructor with no
 * parameters.
 * 
 * @author Danilo Queiroz
 */
public interface ModuleConfigurationBuilder {

	/**
	 * @return the {@link ModuleConfiguration}
	 */
	public abstract ModuleConfiguration createModuleConfiguration();

	/**
	 * Suggestion: for implementation use {@link Arrays#asList(Object...)}
	 * 
	 * @return A {@link Collection} with all module's activities.
	 */
	public Collection<String> getAllModuleActivities();

	/**
	 * Suggestion: for implementation use {@link Arrays#asList(Object...)}
	 * 
	 * @return A sub-set of the all module activities {@link Collection}, with all activities that
	 *         should require administrative privileges.
	 */
	public Collection<String> getAdministrativeModuleActivities();

}
