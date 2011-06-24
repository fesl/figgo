import br.octahedron.straight.modules.ManagerBuilder
import br.octahedron.straight.modules.admin.util.DomainAlreadyExistsException

action = 'notfound'
adminManager = ManagerBuilder.getAdminManager()
authManager = ManagerBuilder.getAuthorizationManager()

if (params.module && params.action) {
	action = request.method.toLowerCase() + "_" + params.module + "_" + params.action
}

// APP CONFIGURATION
def get_app_config() {
	if (adminManager.hasApplicationConfiguration()) {
		appConfiguration = adminManager.getApplicationConfiguration()
		request.accessKey = appConfiguration.route53AccessKeyID
		request.keySecret = appConfiguration.route53AccessKeySecret
		request.zone = appConfiguration.route53AccessKeySecret
	}
	render 'admin/config.vm', request, response
}

def post_app_config() {
	adminManager.configureApplication(params.accessKey, params.keySecret, params.zone)
	redirect '/admin/domain/new'
}

// DOMAIN CONFIGURATION
def get_domain_new() {
	render 'domain/new.vm', request, response
}

def post_domain_create() {
	try {
		adminManager.createDomain(params.name, params.userId)
		redirect '/'
 	} catch (DomainAlreadyExistsException e) {
 		request.error = e.getMessage()
 		render 'domain/new.vm', request, response	
 	}
}

def notfound() {
	render 'notfound.vm', request, response
}

// gabiarra
def get_role_add() {
	authManager.addUsersToRole(params.domain, params.role, params.logins.split(","))
}
"$action"()
