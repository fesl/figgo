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

import br.octahedron.straight.modules.DataAlreadyExistsException;
import br.octahedron.straight.modules.DataDoesNotExistsException;
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfigurationView;
import br.octahedron.straight.modules.configuration.data.ModuleProperty;
import br.octahedron.straight.modules.configuration.manager.ConfigurationManager;

/**
 * @author danilo
 *
 */
public interface ConfigurationIF {

	/**
	 * Checks if exists the {@link DomainConfiguration} for the current Domain.
	 * 
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 */
	public abstract boolean existsDomainConfiguration();

	/**
	 * @return the current domain's {@link DomainConfiguration}
	 * @throws DataDoesNotExistsException
	 *             if there's no {@link DomainConfiguration} created.
	 */
	public abstract DomainConfiguration getDomainConfiguration();

	/**
	 * Creates this domain configuration
	 * 
	 * @param domainName
	 *            This domain name
	 * @throws DataAlreadyExistsException
	 *             if already exists configuration for the given domain.
	 */
	public abstract void createDomainConfiguration(String domainName);

	/**
	 * Enables the given modules for this domain and load the default modules' configuration.
	 * 
	 * @throws DataDoesNotExistsException
	 * 
	 * @see ConfigurationManager#enableModule(Module)
	 */
	public abstract void enableModules(Module... modules);

	/**
	 * Enables the given module for this domain and load the default module's configuration. If the
	 * module is already enabled, nothing happens
	 */
	public abstract void enableModule(Module module);

	/**
	 * Disables the given modules.
	 * 
	 * @throws DataDoesNotExistsException
	 * 
	 * @see ConfigurationManager#disableModule(Module)
	 */
	public abstract void disableModules(Module... modules);

	/**
	 * Disables the given module. If the given module isn't enabled, nothing happens.
	 */
	public abstract void disableModule(Module module);

	/**
	 * @return The {@link DomainSpecificModuleConfigurationView} for the given module, if module is
	 *         enabled for the current domain. If module isn't enabled, it returns <code>null</code>
	 *         .
	 */
	public abstract DomainSpecificModuleConfiguration getModuleConfiguration(Module module);

	/**
	 * Updates an model property's value.
	 * 
	 * If the given module hasn't any property if the the given propertyKey an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * This method performs an verification using the {@link ModuleProperty} regex. If the given
	 * value doesn't satisfy the regex, an {@link IllegalArgumentException} will be thrown.
	 * 
	 * If the given module isn't enabled for the given domain, nothing happens.
	 * 
	 * @param module
	 *            The module
	 * @param propertyKey
	 *            The property key to be updated
	 * @param propertyValue
	 *            The new property value
	 */
	public abstract void setModuleProperty(Module module, String propertyKey, String propertyValue);

	/**
	 * Restores the given module's configuration to defaults. If the given module isn't enabled,
	 * nothing happens.
	 * 
	 * @throws DataDoesNotExistsException
	 *             If this domain isn't configured.
	 */
	public abstract void restoreModuleProperties(Module module);

}