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
package br.octahedron.figgo.modules.configuration.controller;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.datastore.NamespaceRequired;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.configuration.data.DomainConfiguration;
import br.octahedron.figgo.modules.configuration.manager.ConfigurationManager;

/**
 * @author vitoravelino
 *
 */
@AuthenticationRequired
@AuthorizationRequired
@NamespaceRequired
public class ConfigurationController extends Controller {

	private static final String BASE_DIR_TPL = "domain/";
	private static final String EDIT_TPL = BASE_DIR_TPL + "edit.vm";
	private static final String LIST_TPL = BASE_DIR_TPL + "list.vm";
	private static final String MODULE_CONFIG_TPL = BASE_DIR_TPL + "module/config.vm";
	private static final String BASE_URL = "/";
	
	@Inject
	private ConfigurationManager configurationManager;
	
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	public void getEditDomain() {
		DomainConfiguration domainConfiguration = this.configurationManager.getDomainConfiguration();
		out("domain", domainConfiguration);
		out("name", domainConfiguration.getName());
		out("url", domainConfiguration.getUrl());
		out("maillist", domainConfiguration.getMailList());
		out("modules", this.configurationManager.getModulesInfoService());
		success(EDIT_TPL);
	}
	
	public void postUpdateDomain() {
		this.configurationManager.updateDomainConfiguration(in("name"), in("url"), in("maillist"), in("description"));
		redirect(BASE_URL);
	}
	
	public void getListDomain() {
		out("domains", this.configurationManager.getDomainConfiguration());
		success(LIST_TPL);
	}
	
	public void getModuleDomain() {
		out("domain", this.configurationManager.getDomainConfiguration());
		out("name", in("module"));
		out("module", this.configurationManager.getModuleConfiguration(Module.valueOf(in("module").toUpperCase())));
		success(MODULE_CONFIG_TPL);
	}
	
	public void postModuleDomain() {
		
	}
	
//	def post_module() {
//		def errors = []
//		params.each() { key, value -> 
//			try {
//				if (key.startsWith("__")) {
//					configurationManager.setModuleProperty(Module.valueOf(params.module.toUpperCase()), key.substring(2), value)
//				}
//			} catch (IllegalArgumentException e) {
//				errors.add(e.getMessage())
//			}
//		}
//		if (errors.isEmpty()) {
//			redirect '/'
//		} else {
//			request.errors = errors
//			request.name = params.module
//			request.module = configurationManager.getModuleConfiguration(Module.valueOf(params.module.toUpperCase()))
//			render 'module/config.vm', request, response
//		}
//	}
	
//	def get_upload() {
//		request.domain = configurationManager.getDomainConfiguration()
//		request.upload_url = blobstore.createUploadUrl('/blob/domain/upload')
//		render 'domain/upload.vm', request, response
//	}
//
}
