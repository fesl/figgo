def action = 'notfound'

if (params.action) {
	action = "action_" + request.method.toLowerCase() + "_" + params.action
}

def action_get_edit() {
	// request.domain = configurationFacade.getDomainConfiguration(request.serverName)
	forward 'domain/edit.vm'
}

def action_post_edit() {
	// configurationFacade.saveDomainConfiguration(request.serverName, params.name)
	redirect 'http://mundo.figgo.com.br:8080'
}

def notfound() {
	forward 'notfound.vm'
}

"$action"()