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
package br.octahedron.straight.modules.services;

import java.util.Collection;

import br.octahedron.straight.modules.services.data.ServiceView;
import br.octahedron.straight.modules.services.manager.ServiceManager;

/**
 * @author Erick Moreno
 *
 */
public interface ServicesIF {
	
	
	/**
	 * @see ServiceManager#createService(String, String, String)
	 * @param name
	 * @param value
	 * @param description
	 * @return
	 */
	public ServiceView createService(String name, String value, String description);
	
	/**
	 * @see ServiceManager#updateService(String, String, String)
	 * @param name
	 * @param value
	 * @param description
	 * @return
	 */
	public ServiceView updateService(String name, String value, String description);
	
	/**
	 * @see ServiceManager#getService(String)
	 * @param name
	 * @return
	 */
	public ServiceView getService(String name);
	
	/**
	 * @see ServiceManager#existsService(String)
	 * @param name
	 * @return
	 */
	public boolean existsService(String name);
	
	/**
	 * @see ServiceManager#addProvider(String, String)
	 * @param serviceName
	 * @param userId
	 */
	public void addProvider(String serviceName, String userId);
	
	/**
	 * @see ServiceManager#removeProvider(String, String)
	 * @param serviceName
	 * @param userId
	 */
	public void removeProvider(String serviceName, String userId);
	
	/**
	 * @see ServiceManager#getServiceProviders(String)
	 * @param serviceName
	 * @return
	 */
	public Collection<String> getServiceProviders(String serviceName);
	
	/**
	 * @see ServiceManager#getUserServices(String)
	 * @param userId
	 * @return
	 */
	public Collection<ServiceView> getUserServices(String userId);

}
