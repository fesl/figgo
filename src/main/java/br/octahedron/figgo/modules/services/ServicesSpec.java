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
package br.octahedron.figgo.modules.services;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.ModuleSpec;
import br.octahedron.figgo.modules.Module.Type;
import br.octahedron.figgo.modules.configuration.data.DomainSpecificModuleConfiguration;

/**
 * @author Vítor Avelino
 */
public class ServicesSpec implements ModuleSpec {

	private static final String[] ACTIONS = { };
	private static final String[] ADMIN_ACTIONS = { };

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleType()
	 */
	@Override
	public Type getModuleType() {
		return Module.Type.DOMAIN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleActions()
	 */
	@Override
	public Set<String> getModuleActions() {
		return new LinkedHashSet<String>(Arrays.asList(ACTIONS));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleAdministrativeActions()
	 */
	@Override
	public Set<String> getModuleAdministrativeActions() {
		return new LinkedHashSet<String>(Arrays.asList(ADMIN_ACTIONS));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#hasDomainSpecificConfiguration()
	 */
	@Override
	public boolean hasDomainSpecificConfiguration() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getDomainSpecificModuleConfiguration()
	 */
	@Override
	public DomainSpecificModuleConfiguration getDomainSpecificModuleConfiguration() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#hasSubscribers()
	 */
	@Override
	public boolean hasSubscribers() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getSubscribers()
	 */
	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		return null;
	}
}
