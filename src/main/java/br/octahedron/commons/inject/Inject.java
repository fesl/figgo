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
package br.octahedron.commons.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declare fields to be injected on the object. This should be used at the facade classes and its
 * injected classes.
 * 
 * For each fields which should be injected the class should provides a "set" method.
 * 
 * Eg.: If you want to inject an UserService at the UserFacade class, the UserFacade class should
 * have a method "public void setUserService(UserService service)" to injection works.
 * 
 * On the above example, the UserService class, for its time, can have some injection dependencies
 * too.
 * 
 * Eg.:
 * 
 * <pre>
 * 
 * public class UserFacade {
 * 	&#064;Inject
 * 	private UserService service;
 * 
 * 	public void setUserService(UserService service) {
 * 		this.service = service;
 * 	}
 * }
 * 
 * public class UserService {
 * 	&#064;Inject
 * 	private UserDAO userDAO;
 * 
 * 	public void setUserDAO(UserDAO userDAO) {
 * 		this.userDAO = userDAO;
 * 	}
 * }
 * 
 * public class UserDAO {
 * 	&#064;Inject
 * 	private DatastoreFacade datastore;
 * 	&#064;Inject
 * 	private MemcacheFacade memcache;
 * 
 * 	public void setDatastoreFacade(DatastoreFacade datastore) {
 * 		this.datastore = datastore;
 * 	}
 * 
 * 	public void setMemcacheFacade(MemcacheFacade memcache) {
 * 		this.memcache = memcache;
 * 	}
 * }
 * </pre>
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
	/**
	 * Defines if it should use a brand new, and exclusive, instance, or if ith should uses an
	 * already existent instance. By default its <code>false</code> - it will use an already
	 * existent instance.
	 * 
	 * To use a new and exclusive instance, set it to <code>true</code>
	 */
	boolean newInstance() default false;
}
