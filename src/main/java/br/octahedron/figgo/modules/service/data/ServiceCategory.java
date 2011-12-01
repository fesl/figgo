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
package br.octahedron.figgo.modules.service.data;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author vitoravelino
 *
 */
@PersistenceCapable
public class ServiceCategory implements Serializable {

	private static final long serialVersionUID = 1365487370292324050L;

	@PrimaryKey
	@Persistent
	private String id;
	
	@Persistent
	private String name;
	
	public ServiceCategory(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
}
