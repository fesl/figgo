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
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.configuration.data.DomainConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainConfigurationDAO;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfigurationView;
import br.octahedron.straight.modules.configuration.data.ModuleConfigurationDAO;

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
	 * @return The {@link DomainSpecificModuleConfigurationView} for the given module
	 */
	private DomainSpecificModuleConfiguration createModuleConfig(Module module) {
		return module.getModuleSpec().getDomainSpecificModuleConfiguration();
	}
	
	/**
	 * Updates a {@link DomainConfiguration} avatar key with the generated blob key.
	 * 
	 * @param avatarKey Blob key generated when uploaded avatar
	 */
	public void updateAvatarKey(String avatarKey) {
		this.getDomainConfiguration().setAvatarKey(avatarKey);
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#existsDomainConfiguration()
	 */
	public boolean existsDomainConfiguration() {
		return this.domainDAO.count() != 0;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#getDomainConfiguration()
	 */
	public DomainConfiguration getDomainConfiguration() {
		if (this.existsDomainConfiguration()) {
			Collection<DomainConfiguration> domain = this.domainDAO.getAll();
			return domain.iterator().next();
		} else {
			throw new DataDoesNotExistsException("This domain was not configured yet");
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#createDomainConfiguration(java.lang.String)
	 */
	public void createDomainConfiguration(String domainName) {
		if (!this.existsDomainConfiguration()) {
			DomainConfiguration domain = new DomainConfiguration(domainName);
			this.domainDAO.save(domain);
		} else {
			throw new DataAlreadyExistsException("Already exists a domain configuration for the given domain: " + domainName);
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#enableModules(br.octahedron.straight.modules.Module)
	 */
	public void enableModules(Module... modules) {
		for (Module module : modules) {
			this.enableModule(module);
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#enableModule(br.octahedron.straight.modules.Module)
	 */
	public void enableModule(Module module) {
		DomainConfiguration config = this.getDomainConfiguration();
		if (!config.isModuleEnabled(module.name())) {
			config.enableModule(module.name());
			// TODO testar este comportamento!
			if (!this.moduleDAO.exists(module.name()) && module.getModuleSpec().hasDomainSpecificConfiguration()) {
				this.moduleDAO.save(this.createModuleConfig(module));
			}
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#disableModules(br.octahedron.straight.modules.Module)
	 */
	public void disableModules(Module... modules) {
		for (Module module : modules) {
			this.disableModule(module);
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#disableModule(br.octahedron.straight.modules.Module)
	 */
	public void disableModule(Module module) {
		this.getDomainConfiguration().disableModule(module.name());
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#getModuleConfiguration(br.octahedron.straight.modules.Module)
	 */
	public DomainSpecificModuleConfiguration getModuleConfiguration(Module module) {
		DomainConfiguration domainConfig = this.getDomainConfiguration();
		if (domainConfig.isModuleEnabled(module.name()) && module.getModuleSpec().hasDomainSpecificConfiguration()) {
			return this.moduleDAO.get(module.name());
		} else {
			throw new DataDoesNotExistsException("The module " + module.name() + " isn't enabled.");
		}
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#setModuleProperty(br.octahedron.straight.modules.Module, java.lang.String, java.lang.String)
	 */
	public void setModuleProperty(Module module, String propertyKey, String propertyValue) {
		DomainConfiguration domainConf = this.getDomainConfiguration();
		if (domainConf.isModuleEnabled(module.name())) {
			DomainSpecificModuleConfiguration moduleConf = this.moduleDAO.get(module.name());
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
	
	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.manager.Configuration#restoreModuleProperties(br.octahedron.straight.modules.Module)
	 */
	public void restoreModuleProperties(Module module) {
		DomainConfiguration domain = this.getDomainConfiguration();
		if (domain.isModuleEnabled(module.name())) {
			DomainSpecificModuleConfiguration moduleConf = this.moduleDAO.get(module.name());
			moduleConf.restoreDefaults();
		} else {
			throw new DataDoesNotExistsException("The module " + module.name() + " isn't enabled.");
		}
	}
}
