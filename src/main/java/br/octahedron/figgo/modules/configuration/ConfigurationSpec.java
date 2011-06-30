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
package br.octahedron.figgo.modules.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.figgo.modules.Module;
import br.octahedron.figgo.modules.ModuleSpec;
import br.octahedron.figgo.modules.Module.Type;
import br.octahedron.figgo.modules.configuration.data.DomainSpecificModuleConfiguration;
import br.octahedron.figgo.modules.configuration.manager.CreateDomainConfigurationSubscriber;
import br.octahedron.figgo.modules.configuration.manager.DomainChangedSubscriber;
import br.octahedron.figgo.modules.configuration.manager.DomainUploadSubscriber;

/**
 * @author vitoravelino
 * 
 */
public class ConfigurationSpec implements ModuleSpec {

	private static final String[] ACTIONS = { "INDEX", "DOMAIN_EDIT", "DOMAIN_UPLOAD" };
	private static final String[] ADMIN_ACTIONS = { "DOMAIN_EDIT", "DOMAIN_UPLOAD" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleType()
	 */
	@Override
	public Type getModuleType() {
		return Module.Type.APPLICATION_DOMAIN;
	}

	@Override
	public boolean hasDomainSpecificConfiguration() {
		return false;
	}

	@Override
	public DomainSpecificModuleConfiguration getDomainSpecificModuleConfiguration() {
		return null;
	}

	@Override
	public boolean hasSubscribers() {
		return true;
	}

	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		Set<Class<? extends Subscriber>> subscribers = new HashSet<Class<? extends Subscriber>>();
		subscribers.add(CreateDomainConfigurationSubscriber.class);
		subscribers.add(DomainUploadSubscriber.class);
		subscribers.add(DomainChangedSubscriber.class);
		return subscribers;
	}

	@Override
	public boolean usesDomainNamespace() {
		return true;
	}

	@Override
	public boolean needsAuthentication(String action) {
		if (action.equals("INDEX")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean needsAuthorization(String action) {
		if (action.equals("INDEX")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Set<String> getModuleActions() {
		return new LinkedHashSet<String>(Arrays.asList(ACTIONS));
	}

	@Override
	public Set<String> getModuleAdministrativeActions() {
		return new LinkedHashSet<String>(Arrays.asList(ADMIN_ACTIONS));

	}
}
