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
package br.octahedron.figgo.test;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Simple entity create for Datastore tests purpose.
 * 
 * @author Danilo Queiroz
 */
@PersistenceCapable
public class Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent
	private String key;

	@Persistent
	private List<String> elements = new LinkedList<String>();

	/**
	 * 
	 */
	public Entity(String key) {
		this.key = key;
	}

	public void add(String... elements) {
		for (String element : elements) {
			this.elements.add(element);
		}
	}

	public List<String> getElements() {
		return this.elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.key;
	}

}
