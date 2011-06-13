def actions = ['index', 'transfer', 'statement']

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_index() {
	render 'bank/index.vm', request, response
}

def get_transfer() {
	render 'bank/transfer.vm', request, response
}

def get_statement() {
	render 'bank/statement-choice.vm', request, response
}

def post_transfer() {
	redirect 'http://mundo.figgo.com.br:8080'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()