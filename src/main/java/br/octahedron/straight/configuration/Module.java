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
package br.octahedron.straight.configuration;

import br.octahedron.straight.bank.BankConfigurationBuilder;
import br.octahedron.straight.configuration.data.ModuleConfiguration;
import br.octahedron.straight.configuration.data.ModuleProperty;

/**
 * @author danilo
 * 
 */
public enum Module {

	TEST(TestBuilder.class), BANK(BankConfigurationBuilder.class);

	private Class<? extends ModuleConfigurationBuilder> builderClass;

	private Module(Class<? extends ModuleConfigurationBuilder> builder) {
		this.builderClass = builder;
	}

	/**
	 * @return the builder
	 */
	public Class<? extends ModuleConfigurationBuilder> getBuilderClass() {
		return this.builderClass;
	}

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
	}
}
