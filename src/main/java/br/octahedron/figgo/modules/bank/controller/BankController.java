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
import java.util.LinkedList;
import java.util.List;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.auth.AuthorizationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.datastore.NamespaceRequired;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.modules.bank.controller.validation.BankValidators;
import br.octahedron.figgo.modules.bank.data.BankTransaction;
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

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void getIndexBank() {
		this.out("balance", this.accountManager.getBalance(this.currentUser()));
		this.out("transactions", this.accountManager.getLastNTransactions(this.currentUser(), 5));
		this.success(INDEX_TPL);
	}

	public void getTransferBank() {
		this.out("balance", this.accountManager.getBalance(this.currentUser()));
		this.out("transactions", this.accountManager.getLastNTransactions(this.currentUser(), 5));
		this.success(TRANSFER_TPL);
	}

	public void postTransfer() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator transactionValidator = BankValidators.getTransactionValidator();
		Validator destinationValidator = BankValidators.getDestinationValidator();
		if (requiredValidator.isValid() && transactionValidator.isValid() && destinationValidator.isValid()) {
			this.accountManager.transact(this.currentUser(), this.in("userId"), new BigDecimal(this.in("amount")), this.in("comment"),
					TransactionType.valueOf(this.in("type")));
			this.redirect(BASE_URL);
		} else {
			this.out("balance", this.accountManager.getBalance(this.currentUser()));
			this.out("userId", this.in("userId"));
			this.out("amount", this.in("amount"));
			this.out("comment", this.in("comment"));
			this.out("type", this.in("type"));
			this.invalid(TRANSFER_TPL);
		}
	}

	public void getStatementBank() {
		this.out("balance", this.accountManager.getBalance(this.currentUser()));
		this.success(STATEMENT_TPL);
	}

	public void postTransactionsBank() {
		// validate dates
		List<BankTransaction> transactions = new LinkedList<BankTransaction>();
		transactions.add(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "3"));
		transactions.add(new BankTransaction("Conta1", "FiggoBank", new BigDecimal(50), TransactionType.DEPOSIT, "3"));
		out("aaa", "aaaa");
		out("asddaa", "nnnn");
		out("transactions", transactions);
		// this.out("transactions", this.accountManager.getTransactionsByDateRange(currentUser(), Formatter.parse(in("startDate")), Formatter.parse(in("endDate"))));
		jsonSuccess();
	}

	public void getAdminBank() {
		this.out("balance", this.accountManager.getBalance(this.subDomain()));
		this.success(ADMIN_TPL);
	}

	public void postShareBank() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator destinationValidator = BankValidators.getDestinationValidator();
		if (requiredValidator.isValid() && destinationValidator.isValid()) {
			this.accountManager.transact(this.subDomain(), this.in("userId"), new BigDecimal(this.in("amount")), this.in("comment"), TransactionType
					.valueOf(this.in("type")));
			this.redirect(ADMIN_URL);
		} else {
			this.out("balance", this.accountManager.getBalance(this.subDomain()));
			this.out("userId", this.in("userId"));
			this.out("amount", this.in("amount"));
			this.out("comment", this.in("comment"));
			this.out("type", this.in("type"));
			this.invalid(ADMIN_TPL);
		}
	}

	public void postBallastBank() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator comparableValidator = BankValidators.getAmountValidator();
		if (requiredValidator.isValid() && comparableValidator.isValid()) {
			this.accountManager.insertBallast(this.subDomain(), new BigDecimal(this.in("amount")), this.in("comment"));
			this.redirect(ADMIN_URL);
		} else {
			this.out("balance", this.accountManager.getBalance(this.subDomain()));
			this.out("userId", this.in("userId"));
			this.out("amount", this.in("amount"));
			this.out("comment", this.in("comment"));
			this.out("type", this.in("type"));
			this.invalid(ADMIN_TPL);
		}
	}
}
