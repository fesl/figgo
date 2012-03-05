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
package br.octahedron.figgo.modules.admin.controller;

import static br.octahedron.cotopaxi.controller.Converter.Builder.bool;
import static br.octahedron.cotopaxi.controller.Converter.Builder.safeString;
import static br.octahedron.figgo.modules.admin.controller.validation.AdminValidators.getConfigValidator;
import static br.octahedron.figgo.modules.admin.controller.validation.AdminValidators.getDomainValidator;
import static br.octahedron.figgo.util.DomainUtil.getDomainURL;
import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.OnlyForGlobalSubdomainControllerInterceptor.OnlyForGlobal;
import br.octahedron.figgo.modules.admin.data.ApplicationConfiguration;
import br.octahedron.figgo.modules.admin.manager.AdminManager;

/**
 * @author Vítor Avelino
 */
@AuthorizationRequired
@AuthenticationRequired
@OnlyForGlobal
public class AdminController extends Controller {

	private static final String BASE_DIR_TPL = "admin/";
	private static final String INDEX_TPL = BASE_DIR_TPL + "index.vm";
	private static final String CONFIG_APP_TPL = BASE_DIR_TPL + "config.vm";
	private static final String NEW_DOMAIN_TPL = BASE_DIR_TPL + "domain/new.vm";
	private static final String NEW_DOMAIN_URL = "/admin/domain/new";

	@Inject
	private AdminManager adminManager;

	public void setAdminManager(AdminManager adminManager) {
		this.adminManager = adminManager;
	}

	/**
	 * Shows admin index page.
	 */
	public void getAdminIndex() {
		this.success(INDEX_TPL);
	}

	/**
	 * Shows app config form
	 */
	public void getAppConfig() {
		if (this.adminManager.hasApplicationConfiguration()) {
			ApplicationConfiguration applicationConfiguration = this.adminManager.getApplicationConfiguration();
			this.out("accessKey", applicationConfiguration.getRoute53AccessKeyID());
			this.out("keySecret", applicationConfiguration.getRoute53AccessKeySecret());
			this.out("zone", applicationConfiguration.getRoute53ZoneID());
		}
		this.success(CONFIG_APP_TPL);
	}

	/**
	 * Process app config form
	 */
	public void postAppConfig() {
		if (getConfigValidator().isValid()) {
			this.adminManager.configureApplication(this.in("accessKey", safeString()), this.in("keySecret", safeString()),
					this.in("zone", safeString()));
			this.redirect(NEW_DOMAIN_URL);
		} else {
			this.echo();
			this.invalid(CONFIG_APP_TPL);
		}
	}

	/**
	 * Shows new domain form
	 */
	public void getCreateDomain() {
		this.success(NEW_DOMAIN_TPL);
	}

	/**
	 * Process new domain form
	 */
	public void postCreateDomain() {
		Validator validator = getDomainValidator();
		if (validator.isValid()) {
			String domain = this.in("name", safeString());
			this.adminManager.createDomain(domain, this.in("userId", safeString()), this.in("dns", bool()));
			this.redirect(getDomainURL(domain));
		} else {
			this.echo();
			this.invalid(NEW_DOMAIN_TPL);
		}
	}
}
