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
package br.octahedron.straight;

import static br.octahedron.cotopaxi.eventbus.EventBus.subscribe;

import java.util.Set;

import br.octahedron.cotopaxi.Bootloader;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.ModuleSpec;
import br.octahedron.util.Log;

/**
 * A {@link Bootloader} that loads all the {@link Subscriber}. 
 * 
 * @author Danilo Queiroz
 */
public class SubscribersBootloader implements Bootloader {

	private static final Log log = new Log(SubscribersBootloader.class);

	@Override
	public void boot() {
		log.debug("Loading subscribers...");
		for (Module module : Module.values()) {
			ModuleSpec spec = module.getModuleSpec();
			if (spec.hasSubscribers()) {
				Set<Class<? extends Subscriber>> subscribers = spec.getSubscribers();
				for (Class<? extends Subscriber> subscriber : subscribers) {
					log.info("Registering Subscriber to EventBus: %s", subscriber.getName());
					subscribe(subscriber);
				}
			}
		}
		log.debug("Subscribers loaded!");
	}
}
