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

import static br.octahedron.figgo.util.DateUtil.getFirstDateOfCurrentMonth;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import br.octahedron.figgo.modules.bank.data.BankAccount;
import br.octahedron.figgo.modules.bank.data.BankAccountDAO;
import br.octahedron.figgo.modules.bank.data.BankTransaction;
import br.octahedron.figgo.modules.bank.data.BankTransaction.TransactionType;
import br.octahedron.figgo.modules.bank.data.BankTransactionDAO;
import br.octahedron.figgo.modules.bank.data.SystemAccount;
import br.octahedron.util.DateUtil;

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
	 * 
	 * @throws InsufficientBalanceException
	 *             If the origin account hasn't sufficient funds
	 * @throws DisabledBankAccountException
	 *             If some of the given accounts are disabled
	 */
	public void insertBallast(String domainAccount, BigDecimal amount, String comment) throws InsufficientBalanceException,
			DisabledBankAccountException {
		this.transact(SystemAccount.ID, domainAccount, amount, comment, TransactionType.BALLAST);
	}

	/**
	 * Gets a account's balance
	 * 
	 * @throws DisabledBankAccountException
	 *             If the given account is disabled
	 */
	public BigDecimal getBalance(String accountId) throws DisabledBankAccountException {
		return this.getBalance(this.getValidAccount(accountId));
	}

	/**
	 * Get all {@link BankAccount}'s transactions for a given date range.
	 */
	public Collection<BankTransaction> getTransactions(String accountId, Date startDate, Date endDate) {
		return this.transactionDAO.getTransactionsByDateRange(accountId, startDate, endDate);
	}

	/**
	 * Makes a transfer between to accounts.
	 * 
	 * @throws InsufficientBalanceException
	 *             If the origin account hasn't sufficient funds
	 * @throws DisabledBankAccountException
	 *             If some of the given accounts are disabled
	 */
	public void transact(String accountOrigId, String accountDestId, BigDecimal value, String comment, TransactionType type)
			throws InsufficientBalanceException, DisabledBankAccountException {
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
	 * Returns the current amount of transactions of the current month.
	 * 
	 * @return an amount representing the sum of all transactions made on the current month
	 */
	public BigDecimal getCurrentAmountTransactions() {
		return this.transactionDAO.getAllAmountByDateRange(getFirstDateOfCurrentMonth(), DateUtil.getDate());
	}

	/**
	 * Returns the amount of transactions specified by a date range
	 * 
	 * @param startDate
	 *            startDate of the range
	 * @param endDate
	 *            endDate of the range
	 * 
	 * @return an amount representing the sum of all transactions made on the date range
	 */
	public BigDecimal getAmountTransactions(Date startDate, Date endDate) {
		return this.transactionDAO.getAllAmountByDateRange(startDate, endDate);
	}

	/**
	 * Returns the current amount credit by bank on the current month.
	 * 
	 * @return an amount representing the sum of all credit transactions that has bank as
	 *         destination
	 */
	public BigDecimal getCurrentCreditAmount(String domainAccount) {
		return this.transactionDAO.getAmountCreditByDateRange(domainAccount, getFirstDateOfCurrentMonth(), DateUtil.getDate());
	}

	/**
	 * Returns the bank amount credit specified by a date range
	 * 
	 * @param startDate
	 *            startDate of the range
	 * @param endDate
	 *            endDate of the range
	 * 
	 * @return an amount representing the sum of all credit transactions that has bank as
	 *         destination except when origin is {@link SystemAccount}
	 */
	public BigDecimal getCreditAmount(String domainAccount, Date startDate, Date endDate) {
		return this.transactionDAO.getAmountCreditByDateRange(domainAccount, startDate, endDate);
	}

	/**
	 * Returns the whole amount added on the bank by admin.
	 * 
	 * @return an amount representing the whole injected amount on the bank
	 */
	public BigDecimal getBallast() {
		return this.transactionDAO.getBallast();
	}

	/**
	 * Gets the an account balance
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
	 * Gets a valid account. If there's no {@link BankAccount} for the given account id, it creates
	 * the account.
	 * 
	 * @throws DisabledBankAccountException
	 *             If the given account is disabled
	 */
	protected BankAccount getValidAccount(String accountId) throws DisabledBankAccountException {
		BankAccount account = this.accountDAO.get(accountId);
		if (account == null) {
			this.createAccount(accountId);
			return this.getValidAccount(accountId);
		} else if (!account.isEnabled()) {
			logger.info("'" + accountId + "' account is disabled.");
			throw new DisabledBankAccountException(accountId);
		} else {
			return account;
		}
	}

	/**
	 * Creates a new bank account
	 */
	private void createAccount(String ownerId) {
		BankAccount account = new BankAccount(ownerId);
		this.accountDAO.save(account);
	}

	/**
	 * Creates a {@link BankTransaction} to be persisted.
	 * 
	 * @param accountOrig
	 *            The origin account id
	 * @param accountDest
	 *            The destination account id
	 * @param value
	 *            The transaction absolute value
	 * @param comment
	 *            The transaction comment
	 * @param type
	 *            The transaction type.
	 * @return The {@link BankTransaction}
	 */
	private BankTransaction createTransaction(String accountOrig, String accountDest, BigDecimal value, String comment, TransactionType type) {
		return new BankTransaction(accountOrig, accountDest, value, type, comment);
	}

	/**
	 * Gets all account balances. 
	 * @return a HashMap with tke accountID as the key and the account's balance as the value.
	 * @throws DisabledBankAccountException
	 * 
	 */
	public HashMap<String, BigDecimal> getAllBalances() throws DisabledBankAccountException {

		HashMap<String, BigDecimal> balances = new HashMap<String, BigDecimal>();
		Collection<BankAccount> accounts = accountDAO.getAll();
		for (BankAccount account : accounts)
			balances.put(account.getOwnerId(), getBalance(account.getOwnerId()));
		return balances;
	}
}