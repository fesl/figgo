def actions = ['index', 'transfer']

if (actions.contains(params.action)) {
	actionCall = "action_" + request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def action_get_index() {
	render 'bank/index.vm', request, response
}

def action_get_transfer() {
	render 'bank/transfer.vm', request, response
}

def action_post_transfer() {
	redirect 'http://mundo.figgo.com.br:8080'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()