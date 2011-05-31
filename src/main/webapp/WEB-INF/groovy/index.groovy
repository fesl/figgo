def index() {
	if (request.serverName != "figgo.com.br") {
		forward 'index.vm'
	} else {
		// request.domain = configurationFacade.getDomainConfiguration(request.serverName)
		if (!request.domain.isEmpty())
			forward 'domain/index.vm'
		else
			redirect '/domain/edit'
	}
}

index()