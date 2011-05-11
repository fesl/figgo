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
package br.octahedron.straight.configuration.manager;

import br.octahedron.straight.configuration.data.DomainConfigurationDAO;
import br.octahedron.straight.configuration.data.ModuleConfigurationDAO;

/**
 * This entity is responsible by manager all configurations for a domain, and the modules enabled
 * for that domain.
 * 
 * @author Danilo Queiroz
 */
public class ConfigurationManager {
//	private DomainConfigurationDAO domainDAO = new DomainConfigurationDAO();
//	private ModuleConfigurationDAO moduleDAO = new ModuleConfigurationDAO();

	
	/*
	 * 
	 * get domain configuration
	 * get module configuration info service -> module name
	 * 
	 * Create a domain -> name
	 * 
	 * enable modules -> list modules name
	 * 		for each module load configuration and persist
	 * 
	 * disable modules -> list module name
	 * 		for each module, remove configurations
	 * 
	 * update module -> module name, property key, property value
	 * 		validate value
	 * 		update
	 * module restore -> module name
	 * 		restore module default props
	 */
	

}
