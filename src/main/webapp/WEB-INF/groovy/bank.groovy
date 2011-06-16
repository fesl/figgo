import br.octahedron.straight.modules.ManagerBuilder

actions = ['index', 'transfer', 'statement', 'admin', 'ballast', 'share']
configurationManager = ManagerBuilder.getConfigurationManager()
accountManager = ManagerBuilder.getAccountManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_index() {
	request.domain = configurationManager.getDomainConfiguration()
	request.balance = accountManager.getBalance(request.user.email)
	render 'bank/index.vm', request, response
}

def get_admin() {
	request.domain = configurationManager.getDomainConfiguration()
	request.balance = accountManager.getBalance(extractDomainName(request.serverName))
	render 'bank/admin.vm', request, response
}

def post_share() {
	accountManager.transact(extractDomainName(request.serverName), params.userId, params.amount, params.comment, params.type)
	redirect '/bank/admin', request, response
}

def post_ballast() {
	accountManager.insertBallast(extractDomainName(request.serverName), params.amount, params.comment)
	redirect '/bank/admin', request, response
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
	redirect '/'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()