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
package br.octahedron.straight.modules.configuration.data;

import java.util.Set;


/**
 * A read-only interface for the {@link DomainConfiguration}
 * 
 * @see DomainConfiguration
 * 
 * @author Danilo Queiroz
 * @author Erick Moreno
 */
public interface DomainConfigurationView {
	
	/**
	 * @see DomainConfiguration#getDomainName()
	 */
	public abstract String getDomainName();
	
	/**
	 * @see DomainConfiguration#getName()
	 */
	public abstract String getName();

	/**
	 * @see DomainConfiguration#getModulesEnabled()
	 */
	public abstract Set<String> getModulesEnabled();

	/**
	 * @see DomainConfiguration#isModuleEnabled(String)
	 */
	public abstract boolean isModuleEnabled(String moduleName);

}
