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
package br.octahedron.figgo.modules.service.controller.validation;

import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Rule;
import br.octahedron.figgo.modules.service.manager.ServiceManager;

/**
 * @author Vítor Avelino
 *
 */
public class ExistentServiceRule implements Rule {

	@Inject
	private ServiceManager serviceManager;
	
	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.validation.Rule#getMessage()
	 */
	@Override
	public String getMessage() {
		return "EXISTENT_SERVICE";
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.validation.Rule#isValid(java.lang.String)
	 */
	@Override
	public boolean isValid(String input) {
		return serviceManager.existsService(input);
	}

}