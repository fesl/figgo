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
package br.octahedron.figgo.modules.bank.controller;

import static br.octahedron.figgo.util.DomainUtil.generateDomainUserID;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.figgo.modules.bank.data.BankAccount;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class AbstractBankController extends Controller {

	/**
	 * Gets the {@link BankAccount} ID for the current subdomain. It will only works properly when
	 * the controller is in a subdomain.
	 * 
	 * @return The {@link BankAccount} ID for the current subdomain account.
	 */
	protected String domainBankAccount() {
		return generateDomainUserID(this.subDomain());
	}

}
