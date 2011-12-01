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
package br.octahedron.figgo.modules.service.manager;

import br.octahedron.cotopaxi.eventbus.NamespaceEvent;

/**
 * @author vitoravelino
 *
 */
public class ServiceCreatedEvent extends NamespaceEvent {

	private static final long serialVersionUID = -4859837378790183380L;

	private final String categoryId;
	private final String categoryName;
	
	/**
	 * @param categoryName
	 * @param string 
	 */
	public ServiceCreatedEvent(String categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	
	public String getCategory() {
		return this.categoryName;
	}
	
	public String getCategoryId() {
		return this.categoryId;
	}

}
