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
	 * @param ownerId
	 * @return
	 */
	public BankAccount createAccount(String ownerId) {
		BankAccount account = new BankAccount(ownerId);
		this.accountDAO.save(account);
		return account;
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
	 * TODO!
	 * 
	 * @param domainAccount
	 * @param amount
	 * @param comment
	 */
	public void insertBallast(String domainAccount, BigDecimal amount, String comment) {
		this.transact(SystemAccount.ID, domainAccount, amount, comment, TransactionType.TRANSFER);
	}

	public BigDecimal getBalance(String accountId) {
		try {
			return this.getBalance(this.getValidAccount(accountId));
		} catch (InexistentBankAccountException e) {
			return this.getBalance(this.createAccount(accountId));
		}
	}

	protected BigDecimal getBalance(BankAccount account) {
		account.setTransactionInfoService(this.transactionDAO);
		return account.getBalance();
	}

	/**
	 * @param accountOrig
	 * @param accountDest
	 * @param value
	 * @param comment
	 * @param type
	 */
	public void transact(String accountOrigId, String accountDestId, BigDecimal value, String comment, TransactionType type) {
		BankTransaction transaction = this.createTransaction(accountOrigId, accountDestId, value, comment, type);
		BankAccount accountOrig = this.getValidAccount(accountOrigId);
		this.getValidAccount(accountDestId); // just to check validation of destination

		if (this.hasSufficientBalance(accountOrig, value)) {
			this.transactionDAO.save(transaction);
		} else {
			logger.info(accountOrigId + " has insufficient balance to make this transaction (" + transaction + ") ");
			throw new InsufficientBalanceException(accountOrigId + " has insufficient balance to make transaction");
		}
	}

	private BankTransaction createTransaction(String accountOrig, String accountDest, BigDecimal value, String comment, TransactionType type) {
		return new BankTransaction(accountOrig, accountDest, value, type, comment);
	}

	protected boolean hasSufficientBalance(BankAccount account, BigDecimal value) {
		return this.getBalance(account).compareTo(value) >= 0;
	}

	protected BankAccount getValidAccount(String accountId) {
		BankAccount account = this.accountDAO.get(accountId);
		if (account == null) {
			logger.info("Does not exist an account associated to '" + accountId + "' id on bank");
			throw new InexistentBankAccountException("Does not exist an account associated to '" + accountId + "' id on bank");
		} else if (!account.isEnabled()) {
			logger.info("'" + accountId + "' account is disabled.");
			throw new DisabledBankAccountException("'" + accountId + "' account is disabled.");
		} else {
			return account;
		}
	}
}