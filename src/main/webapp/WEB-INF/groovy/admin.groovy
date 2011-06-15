import br.octahedron.straight.modules.configuration.data.DomainConfiguration

action = 'notfound'
adminManager = ManagerBuilder.getAdminManager()

if (params.module && params.action) {
	action = "action_" + params.module + "_" + request.method.toLowerCase() + "_" + params.action
}

def notfound() {
	render 'notfound.vm', request, response
}

// APP CONFIGURATION
def action_app_get_config() {
	if (adminManager.hasApplicationConfiguration()) {
		appConfiguration = adminManager.getApplicationConfiguration()
		request.accessKey = appConfiguration.route53AccessKeyID
		request.keySecret = appConfiguration.route53AccessKeySecret
		request.zone = appConfiguration.route53AccessKeySecret
	}
	render 'admin/config.vm', request, response
}

def action_app_post_config() {
	adminManager.configureApplication(params.accessKey, params.keySecret, params.zone)
	redirect '/admin/domain/new'
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