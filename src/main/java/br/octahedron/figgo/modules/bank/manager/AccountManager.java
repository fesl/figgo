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
package br.octahedron.figgo.modules.bank.manager;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import br.octahedron.figgo.modules.bank.data.BankAccount;
import br.octahedron.figgo.modules.bank.data.BankAccountDAO;
import br.octahedron.figgo.modules.bank.data.BankTransaction;
import br.octahedron.figgo.modules.bank.data.BankTransactionDAO;
import br.octahedron.figgo.modules.bank.data.SystemAccount;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;

/**
 * @author Erick Moreno
 * @author Vítor Avelino
 * 
 */
public class AccountManager {

	private static final Logger logger = Logger.getLogger(AccountManager.class.getName());

	private BankAccountDAO accountDAO = new BankAccountDAO();
	private BankTransactionDAO transactionDAO = new BankTransactionDAO();

	/*
	 * Just for tests.
	 */
	protected void setTransactionDAO(BankTransactionDAO transactionDAO) {
		this.transactionDAO = transactionDAO;
	}

	/*
	 * Just for tests.
	 */
	protected void setAccountDAO(BankAccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	/**
	 * Get the N last transactions for an account.
	 * 
	 * @return A {@link Collection} with the last n transactions. If there's no transactions for the
	 *         given account, returns an empty {@link Collection}
	 */
	public Collection<BankTransaction> getLastNTransactions(String accountId, int qnt) {
		return this.transactionDAO.getLastNTransactions(accountId, qnt);
	}

	/**
	 * Transfers a amount from the system account to a given domain account
	 */
	public void insertBallast(String domainAccount, BigDecimal amount, String comment) {
		this.transact(SystemAccount.ID, domainAccount, amount, comment, TransactionType.BALLAST);
	}

	/**
	 * Gets a account's balance 
	 */
	public BigDecimal getBalance(String accountId) {
		return this.getBalance(this.getValidAccount(accountId));
	}

	/**
	 * Get all {@link BankAccount}'s transactions for a given date range. 
	 */
	public Collection<BankTransaction> getTransactionsByDateRange(String accountId, Date startDate, Date endDate) {
		return this.transactionDAO.getTransactionsByDateRange(accountId, startDate, endDate);
	}

	/**
	 * Makes a transfer between to accounts.
	 */
	public void transact(String accountOrigId, String accountDestId, BigDecimal value, String comment, TransactionType type) {
		BankTransaction transaction = this.createTransaction(accountOrigId, accountDestId, value, comment, type);
		BankAccount accountOrig = this.getValidAccount(accountOrigId);
		// don't need to store the destination account. The method is called only to ensure that the
		// destination account exists (or will be created) and is valid
		this.getValidAccount(accountDestId);

		if (this.hasSufficientBalance(accountOrig, value)) {
			this.transactionDAO.save(transaction);
		} else {
			logger.info(accountOrigId + " has insufficient balance to make this transaction (" + transaction + ") ");
			throw new InsufficientBalanceException(accountOrigId + " has insufficient balance to make transaction");
		}
	}

	/**
	 * Gets the ball
	 */
	protected BigDecimal getBalance(BankAccount account) {
		account.setTransactionInfoService(this.transactionDAO);
		return account.getBalance();
	}

	/**
	 * Checks if the given account funds' is greater than a given value.
	 */
	protected boolean hasSufficientBalance(BankAccount account, BigDecimal value) {
		return this.getBalance(account).compareTo(value) >= 0;
	}

	/**
	 * Gets a valid account. If there's no {@link BankAccount} for thr given account id, it creates
	 * the account.
	 */
	protected BankAccount getValidAccount(String accountId) {
		BankAccount account = this.accountDAO.get(accountId);
		if (account == null) {
			this.createAccount(accountId);
			return this.getValidAccount(accountId);
		} else if (!account.isEnabled()) {
			logger.info("'" + accountId + "' account is disabled.");
			throw new DisabledBankAccountException("'" + accountId + "' account is disabled.");
		} else {
			return account;
		}
	}

	/**
	 * Creates a bank account
	 */
	private void createAccount(String ownerId) {
		BankAccount account = new BankAccount(ownerId);
		this.accountDAO.save(account);
	}

	private BankTransaction createTransaction(String accountOrig, String accountDest, BigDecimal value, String comment, TransactionType type) {
		return new BankTransaction(accountOrig, accountDest, value, type, comment);
	}
}