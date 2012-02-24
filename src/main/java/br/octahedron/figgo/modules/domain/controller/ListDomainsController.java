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

import static br.octahedron.cotopaxi.controller.Converter.Builder.strArray;
import static br.octahedron.figgo.modules.domain.controller.ConfigurationController.BASE_DIR_TPL;
import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.figgo.modules.domain.manager.ConfigurationManager;

/**
 * TODO DOCUMENTAR
 * 
 * OPERAÇÕES PARA TODOS NS
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
@AuthenticationRequired
public class ListDomainsController extends Controller {

	private static final String LIST_TPL = BASE_DIR_TPL + "list.vm";
	
	@Inject
	private ConfigurationManager configurationManager;

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	public void getListDomain() {
		this.out("domains", this.configurationManager.getDomainsConfiguration());
		this.success(LIST_TPL);
	}
	
	/**
	 * Get domains informations
	 * TODO DOCUMENTAR
	 * @throws ConvertionException 
	 */
	public void getDomainsInfo() {
		this.out("result", this.configurationManager.getDomainsConfiguration(in("domains", strArray(","))));
		this.jsonSuccess();
	}
	
}
