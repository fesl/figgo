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
package br.octahedron.figgo.modules.domain.controller;

import static br.octahedron.cotopaxi.CotopaxiProperty.APPLICATION_BASE_URL;
import static br.octahedron.cotopaxi.CotopaxiProperty.getProperty;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.interceptor.TemplateInterceptor;
import br.octahedron.cotopaxi.view.response.TemplateResponse;
import br.octahedron.figgo.modules.DataDoesNotExistsException;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.bank.BankSpec;
import br.octahedron.figgo.modules.domain.data.ModuleConfiguration;
import br.octahedron.figgo.modules.domain.manager.ConfigurationManager;

/**
 * A response interceptor that adds the current DomainConfiguration to output
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class DomainResponseInterceptor extends TemplateInterceptor {
	
	@Inject
	private ConfigurationManager configurationManager;
	
	@Inject
	private NamespaceManager namespaceManager;
	
	/**
	 * @param namespaceManager the namespaceManager to set
	 */
	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}
	
	/**
	 * @param configurationManager the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.interceptor.TemplateInterceptor#preRender(br.octahedron.cotopaxi.view.response.RenderableResponse)
	 */
	@Override
	public void preRender(TemplateResponse response) {
		try {
			// TODO review
			response.addOutput("applicationBaseURL", getProperty(APPLICATION_BASE_URL));
			response.addOutput("applicationDomain", getProperty("APPLICATION_DOMAIN"));
			String subdomain = this.subDomain();
			if (!subdomain.equals("www")) {
				this.namespaceManager.changeToNamespace(subdomain);
				ModuleConfiguration moduleConfiguration = this.configurationManager.getModuleConfiguration(Module.BANK);
				response.addOutput("currencySymbol", moduleConfiguration.getPropertyValue(BankSpec.CURRENCY_SYMBOL));
				response.addOutput("domain", this.configurationManager.getDomainConfiguration());
				response.addOutput("modules", this.configurationManager.getModulesInfoService());
			}
		} catch (DataDoesNotExistsException e) {
			// nothing to do here.
		} finally {
			namespaceManager.changeToPreviousNamespace();
		}
	}

}
