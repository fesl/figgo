actions = ['edit']

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_edit() {
	request.domain = configurationFacade.getDomainConfiguration()
	render 'domain/edit.vm', request, response
}

def post_edit() {
	configurationFacade.saveDomainConfiguration(params.name, params.description)
	redirect '/'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()
