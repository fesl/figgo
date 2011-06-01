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
package br.octahedron.straight.modules;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.straight.modules.bank.BankConfigurationBuilder;
import br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder;
import br.octahedron.straight.modules.configuration.data.ModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.ModuleProperty;

/**
 * Especify the existents modules and the respective {@link ModuleConfigurationBuilder} for each
 * one.
 * 
 * @author Danilo Queiroz
 */
public enum Module {

	TEST(TestBuilder.class, null), BANK(BankConfigurationBuilder.class, null);

	private static final Logger logger = Logger.getLogger(Module.class.getName());
	private Class<? extends ModuleConfigurationBuilder> builderClass;
	private Class<? extends Subscriber> subscriberClass;

	private Module(Class<? extends ModuleConfigurationBuilder> builder, Class<? extends Subscriber> subscriber) {
		this.builderClass = builder;
		this.subscriberClass = subscriber;
	}
	
	public Class<? extends Subscriber> getSubscriberClass() {
			return this.subscriberClass;
	}

	/**
	 * @return Gets the {@link ModuleConfigurationBuilder} for the module
	 */
	public ModuleConfigurationBuilder getModuleBuilder() {
		try {
			return this.builderClass.newInstance();
		} catch (Exception ex) {
			String message = "Unable to create ModuleConfigurationBuilder for module " + this.name()
					+ ". The builder should have an empty and public constructor!";
			logger.log(Level.SEVERE, message, ex);
			throw new RuntimeException(message, ex);
		}
	}
	
	/**
	 * Module used by TESTS.
	 */
	public static class TestBuilder implements ModuleConfigurationBuilder {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * br.octahedron.straight.configuration.ModuleConfigurationBuilder#createModuleConfiguration
		 * ()
		 */
		@Override
		public ModuleConfiguration createModuleConfiguration() {
			ModuleConfiguration config = new ModuleConfiguration("TEST", TestBuilder.class);
			config.addProperty(new ModuleProperty("some.property", "some.value"));
			config.addProperty(new ModuleProperty("int", "0", "[-]?\\d+", "integer value."));
			return config;
		}

		/* (non-Javadoc)
		 * @see br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder#getAdministrativeModuleActivities()
		 */
		@Override
		public Collection<String> getAdministrativeModuleActivities() {
			return Collections.emptyList();
		}

		/* (non-Javadoc)
		 * @see br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder#getAllModuleActivities()
		 */
		@Override
		public Collection<String> getAllModuleActivities() {
			return Collections.emptyList();
		}
	}
}
