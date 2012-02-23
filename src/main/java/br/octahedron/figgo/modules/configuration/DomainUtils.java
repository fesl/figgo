/*
 *  Figgo - https://www.ohloh.net/p/figgo/
 *  Copyright (C) 2011  Octahedron - FESL
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

import br.octahedron.figgo.modules.bank.data.BankAccount;

/**
 * Provides useful methods to deal with domains.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class DomainUtils {

	private static final String ADDRESS_SUFFIX = "@figgo.com.br";

	/**
	 * Generates the {@link BankAccount} ID for the given domain.
	 * 
	 * @param domain
	 *            The domain name.
	 * @return The bank account id for the domain's account.
	 */
	public static String generateDomainUserID(String domain) {
		return domain.trim() + ADDRESS_SUFFIX;
	}
}
