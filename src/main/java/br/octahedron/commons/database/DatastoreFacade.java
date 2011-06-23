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

import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

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

	public <T> Query createQueryForClass(Class<T> klass) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		return pm.newQuery(klass);
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

	/**
	 * @param term
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> basicQuerySearch(Class<T> klass, String term, String attribute) {
		Query query = this.createQueryForClass(klass);
		query.setFilter(attribute + " >= :1 && " + attribute + " < :2");
		term = (term != null ? term : "").trim();
		return (List<T>) query.execute(term, (term + "\ufffd"));
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
