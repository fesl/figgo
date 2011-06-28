import br.octahedron.commons.database.NamespaceCommons
import br.octahedron.straight.modules.ManagerBuilder

usersManager = ManagerBuilder.getUserManager()
configurationManager = ManagerBuilder.getConfigurationManager()
authorizationManager = ManagerBuilder.getAuthorizationManager()

def index() {
	if (request.serverName == "www.figgo.com.br" || request.serverName == "localhost") {
		if (request.user) {
			request.domains = authorizationManager.getUserDomains(request.user.email)
			request.user = usersManager.getUser(request.user.email)
			render 'user/dashboard.vm', request, response		
		} else {
			render 'index.vm', request, response
		}
	} else {
		request.domain = configurationManager.getDomainConfiguration()
		try {
			NamespaceCommons.changeToGlobalNamespace()
			userExists = authorizationManager.getUserDomains(request.user?.email).contains(request.domain.domainName)
		} finally {
			NamespaceCommons.changeToPreviousNamespace()
		}

		if (userExists) {
			render 'domain/index.vm', request, response
		} else {
			render 'domain/public_index.vm', request, response
		}
	}
}

index()