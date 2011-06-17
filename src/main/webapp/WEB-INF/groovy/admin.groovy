import br.octahedron.straight.modules.ManagerBuilder
import  br.octahedron.straight.modules.admin.util.DomainAlreadyExistsException

action = 'notfound'
adminManager = ManagerBuilder.getAdminManager()
authManager = ManagerBuilder.getAuthorizationManager()

if (params.module && params.action) {
	action = "action_" + params.module + "_" + request.method.toLowerCase() + "_" + params.action
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
	try {
		adminManager.createDomain(params.name, params.userId)
		redirect '/'
 	} catch (DomainAlreadyExistsException e) {
 		request.error = e.getMessage()
 		render 'domain/new.vm', request, response	
 	}
}


def action_domain_get_new() {
	render 'domain/new.vm', request, response
}

def notfound() {
	render 'notfound.vm', request, response
}

// gabiarra
def action_role_get_add() {
	authManager.addUsersToRole(params.domain, params.role, params.logins.split(","))
}
"$action"()
