actions = ['edit', 'upload']

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_edit() {
	request.domain = configurationFacade.getDomainConfiguration()
	render 'domain/edit.vm', request, response
}

def get_upload() {
	request.upload_url = blobstore.createUploadUrl('/blob/domain/upload')
	render 'domain/upload.vm', request, response
}

def post_edit() {
	configurationFacade.saveDomainConfiguration(params.name)
	redirect '/'
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()
