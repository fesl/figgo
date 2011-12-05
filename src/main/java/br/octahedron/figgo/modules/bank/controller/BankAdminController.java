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
package br.octahedron.figgo.modules.bank.controller;

import java.math.BigDecimal;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceRequired;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.modules.bank.controller.validation.BankValidators;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;
import br.octahedron.figgo.modules.bank.manager.AccountManager;

/**
 * @author Vítor Avelino
 * 
 */
@AuthenticationRequired
@AuthorizationRequired
@NamespaceRequired
public class BankAdminController extends Controller {

	/*
	 * TODO public bank pages should be in other controller
	 */

	private static final String BASE_DIR_TPL = "bank/";
	private static final String BALLAST_TPL = BASE_DIR_TPL + "ballast.vm";
	private static final String SHARE_TPL = BASE_DIR_TPL + "share.vm";
	private static final String BASE_URL = "/bank";
	private static final String BALLAST_URL = BASE_URL + "/ballast";
	private static final String SHARE_URL = BASE_URL + "/share";

	@Inject
	private AccountManager accountManager;

	/**
	 * Method used by the Injector
	 */
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Gets the bank share interface
	 */
	public void getShareBank() {
		this.out("balance", this.accountManager.getBalance(this.subDomain()));
		this.success(SHARE_TPL);
	}
	
	/**
	 * Gets the bank ballast interface
	 */
	public void getBallastBank() {
		this.out("balance", this.accountManager.getBalance(this.subDomain()));
		this.success(BALLAST_TPL);
	}

	/**
	 * Transfer from bank account to a user account
	 */
	public void postShareBank() {
		Validator requiredValidator = BankValidators.getTransferValidator();
		Validator amountValidator = BankValidators.getAmountValidator();
		if (requiredValidator.isValid() && amountValidator.isValid()) {
			this.accountManager.transact(this.subDomain(), this.in("userId"), new BigDecimal(this.in("amount")), this.in("comment"), TransactionType.valueOf(this.in("type")));
			this.redirect(SHARE_URL);
		} else {
			this.echo();
			this.out("balance", this.accountManager.getBalance(this.subDomain()));
			this.invalid(SHARE_TPL);
		}
	}

	/**
	 * Transfer from system account to bank account
	 */
	public void postBallastBank() {
		Validator requiredValidator = BankValidators.getBallastValidator();
		Validator comparableValidator = BankValidators.getAmountValidator();
		if (requiredValidator.isValid() && comparableValidator.isValid()) {
			this.accountManager.insertBallast(this.subDomain(), new BigDecimal(this.in("amount")), this.in("comment"));
			this.redirect(BALLAST_URL);
		} else {
			this.echo();
			this.out("balance", this.accountManager.getBalance(this.subDomain()));
			this.invalid(BALLAST_TPL);
		}
	}
}
