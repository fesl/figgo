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
package br.octahedron.commons.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * Facade to GAE DataStore Service. Provides methods to save, load and query objects in low level.
 * 
 * @see DatastoreFacade
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class DatastoreFacade {

	/*
	 * Every datastore write operation is atomic, so it isn't necessary to use transactions for
	 * save/delete operations over an unique entity.
	 */

	protected PersistenceManagerPool pool = PersistenceManagerPool.getInstance();

	public <T> void saveObject(T persistentObject) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		pm.makePersistent(persistentObject);
		pm.close();
	}

	public <T> void saveAllObjects(Collection<T> persistentObjects) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistentAll(persistentObjects);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public <T> boolean existsObject(Class<T> klass, Object key) {
		if (key != null) {
			PersistenceManager pm = this.pool.getPersistenceManagerForThread();
			try {
				pm.getObjectById(klass, key);
				return true;
			} catch (JDOObjectNotFoundException ex) {
				// object not found, returning null
				return false;
			}
		}
		return false;
	}

	public <T> T getObjectByKey(Class<T> klass, Object key) {
		if (key != null) {
			PersistenceManager pm = this.pool.getPersistenceManagerForThread();
			try {
				T obj = pm.getObjectById(klass, key);
				return obj;
			} catch (JDOObjectNotFoundException ex) {
				// object not found, returning null
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjects(Class<T> klass) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Query query = pm.newQuery(klass);
		return (List<T>) query.execute();
	}

	public <T> int countObjects(Class<T> klass) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Query query = pm.newQuery(klass);
		query.setResult("count(this)");
		return (Integer) query.execute();
	}

	public <T> void deleteObject(T persistentObject) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		pm.deletePersistent(persistentObject);
	}

	public <T> void deleteObjects(Class<T> klass) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Query query = pm.newQuery(klass);
			query.deletePersistentAll();
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public <T> void deleteAllObjects(Collection<T> persistentObjects) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.deletePersistentAll(persistentObjects);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public <T> Query createQueryForClass(Class<T> klass) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		return pm.newQuery(klass);
	}
	
	/**
	 * Query for all entities of the given class with the given attribute starts with the given prefix;
	 * 
	 * @param prefix the attribute's prefix to be queried
	 * @param attribute the entity attribute being queried
	 * 
	 * @return a list of all entities which attribute starts with the given prefix. If no entity be found, it returns an empty list.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> startsWithQuery(Class<T> klass, String prefix, String attribute) {
		Query query = this.createQueryForClass(klass);
		query.setFilter(attribute + ".startsWith(:1)");
		prefix = (prefix != null ? prefix : "").trim();
		return (List<T>) query.execute(prefix);
	}

	/**
	 * Retrieves all namespaces created to domains on application
	 */
	public List<String> getNamespaces() {
		com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(com.google.appengine.api.datastore.Query.NAMESPACE_METADATA_KIND);
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<String> results = new ArrayList<String>();
	    for (com.google.appengine.api.datastore.Entity e : ds.prepare(q).asIterable()) {
	        // A zero numeric id is used for the non-default namespaces
	        if (e.getKey().getId() == 0) { 
	        	results.add(e.getKey().getName());
	        }
	    }
		return results;
	}

	/**
	 * This class is a wrapper to the {@link PersistenceManagerFactory}.
	 */
	protected static class PMFWrapper {
		static {
			System.setProperty("javax.jdo.PersistenceManagerFactoryClass",
					"org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory");
		}
		private static final PersistenceManagerFactory pmFactory = JDOHelper.getPersistenceManagerFactory("transactions-optional");

		public static PersistenceManagerFactory getPersistenceManagerFactory() {
			return pmFactory;
		}

		public static PersistenceManager getPersistenceManager() {
			return pmFactory.getPersistenceManager();
		}
	}
}
