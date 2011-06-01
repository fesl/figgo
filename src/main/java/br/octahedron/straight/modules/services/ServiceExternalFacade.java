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

import br.octahedron.straight.inject.Inject;
import br.octahedron.straight.modules.services.data.ServiceView;
import br.octahedron.straight.modules.services.manager.ServiceManager;


/**
 * A facade for some of the {@link ServiceManager} methods. This facade should be used by the
 * application view.
 * 
 * @see ServiceManager
 * 
 * @author Erick Moreno
 *
 */
public class ServiceExternalFacade {
	
	@Inject
	private ServiceManager serviceManager;
	
	public void setServiceManager(ServiceManager serviceManager){
		this.serviceManager = serviceManager;
	}
	
	/**
	 * @see ServiceManager#createService(String, String, String)
	 * @param name
	 * @param value
	 * @param description
	 * @return
	 */
	public ServiceView createService(String name, String value, String description){
		return this.serviceManager.createService(name, value, description);
	}
	
	/**
	 * @see ServiceManager#updateService(String, String, String)
	 * @param name
	 * @param value
	 * @param description
	 * @return
	 */
	public ServiceView updateService(String name, String value, String description){
		return this.serviceManager.updateService(name, value, description);
	}
	
	/**
	 * @see ServiceManager#getService(String)
	 * @param name
	 * @return
	 */
	public ServiceView getService(String name){
		return this.serviceManager.getService(name);
	}
	
	/**
	 * @see ServiceManager#existsService(String)
	 * @param name
	 * @return
	 */
	public boolean existsService(String name){
		return this.serviceManager.existsService(name);
	}
	
	/**
	 * @see ServiceManager#addProvider(String, String)
	 * @param serviceName
	 * @param userId
	 */
	public void addProvider(String serviceName, String userId){
		this.serviceManager.addProvider(serviceName, userId);
	}
	
	/**
	 * @see ServiceManager#removeProvider(String, String)
	 * @param serviceName
	 * @param userId
	 */
	public void removeProvider(String serviceName, String userId){
		this.serviceManager.removeProvider(serviceName, userId);
	}
	
	/**
	 * @see ServiceManager#getServiceProviders(String)
	 * @param serviceName
	 * @return
	 */
	public Collection<ServiceView> getServiceProviders(String serviceName){
		return this.getServiceProviders(serviceName);
	}
	
	/**
	 * @see ServiceManager#getUserServices(String)
	 * @param userId
	 * @return
	 */
	public Collection<ServiceView> getUserServices(String userId){
		return this.getUserServices(userId);
	}

}
