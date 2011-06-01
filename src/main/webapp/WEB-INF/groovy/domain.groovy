def action = 'notfound'

if (params.action) {
	action = "action_" + request.method.toLowerCase() + "_" + params.action
}

def action_get_edit() {
	// request.domain = configurationFacade.getDomainConfiguration(request.serverName)
	render 'domain/edit.vm', request, response
}

def action_post_edit() {
	// configurationFacade.saveDomainConfiguration(request.serverName, params.name)
	redirect 'http://mundo.figgo.com.br:8080'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$action"()