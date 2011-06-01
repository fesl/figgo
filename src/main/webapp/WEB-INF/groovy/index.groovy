def index() {
	if (request.serverName == "figgo.com.br") {
		render 'index.vm', request, response
	} else {
		// request.domain = configurationFacade.getDomainConfiguration(request.serverName)
		//if (!request.domain.isEmpty())
			render 'domain/index.vm', request, response
		//else
		//	redirect '/domain/edit'
	}
}

index()