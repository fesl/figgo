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

import static br.octahedron.cotopaxi.auth.AbstractGoogleAuthenticationInterceptor.CURRENT_USER_EMAIL;

import java.math.BigDecimal;

import br.octahedron.cotopaxi.auth.AuthenticationRequired;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.validation.Validator;
import br.octahedron.figgo.modules.bank.controller.validation.BankValidators;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;
import br.octahedron.figgo.modules.bank.manager.AccountManager;

/**
 * @author Vítor Avelino
 *
 */
public class BankController extends Controller {

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

	@AuthenticationRequired
	public void getIndex() {
		out("balance", this.accountManager.getBalance((String) session(CURRENT_USER_EMAIL)));
		out("transactions", this.accountManager.getLastNTransactions((String) session(CURRENT_USER_EMAIL), 5));
		success(INDEX_TPL);
	}

	@AuthenticationRequired
	public void getTransfer() {
		out("balance", this.accountManager.getBalance((String) session(CURRENT_USER_EMAIL)));
		out("transactions", this.accountManager.getLastNTransactions((String) session(CURRENT_USER_EMAIL), 5));
		success(TRANSFER_TPL);
	}

	@AuthenticationRequired
	public void getStatement() {
		out("balance", this.accountManager.getBalance((String) session(CURRENT_USER_EMAIL)));
		success(STATEMENT_TPL);
	}
	
	@AuthenticationRequired
	public void getAdmin() {
		out("balance", this.accountManager.getBalance(getSubDomain()));
		success(ADMIN_TPL);
	}
	
	@AuthenticationRequired
	public void postTransfer() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator transactionValidator = BankValidators.getTransactionValidator();
		Validator destinationValidator = BankValidators.getDestinationValidator();
		if (requiredValidator.isValid() && transactionValidator.isValid() && destinationValidator.isValid()) {
			this.accountManager.transact((String) session(CURRENT_USER_EMAIL), in("userId"), new BigDecimal(in("amount")), in("comment"), TransactionType.valueOf(in("type")));
			redirect(BASE_URL);
		} else {
			out("balance", this.accountManager.getBalance((String) session(CURRENT_USER_EMAIL)));
			out("userId", in("userId"));
			out("amount", in("amount"));
			out("comment", in("comment"));
			out("type", in("type"));
			invalid(TRANSFER_TPL);
		}
	}
	
	@AuthenticationRequired
	public void postShare() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator destinationValidator = BankValidators.getDestinationValidator();
		if (requiredValidator.isValid() && destinationValidator.isValid()) {
			this.accountManager.transact(getSubDomain(), in("userId"), new BigDecimal(in("amount")), in("comment"), TransactionType.valueOf(in("type")));
			redirect(ADMIN_URL);
		} else {
			out("balance", this.accountManager.getBalance(getSubDomain()));
			out("userId", in("userId"));
			out("amount", in("amount"));
			out("comment", in("comment"));
			out("type", in("type"));
			invalid(ADMIN_TPL);
		}
	}
	
	@AuthenticationRequired
	public void postBallast() {
		Validator requiredValidator = BankValidators.getRequiredValidator();
		Validator comparableValidator = BankValidators.getAmountValidator();
		if (requiredValidator.isValid() && comparableValidator.isValid()) {
			this.accountManager.insertBallast(getSubDomain(), new BigDecimal(in("amount")), in("comment"));
			redirect(ADMIN_URL);
		} else {
			out("balance", this.accountManager.getBalance(getSubDomain()));
			out("userId", in("userId"));
			out("amount", in("amount"));
			out("comment", in("comment"));
			out("type", in("type"));
			invalid(ADMIN_TPL);
		}
	}

}
