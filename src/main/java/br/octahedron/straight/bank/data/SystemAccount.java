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
package br.octahedron.straight.bank.data;

import java.math.BigDecimal;

import javax.jdo.annotations.PersistenceCapable;

/**
 * @author vitoravelino
 *
 */
@PersistenceCapable
public class SystemAccount extends BankAccount {

	private static final long serialVersionUID = 7342767364513878827L;
	
	public SystemAccount() {
		super("Banco", 0L);
	}
	
	@Override
	public BigDecimal getBalance() {
		return new BigDecimal(Long.MAX_VALUE);
	} 

}
