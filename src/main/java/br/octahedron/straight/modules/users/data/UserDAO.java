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
package br.octahedron.straight.modules.users.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.ComparisonChain;

import br.octahedron.commons.database.GenericDAO;

/**
 * @author Erick Moreno
 * @author Danilo Queiroz
 * @author Vítor Avelino
 * 
 */
public class UserDAO extends GenericDAO<User> {
	
	private static final String NAME_ATTRIBUTE = "nameLowerCase";
	private static final String EMAIL_ATTRIBUTE = "userId";

	public UserDAO() {
		super(User.class);
	}
	
	/**
	 * Retrieves a collection of {@link User} that its name or email starts with a term.
	 * 
	 * @param term
	 * @return
	 */
	public Collection<User> getUsersStartingWith(String term) {
		Collection<User> searchResultName = this.datastoreFacade.startsWithQuery(User.class, term.toLowerCase(), NAME_ATTRIBUTE);
		Collection<User> searchResultEmail = this.datastoreFacade.startsWithQuery(User.class, term.toLowerCase(), EMAIL_ATTRIBUTE);
		SortedSet<User> result = new TreeSet<User>(new UserComparator());
		result.addAll(searchResultEmail);
		result.addAll(searchResultName);
		return result;
	}
	
	private class UserComparator implements Comparator<User> {
		@Override
		public int compare(User o1, User o2) {
			return ComparisonChain.start()
				   .compare(o1.getName().toLowerCase(), o2.getName().toLowerCase())
				   .compare(o1.getUserId().toLowerCase(), o2.getUserId().toLowerCase())
				   .result();
		}
	}

}
