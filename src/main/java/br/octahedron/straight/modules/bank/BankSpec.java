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

import java.util.Collections;
import java.util.Set;

import br.octahedron.commons.eventbus.Subscriber;
import br.octahedron.straight.modules.ModuleSpec;
import br.octahedron.straight.modules.Module;
import br.octahedron.straight.modules.configuration.data.DomainSpecificModuleConfiguration;
import br.octahedron.straight.modules.configuration.data.ModuleProperty;

/**
 * @author Danilo Queiroz
 */
public class BankSpec implements ModuleSpec {
	
	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#isDomainSpecificModule()
	 */
	@Override
	public boolean isDomainSpecificModule() {
		return true;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#hasDomainSpecificConfiguration()
	 */
	@Override
	public boolean hasDomainSpecificConfiguration() {
		return true;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#getDomainSpecificModuleConfiguration()
	 */
	@Override
	public DomainSpecificModuleConfiguration getDomainSpecificModuleConfiguration() {
		DomainSpecificModuleConfiguration bankConfig = new DomainSpecificModuleConfiguration(Module.BANK.name());
		bankConfig.addProperty(new ModuleProperty("name", "Banco", "[A-Za-z0-9 _-]{5,}", "O nome do banco."));
		bankConfig.addProperty(new ModuleProperty("currency", "Dinheiro", "", "O nome da moeda do banco."));
		bankConfig.addProperty(new ModuleProperty("currencyAbreviation", "$", "", "A abreviação para a moeda do banco."));
		return bankConfig;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#hasSubscribers()
	 */
	@Override
	public boolean hasSubscribers() {
		return false;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#getSubscribers()
	 */
	@Override
	public Set<Class<? extends Subscriber>> getSubscribers() {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleActions()
	 */
	@Override
	public Set<String> getModuleActions() {
		return Collections.emptySet(); // TODO to be defined
	}

	/* (non-Javadoc)
	 * @see br.octahedron.straight.modules.ModuleSpec#getModuleAdministrativeActions()
	 */
	@Override
	public Set<String> getModuleAdministrativeActions() {
		return Collections.emptySet(); // TODO to be defined
	}

}
