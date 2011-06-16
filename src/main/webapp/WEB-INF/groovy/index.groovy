import br.octahedron.straight.modules.ManagerBuilder

usersManager = ManagerBuilder.getUserManager()

def index() {
	if (request.serverName == "figgo.com.br" || request.serverName == "localhost") {
		if (request.user) {
			request.user = usersManager.getUser(request.user.email)
			render 'user/dashboard.vm', request, response		
		} else {
			render 'index.vm', request, response
		}
	} else {
		request.domain = configurationFacade.getDomainConfiguration()
		if (!request.domain.isEmpty()) {
			render 'domain/index.vm', request, response
		} else {
			redirect '/domain/edit'
		}
	}
}

index()