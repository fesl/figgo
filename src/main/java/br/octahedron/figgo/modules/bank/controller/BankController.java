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

import br.octahedron.commons.util.Formatter;
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
public class BankController extends Controller {

	/*
	 * TODO public bank pages should be in other controller
	 */

	private static final String BASE_DIR_TPL = "bank/";
	private static final String ADMIN_TPL = BASE_DIR_TPL + "admin.vm";
	private static final String INDEX_TPL = BASE_DIR_TPL + "index.vm";
	private static final String STATEMENT_TPL = BASE_DIR_TPL + "statement.vm";
	private static final String TRANSFER_TPL = BASE_DIR_TPL + "transfer.vm";
	private static final String BASE_URL = "/bank";
	private static final String ADMIN_URL = BASE_URL + "/admin";

	@Inject
	private AccountManager accountManager;

	/**
	 * Method used by the Injector
	 */
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Shows the initial bank page for a user
	 */
	public void getIndexBank() {
		this.out("balance", this.accountManager.getBalance(this.currentUser()));
		this.out("transactions", this.accountManager.getLastNTransactions(this.currentUser(), 5));
		this.success(INDEX_TPL);
	}

	/**
	 * Shows the page for transfers between users
	 */
	public void getTransferBank() {
		// TODO validate value
		this.out("balance", this.accountManager.getBalance(this.currentUser()));
		this.out("transactions", this.accountManager.getLastNTransactions(this.currentUser(), 5));
		this.success(TRANSFER_TPL);
	}

	/**
	 * Posts a transfer between users
	 */
	public void postTransfer() {
		// TODO validate value
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator destinationValidator = BankValidators.getDestinationValidator();

		if (requiredValidator.isValid() && destinationValidator.isValid()) {
			this.accountManager.transact(this.currentUser(), this.in("userId"), new BigDecimal(this.in("amount")), this.in("comment"),
					TransactionType.valueOf(this.in("type")));
			this.redirect(BASE_URL);
		} else {
			this.out("balance", this.accountManager.getBalance(this.currentUser()));
			this.echo();
			this.invalid(TRANSFER_TPL);
		}
	}

	/**
	 * Gets the current user current balance
	 */
	public void getStatementBank() {
		this.out("balance", this.accountManager.getBalance(this.currentUser()));
		this.success(STATEMENT_TPL);
	}

	/**
	 * Posts parameters to get user's transactions
	 */
	public void postTransactionsBank() {
		// TODO date validation ASAP
		// Need to validate is begin date is lesser than end date
		this.out("transactions", this.accountManager.getTransactionsByDateRange(currentUser(), Formatter.parse(in("startDate")), Formatter.parse(in("endDate"))));
		jsonSuccess();
	}

	// TODO move admin bank operations to a new controller

	/**
	 * Gets the bank admin interface
	 */
	public void getAdminBank() {
		this.out("balance", this.accountManager.getBalance(this.subDomain()));
		this.success(ADMIN_TPL);
	}

	/**
	 * Transfer from bank account to a user account
	 */
	public void postShareBank() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator destinationValidator = BankValidators.getDestinationValidator();
		if (requiredValidator.isValid() && destinationValidator.isValid()) {
			this.accountManager.transact(this.subDomain(), this.in("userId"), new BigDecimal(this.in("amount")), this.in("comment"), TransactionType.valueOf(this.in("type")));
			this.redirect(ADMIN_URL);
		} else {
			this.echo();
			this.out("balance", this.accountManager.getBalance(this.subDomain()));
			this.invalid(ADMIN_TPL);
		}
	}

	/**
	 * Transfer from system account to bank account
	 */
	public void postBallastBank() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		// TODO change validation (we dont need the type)
		Validator comparableValidator = BankValidators.getAmountValidator();
		if (requiredValidator.isValid() && comparableValidator.isValid()) {
			this.accountManager.insertBallast(this.subDomain(), new BigDecimal(this.in("amount")), this.in("comment"));
			this.redirect(ADMIN_URL);
		} else {
			this.echo();
			this.out("balance", this.accountManager.getBalance(this.subDomain()));
			this.invalid(ADMIN_TPL);
		}
	}
}
