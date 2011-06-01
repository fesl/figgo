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
package br.octahedron.straight.database;

import java.util.Collection;

import br.octahedron.straight.inject.Inject;

/**
 * An generic DAO interface to be extended.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class GenericDAO<T> {

	@Inject
	protected DatastoreFacade datastoreFacade = new DatastoreFacade();
	private Class<T> klass;
	

	public GenericDAO(Class<T> klass) {
		this.klass = klass;
	}
	
	/**
	 * @param datastoreFacade the datastoreFacade to set
	 */
	public void setDatastoreFacade(DatastoreFacade datastoreFacade) {
		this.datastoreFacade = datastoreFacade;
	}

	/**
	 * Deletes an object. The given parameter can be the object to be deleted, it means an instance
	 * of T, or can be the key for the object to be deleted.
	 * 
	 * @param object
	 *            The object to be deleted or the key for the object to be deleted.
	 */
	public void delete(Object object) {
		if (object.getClass().equals(this.klass)) {
			this.datastoreFacade.deleteObject(object);
		} else {
			if (this.exists(object)) {
				this.delete(this.get(object));
			}
		}
	}

	/**
	 * Saves an entity. It doesn't verify if the object already exists, it just saves, overwriting
	 * the previous object, if exists.
	 * 
	 * @param entity
	 *            The entity to be save
	 */
	public void save(T entity) {
		this.datastoreFacade.saveObject(entity);
	}

	/**
	 * @return Gets all T entities.
	 */
	public Collection<T> getAll() {
		return this.datastoreFacade.getObjects(this.klass);
	}

	/**
	 * Gets an T entity.
	 * 
	 * @param key
	 *            the entity's key.
	 * @return The entity with the given key, if exists, null otherwise.
	 */
	public T get(Object key) {
		return this.datastoreFacade.getObjectByKey(this.klass, key);
	}

	/**
	 * Checks if exists an entity with the given key.
	 * 
	 * @param key
	 *            the entity's key
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 */
	public boolean exists(Object key) {
		return this.datastoreFacade.existsObject(this.klass, key);
	}

	/**
	 * @return The number of T entities stored
	 */
	public int count() {
		return this.datastoreFacade.countObjects(this.klass);
	}

}
