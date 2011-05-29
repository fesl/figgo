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
package br.octahedron.straight.modules.bank;

import java.util.Arrays;
import java.util.Collection;

import br.octahedron.straight.modules.configuration.Module;
import br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder;
import br.octahedron.straight.modules.configuration.data.ModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.ModuleProperty;

/**
 * The {@link ModuleConfigurationBuilder} for the Bank module.
 * 
 * It is responsible by create the {@link ModuleConfiguration} for bank, holding all the properties
 * for this module.
 * 
 * @author Danilo Queiroz
 */
public class BankConfigurationBuilder implements ModuleConfigurationBuilder {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.straight.configuration.ModuleConfigurationBuilder#createModuleConfiguration()
	 */
	@Override
	public ModuleConfiguration createModuleConfiguration() {
		// TODO set the correct facade for the bank.
		ModuleConfiguration bankConfig = new ModuleConfiguration(Module.BANK.name(), null);
		bankConfig.addProperty(new ModuleProperty("name", "Banco", "", "O nome do banco."));
		bankConfig.addProperty(new ModuleProperty("currency", "Dinheiro", "", "O nome da moeda do banco."));
		bankConfig.addProperty(new ModuleProperty("currencyAbreviation", "$", "", "A abreviação para a moeda do banco."));
		return bankConfig;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder#getAllModuleActivities()
	 */
	@Override
	public Collection<String> getAllModuleActivities() {
		return Arrays.asList("1", "2", "3"); // TODO set available activities
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.configuration.ModuleConfigurationBuilder#getAdministrativeModuleActivities()
	 */
	@Override
	public Collection<String> getAdministrativeModuleActivities() {
		return Arrays.asList("1", "2"); // TODO set admin activities
	}
}
