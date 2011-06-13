def actions = ['edit']

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_edit() {
	// request.domain = configurationFacade.getDomainConfiguration(request.serverName)
	render 'domain/edit.vm', request, response
}

def post_edit() {
	// configurationFacade.saveDomainConfiguration(request.serverName, params.name)
	redirect 'http://mundo.figgo.com.br:8080'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()