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
package br.octahedron.straight.modules.configuration.manager;

import java.util.Collection;

import br.octahedron.straight.modules.DataAlreadyExistsException;
import br.octahedron.straight.modules.DataDoesNotExistsException;
import br.octahedron.straight.modules.configuration.Module;
import br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationDAO;
import br.octahedron.straight.modules.configuration.data.ModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.ModuleConfigurationDAO;
import br.octahedron.straight.modules.configuration.data.ModuleConfigurationView;
import br.octahedron.straight.modules.configuration.data.ModuleProperty;

/**
 * This entity is responsible by manager all configurations for a domain, and the modules enabled
 * for that domain.
 * 
 * This class assumes that it's working on the domain namespace, so it deals with only the current
 * one domain.
 * 
 * @author Danilo Queiroz
 */
public class ConfigurationManager {

	private DomainConfigurationDAO domainDAO = new DomainConfigurationDAO();
	private ModuleConfigurationDAO moduleDAO = new ModuleConfigurationDAO();

	/**
	 * To be used by tests
	 * 
	 * @param domainDAO
	 *            The {@link DomainConfigurationDAO} to be set
	 */
	protected void setDomainConfigurationDAO(DomainConfigurationDAO domainDAO) {
		this.domainDAO = domainDAO;
	}

	/**
	 * To be used by tests
	 * 
	 * @param moduleDAO
	 *            The {@link ModuleConfigurationDAO} to be set
	 */
	protected void setModuleConfigurationDAO(ModuleConfigurationDAO moduleDAO) {
		this.moduleDAO = moduleDAO;
	}

	/**
	 * @return The {@link ModuleConfigurationView} for the given module
	 */
	private ModuleConfiguration createModuleConfig(Module module) {
		ModuleConfigurationBuilder builder = module.getModuleBuilder();
		return builder.createModuleConfiguration();
	}

	/**
	 * Checks if exists the {@link DomainConfiguration} for the current Domain.
	 * 
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 */
	public boolean existsDomainConfiguration() {
		return this.domainDAO.count() != 0;
	}

	/**
	 * @return the current domain's {@link DomainConfiguration}
	 * @throws DataDoesNotExistsException
	 *             if there's no {@link DomainConfiguration} created.
	 */
	public DomainConfiguration getDomainConfiguration() {
		if (this.existsDomainConfiguration()) {
			Collection<DomainConfiguration> domain = this.domainDAO.getAll();
			return domain.iterator().next();
		} else {
			throw new DataDoesNotExistsException("This domain was not configured yet");
		}
	}

	/**
	 * Creates this domain configuration
	 * 
	 * @param domainName
	 *            This domain name
	 * @throws DataAlreadyExistsException
	 *             if already exists configuration for the given domain.
	 */
	public void createDomainConfiguration(String domainName) {
		if (!this.existsDomainConfiguration()) {
			DomainConfiguration domain = new DomainConfiguration(domainName);
			this.domainDAO.save(domain);
		} else {
			throw new DataAlreadyExistsException("Already exists a domain configuration for the given domain: " + domainName);
		}
	}

	/**
	 * Enables the given modules for this domain and load the default modules' configuration.
	 * 
	 * @throws DataDoesNotExistsException
	 * 
	 * @see ConfigurationManager#enableModule(Module)
	 */
	public void enableModules(Module... modules) {
		for (Module module : modules) {
			this.enableModule(module);
		}
	}

	/**
	 * Enables the given module for this domain and load the default module's configuration. If the
	 * module is already enabled, nothing happens
	 */
	public void enableModule(Module module) {
		DomainConfiguration config = this.getDomainConfiguration();
		if (!config.isModuleEnabled(module.name())) {
			config.enableModule(module.name());
			// TODO testar este comportamento!
			if (!this.moduleDAO.exists(module.name())) {
				this.moduleDAO.save(this.createModuleConfig(module));
			}
		}
	}

	/**
	 * Disables the given modules.
	 * 
	 * @throws DataDoesNotExistsException
	 * 
	 * @see ConfigurationManager#disableModule(Module)
	 */
	public void disableModules(Module... modules) {
		for (Module module : modules) {
			this.disableModule(module);
		}
	}

	/**
	 * Disables the given module. If the given module isn't enabled, nothing happens.
	 */
	public void disableModule(Module module) {
		this.getDomainConfiguration().disableModule(module.name());
	}

	/**
	 * @return The {@link ModuleConfigurationView} for the given module, if module is enabled for
	 *         the current domain. If module isn't enabled, it returns <code>null</code>.
	 */
	public ModuleConfiguration getModuleConfiguration(Module module) {
		DomainConfiguration domainConfig = this.getDomainConfiguration();
		if (domainConfig.isModuleEnabled(module.name())) {
			return this.moduleDAO.get(module.name());
		} else {
			throw new DataDoesNotExistsException("The module " + module.name() + " isn't enabled.");
		}
	}

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
	public void setModuleProperty(Module module, String propertyKey, String propertyValue) {
		DomainConfiguration domainConf = this.getDomainConfiguration();
		if (domainConf.isModuleEnabled(module.name())) {
			ModuleConfiguration moduleConf = this.moduleDAO.get(module.name());
			if (moduleConf.existsProperty(propertyKey)) {
				String regex = moduleConf.getPropertyRegex(propertyKey);
				if (!regex.isEmpty()) {
					if (!propertyValue.matches(regex)) {
						throw new IllegalArgumentException("The given value for property " + propertyKey + " from module " + module.name()
								+ " isn't valid.");
					}
				}
				moduleConf.setConfigurationValue(propertyKey, propertyValue);
			} else {
				throw new IllegalArgumentException("The module " + module.name() + "hasn't any property with key " + propertyKey);
			}
		} else {
			throw new DataDoesNotExistsException("The module " + module.name() + " isn't enabled.");
		}
	}

	/**
	 * Restores the given module's configuration to defaults. If the given module isn't enabled,
	 * nothing happens.
	 * 
	 * @throws DataDoesNotExistsException
	 *             If this domain isn't configured.
	 */
	public void restoreModuleProperties(Module module) {
		DomainConfiguration domain = this.getDomainConfiguration();
		if (domain.isModuleEnabled(module.name())) {
			ModuleConfiguration moduleConf = this.moduleDAO.get(module.name());
			moduleConf.restoreDefaults();
		} else {
			throw new DataDoesNotExistsException("The module " + module.name() + " isn't enabled.");
		}
	}
}
