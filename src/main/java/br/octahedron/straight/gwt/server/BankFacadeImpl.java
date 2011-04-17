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
package br.octahedron.straight.gwt.server;

import br.octahedron.straight.bank.manager.AccountManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author vitoravelino
 *
 */
public class BankFacadeImpl extends RemoteServiceServlet {

	private static final long serialVersionUID = -7717580296373453814L;
	
	private AccountManager accountManager = new AccountManager();
	
	public String myMethod(String s) {
		return s;
	}
	
	public void createAccount(String ownerId) {
		accountManager.createAccount(ownerId);
	}
}
