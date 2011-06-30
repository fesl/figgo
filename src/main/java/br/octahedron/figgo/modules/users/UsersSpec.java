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
package br.octahedron.figgo.modules.users;

import java.util.Set;
import java.util.TreeSet;

import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.ModuleSpec;
import br.octahedron.figgo.modules.Module.Type;
import br.octahedron.figgo.modules.configuration.data.DomainSpecificModuleConfiguration;
import br.octahedron.figgo.modules.users.manager.UsersUploadSubscriber;

/**
 * @author vitoravelino
 * 
 */
public class UsersSpec implements ModuleSpec {

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleType()
	 */
	@Override
	public Type getModuleType() {
		// TODO Auto-generated method stub
		return Module.Type.APPLICATION_GLOBAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getDomainSpecificModuleConfiguration()
	 */
	@Override
	public DomainSpecificModuleConfiguration getDomainSpecificModuleConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleActions()
	 */
	@Override
	public Set<String> getModuleActions() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleAdministrativeActions()
	 */
	@Override
	public Set<String> getModuleAdministrativeActions() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getSubscribers()
	 */
	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		Set<Class<? extends Subscriber>> subscribers = new TreeSet<Class<? extends Subscriber>>();
		subscribers.add(UsersUploadSubscriber.class);
		return subscribers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#hasDomainSpecificConfiguration()
	 */
	@Override
	public boolean hasDomainSpecificConfiguration() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#hasSubscribers()
	 */
	@Override
	public boolean hasSubscribers() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#needsAuthentication(java.lang.String)
	 */
	@Override
	public boolean needsAuthentication(String action) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#needsAuthorization(java.lang.String)
	 */
	@Override
	public boolean needsAuthorization(String action) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#usesDomainNamespace()
	 */
	@Override
	public boolean usesDomainNamespace() {
		return false;
	}

}
