import java.math.BigDecimal
import java.util.ArrayList
import br.octahedron.commons.database.NamespaceCommons
import br.octahedron.straight.modules.ManagerBuilder
import br.octahedron.straight.modules.bank.data.BankTransaction.TransactionType
import br.octahedron.commons.util.Formatter

zero = new BigDecimal("0.00")
actions = ['index', 'transfer', 'statement', 'admin', 'ballast', 'share']
configurationManager = ManagerBuilder.getConfigurationManager()
accountManager = ManagerBuilder.getAccountManager()
usersManager = ManagerBuilder.getUserManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

request.formatter = Formatter
request.domain = configurationManager.getDomainConfiguration()

def get_index() {
	request.balance = accountManager.getBalance(request.user.email)
	request.transactions = accountManager.getLastNTransactions(request.user.email, 5)
	render 'bank/index.vm', request, response
}

def get_admin() {
	request.balance = accountManager.getBalance(extractDomainName(request.serverName))
	render 'bank/admin.vm', request, response
}

def get_transfer() {
	request.balance = accountManager.getBalance(request.user.email)
	render 'bank/transfer.vm', request, response
}

def get_statement() {
	request.balance = accountManager.getBalance(request.user.email)
	render 'bank/statement-choice.vm', request, response
}

def post_transfer() {
	def errors = validateTransaction(params)
	if (errors.isEmpty()) {
		accountManager.transact(request.user.email, params.userId.trim(), new BigDecimal(params.amount.trim()), params.comment, TransactionType.valueOf(params.type))
		redirect '/bank'
	} else {
		request.balance = accountManager.getBalance(request.user.email)
		request.errors = errors
		request.userId = params.userId
		request.amount = params.amount
		request.comment = params.comment
		request.type = params.type
		render 'bank/transfer.vm', request, response
	}
}

def post_share() {
	def errors = validateDest(params)
	if (errors.isEmpty()) {
		accountManager.transact(extractDomainName(request.serverName), params.userId.trim(), new BigDecimal(params.amount.trim()), params.comment, TransactionType.valueOf(params.type))
		redirect '/bank/admin'
	} else {
		request.balance = accountManager.getBalance(extractDomainName(request.serverName))
		request.errors = errors
		request.userId = params.userId
		request.amount = params.amount
		request.comment = params.comment
		request.type = params.type
		render 'bank/admin.vm', request, response
	}
}

def post_ballast() {
	def errors = validateValue(params)
	if (errors.isEmpty()) {
		accountManager.insertBallast(extractDomainName(request.serverName), new BigDecimal(params.amount.trim()), params.comment)
		redirect '/bank/admin'
	} else {
		request.balance = accountManager.getBalance(extractDomainName(request.serverName))
		request.errors = errors
		request.amount = params.amount
		request.comment = params.comment
		request.type = params.type
		render 'bank/admin.vm', request, response
	}
}

def notfound() {
	render 'notfound.vm', request, response
}

def validateValue(params) {
	def errors = []
	try { 
		amount = new BigDecimal(params.amount.trim())
		if (amount.compareTo(zero) <= 0) {
			errors.add("Valor inválido")
		}
	} catch (NumberFormatException e)	{
		e.printStackTrace();
		errors.add("Valor inválido") 
	}
	return errors
}

def validateDest(params) {
	def errors = []
	try {
		NamespaceCommons.changeToGlobalNamespace()
		if (! usersManager.existsUser(params.userId.trim())) {
			errors.add("Conta de destino não existe") 
		}
	} finally {
		NamespaceCommons.changeToPreviousNamespace()
	}
	
	errors.addAll(validateValue(params))
	return errors
}

def validateTransaction(params) {
	def errors = []
	if (request.user.email.equals(params.userId.trim())) {
		errors.add("Conta de destino é a conta de origem") 
	}

	errors.addAll(validateDest(params))
	return errors
}

"$actionCall"()