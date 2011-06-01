import br.octahedron.straight.modules.configuration.data.DomainConfiguration

def action = 'notfound'

if (params.module && params.action) {
	action = "action_" + params.module + "_" + request.method.toLowerCase() + "_" + params.action
}

def notfound() {
	render 'notfound.vm', request, response
}

// DOMAIN CONFIGURATION
def action_domain_post_create() {
	adminFacade.createDomain(params.name + "." + request.serverName)
	redirect 'http://' + params.name + '.figgo.com.br:8080'
}

def action_domain_get_new() {
	render 'domain/new.vm', request, response
}

"$action"()