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
package br.octahedron.straight.test;

import java.util.Collection;

import javax.jdo.Query;

import br.octahedron.straight.database.GenericDAO;

/**
 * @author Danilo Queiroz
 */
public class EntityDAO extends GenericDAO<Entity> {

	public EntityDAO() {
		super(Entity.class);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Entity> getEntitiesWithElement(String element) {
		Query query = this.datastoreFacade.createQueryForClass(Entity.class);
		query.setFilter("elements == value");
		query.declareParameters("java.lang.String value");
		return (Collection<Entity>) query.execute(element);
	}

}
