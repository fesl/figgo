import br.octahedron.straight.modules.ManagerBuilder

actions = ['edit', 'upload']
configurationManager = ManagerBuilder.getConfigurationManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_edit() {
	request.domain = configurationManager.getDomainConfiguration()
	render 'domain/edit.vm', request, response
}

def get_upload() {
	request.upload_url = blobstore.createUploadUrl('/blob/domain/upload')
	render 'domain/upload.vm', request, response
}

def post_edit() {
	configurationManager.updateDomainConfiguration(params.name)
	redirect '/'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()
