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
package br.octahedron.figgo.modules;

import br.octahedron.figgo.modules.domain.data.ModuleConfiguration;

/**
 * @author danilo
 *
 */
public interface DomainModuleSpec extends ApplicationDomainModuleSpec {


	/**
	 * @return <code>true</code> if this module has configurations parameter specific for each
	 *         domain or <code>false</code> if not.
	 */
	public boolean hasDomainSpecificConfiguration();

	/**
	 * @return The {@link ModuleConfiguration} for this module, if, and only if, the
	 *         {@link ModuleSpec#hasDomainSpecificConfiguration()} returns <code>true</code>. If the
	 *         {@link ModuleSpec#hasDomainSpecificConfiguration()} returns <code>false</code> it
	 *         returns null.
	 */
	public ModuleConfiguration getDomainSpecificModuleConfiguration();
	
}
