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
import java.util.List;

import javax.jdo.JDOFatalUserException;
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

	private int maxSize = 0;
	private boolean detach = false;
	protected PersistenceManagerPool pool = PersistenceManagerPool.getInstance();

	public void setQueriesMaxSize(int size) {
		this.maxSize = size;
	}

	public int getMaxQueriesSize() {
		return this.maxSize;
	}

	public void detachObjectsOnLoad(boolean detach) {
		this.detach = detach;
	}

	public boolean detachObjectsOnLoad() {
		return this.detach;
	}

	public <T> void saveObject(T persistentObject) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			pm.makePersistent(persistentObject);
		} finally {
			this.close(pm);
		}
	}

	public <T> void saveAllObjects(Collection<T> persistentObjects) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			pm.setDetachAllOnCommit(this.detach);
			tx.begin();
			pm.makePersistentAll(persistentObjects);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			this.close(pm);
		}

	}

	public <T> void deleteObject(T persistentObject) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			pm.deletePersistent(persistentObject);
		} finally {
			this.close(pm);
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
			this.close(pm);
		}
	}

	public <T> void deleteObjects(Class<T> klass) {
		this.deleteObjectsByQuery(klass, null);
	}

	public <T> void deleteObjectsByQuery(Class<T> klass, String filter) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Query query = this.prepareQuery(klass, filter, null, pm);
			query.deletePersistentAll();
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			this.close(pm);
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
			} finally {
				this.close(pm);
			}
		}
		return false;
	}

	public <T> T getObjectByKey(Class<T> klass, Object key) {
		if (key != null) {
			PersistenceManager pm = this.pool.getPersistenceManagerForThread();
			try {
				T obj = pm.getObjectById(klass, key);
				if (this.detach) {
					return pm.detachCopy(obj);
				} else {
					return obj;
				}
			} catch (JDOObjectNotFoundException ex) {
				// object not found, returning null
				return null;
			} finally {
				this.close(pm);
			}
		}
		return null;
	}

	public <T> Collection<T> getObjects(Class<T> klass) {
		return this.getObjectsByQuery(klass, null);
	}

	public <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter) {
		return this.getObjectsByQuery(klass, filter, null);
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter, String orderingAtts) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			Query query = this.prepareQuery(klass, filter, orderingAtts, pm);
			List<T> objs = null;
			try {
				objs = (List<T>) query.execute();
			} catch (JDOFatalUserException e) {
				// if occurs any error using cursor (invalid cursor)
				query = this.prepareQuery(klass, filter, orderingAtts, pm);
				objs = (List<T>) query.execute();
			}
			if (this.detach) {
				return pm.detachCopyAll(objs);
			} else {
				return objs;
			}
		} finally {
			this.close(pm);
		}
	}

	public <T> int countObjects(Class<T> klass) {
		return this.countObjectsByQuery(klass, null);
	}

	public <T> int countObjectsByQuery(Class<T> klass, String filter) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			Query query = this.prepareQuery(klass, filter, null, pm, true);
			query.setResult("count(this)");
			return (Integer) query.execute();
		} finally {
			this.close(pm);
		}
	}

	protected void close(PersistenceManager pm) {
		if (this.detach) {
			pm.close();
		}
	}

	protected <T> Query prepareQuery(Class<T> klass, String filter, String orderingAtts, PersistenceManager pm, boolean onKeys) {
		Query query;
		if (onKeys) {
			query = pm.newQuery("select id from " + klass.getName());
		} else {
			query = pm.newQuery(klass);
		}
		if (filter != null) {
			query.setFilter(filter);
		}
		if (orderingAtts != null) {
			query.setOrdering(orderingAtts);
		}
		if (this.maxSize > 0) {
			query.setRange(0, this.maxSize);
		}
		return query;
	}

	/**
	 * Prepare a query to the given class, using the given filter and ordering atts.
	 */
	private <T> Query prepareQuery(Class<T> klass, String filter, String orderingAtts, PersistenceManager pm) {
		return this.prepareQuery(klass, filter, orderingAtts, pm, false);
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
