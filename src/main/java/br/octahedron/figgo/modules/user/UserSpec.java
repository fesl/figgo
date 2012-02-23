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
package br.octahedron.figgo.modules.user;

import java.util.Set;
import java.util.TreeSet;

import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.figgo.modules.ModuleSpec;
import br.octahedron.figgo.modules.user.manager.DomainUserSubscriber;
import br.octahedron.figgo.modules.user.manager.UsersUploadSubscriber;

/**
 * @author Vítor Avelino vitoravelino@octahedron.com.br
 * @author Danilo Queiroz daniloqueiroz@octahedron.com.br
 */
public class UserSpec implements ModuleSpec {

	@Override
	public Type getModuleType() {
		return Type.APPLICATION_GLOBAL;
	}

	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		Set<Class<? extends Subscriber>> subscribers = new TreeSet<Class<? extends Subscriber>>();
		subscribers.add(UsersUploadSubscriber.class);
		subscribers.add(DomainUserSubscriber.class);
		return subscribers;
	}

	@Override
	public boolean hasSubscribers() {
		return true;
	}
}
