import java.math.BigDecimal
import br.octahedron.straight.modules.ManagerBuilder
import br.octahedron.straight.modules.bank.data.BankTransaction.TransactionType
import br.octahedron.commons.util.Formatter

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

def get_index() {
	request.domain = configurationManager.getDomainConfiguration()
	request.balance = accountManager.getBalance(request.user.email)
	request.transactions = accountManager.getLastNTransactions(request.user.email, 5)
	render 'bank/index.vm', request, response
}

def get_admin() {
	request.domain = configurationManager.getDomainConfiguration()
	request.balance = accountManager.getBalance(extractDomainName(request.serverName))
	render 'bank/admin.vm', request, response
}

def get_transfer() {
	request.domain = configurationManager.getDomainConfiguration()
	request.balance = accountManager.getBalance(request.user.email)
	render 'bank/transfer.vm', request, response
}

def get_statement() {
	request.domain = configurationManager.getDomainConfiguration()
	request.balance = accountManager.getBalance(request.user.email)
	render 'bank/statement-choice.vm', request, response
}

def post_transfer() {
	(isValid, errors) = validateTransaction(params)
	if (isValid) {
		accountManager.transact(request.user.email, params.userId, new BigDecimal(params.amount), params.comment, TransactionType.valueOf(params.type))
		redirect '/bank'
	} else {
		request.errors = errors
		request.userId = params.userId
		request.amount = params.amount
		request.comment = params.comment
		request.type = params.type
	}
}

def post_share() {
	(isValid, errors) = validateTransaction(params)
	if (isValid) {
		accountManager.transact(extractDomainName(request.serverName), params.userId, new BigDecimal(params.amount), params.comment, TransactionType.valueOf(params.type))
		redirect '/bank/admin'
	} else {
		request.errors = errors
		request.userId = params.userId
		request.amount = params.amount
		request.comment = params.comment
		request.type = params.type
	}
}

def post_ballast() {
	(isValid, errors) = validateTransaction(params)
	if (isValid) {
		accountManager.insertBallast(extractDomainName(request.serverName), new BigDecimal(params.amount), params.comment)
		redirect '/bank/admin'
	} else {
		request.errors = errors
		request.amount = params.amount
		request.comment = params.comment
		request.type = params.type
	}
}

def notfound() {
	render 'notfound.vm', request, response
}

def validateTransaction(params) {
	isValid = true
	errors = []
	count = 0
	if (! usersManager.existsUser(params.userId) {
		errors[count++] = "Conta de destino não existe" 
		isValid = false
	}
	
	try { 
		amount = new BigDecimal(params.amount)
		if (new BigDecimal(0).compareTo(amount) <= 0) {
			errors[count++] = "Valor inválido" 
			isValid = false
		}
	catch (NumberFormatException e)	{
		errors[count++] = "Valor inválido" 
		isValid = false
	}
	
	return [isValid, errors]
}

"$actionCall"()