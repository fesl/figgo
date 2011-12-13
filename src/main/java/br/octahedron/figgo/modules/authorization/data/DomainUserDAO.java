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
package br.octahedron.figgo.modules.authorization.data;

import java.util.Collection;

import javax.jdo.Query;

import br.octahedron.cotopaxi.datastore.jdo.GenericDAO;

/**
 * @author vitoravelino
 *
 */
public class DomainUserDAO extends GenericDAO<DomainUser> {

	public DomainUserDAO() {
		super(DomainUser.class);
	}
	
	public Collection<DomainUser> getActiveUsers(String domain) {
		return this.getUsers(domain, true);
	}
	
	public Collection<DomainUser> getNonActiveUsers(String domain) {
		return this.getUsers(domain, false);
	}

	@SuppressWarnings("unchecked")
	private Collection<DomainUser> getUsers(String domain, boolean isActive) {
		Query query = this.createQuery();
		query.setFilter("isActive == :isActive && domains == :domain");
		return (Collection<DomainUser>) query.execute(isActive, domain);
	}
}
