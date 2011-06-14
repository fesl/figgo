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

import static br.octahedron.commons.eventbus.EventBus.subscribe;

import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.straight.modules.ModuleSpec;
import br.octahedron.straight.modules.Module;

/**
 * A {@link ServletContextListener} that bootstraps the system.
 * 
 * @author Danilo Queiroz
 */
public class BootstrapServletListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(BootstrapServletListener.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Booting...");
		for (Module module : Module.values()) {
			ModuleSpec spec = module.getModuleSpec();
			if ( spec.hasSubscribers() ) {
				Set<Class<? extends Subscriber>> subscribers = spec.getSubscribers();
				for (Class<? extends Subscriber> subscriber : subscribers) {
					logger.info("Registering Subscriber to EventBus: " + subscriber.getName());
					subscribe(subscriber);
				}
			}
		}
		logger.info("Up and running!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("System is going down!");
	}

}
