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
package br.octahedron.straight.modules.bank.data;

import br.octahedron.straight.database.GenericDAO;

/**
 * @author Danilo Queiroz
 */
public class BankAccountDAO extends GenericDAO<BankAccount> {

	private static final Long SYSTEM_ACCOUNT_ID = new Long(0L);
	private static final BankAccount SYSTEM_ACCOUNT = new SystemAccount();

	public BankAccountDAO() {
		super(BankAccount.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.database.GenericDAO#get(java.lang.Object)
	 */
	@Override
	public BankAccount get(Object key) {
		Long keyLong = (Long) key;
		if (keyLong.equals(SYSTEM_ACCOUNT_ID)) {
			return SYSTEM_ACCOUNT;
		} else {
			return super.get(key);
		}
	}

}
