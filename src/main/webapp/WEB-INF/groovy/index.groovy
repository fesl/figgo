import br.octahedron.straight.modules.ManagerBuilder

usersManager = ManagerBuilder.getUserManager()
configurationManager = ManagerBuilder.getConfigurationManager()

def index() {
	if (request.serverName == "www.figgo.com.br" || request.serverName == "localhost") {
		if (request.user) {
			request.user = usersManager.getUser(request.user.email)
			render 'user/dashboard.vm', request, response		
		} else {
			render 'index.vm', request, response
		}
	} else {
		request.domain = configurationManager.getDomainConfiguration()
		render 'domain/index.vm', request, response
	}
}

index()