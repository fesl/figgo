import br.octahedron.straight.modules.ManagerBuilder

actions = ['create', 'new', 'dashboard', 'upload', 'edit', 'update']
usersManager = ManagerBuilder.getUserManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def post_create() {
	(isValid, errors) = userValidation(params)
	if (isValid) {
		usersManager.createUser(request.user.email, name, phone, params.description)
		redirect '/dashboard'
	} else {
		request.errors = errors
		request.name = params.name
		request.phoneNumber = params.phoneNumber
		request.description = params.description
		render 'user/new.vm', request, response
	}
}

def get_new() {
	render 'user/new.vm', request, response
}

def get_dashboard() {
	request.user = usersManager.getUser(request.user.email)
	render 'user/dashboard.vm', request, response
}

def get_edit() {
	loggedUser = usersManager.getUser(request.user.email)
	request.name = loggedUser.name
	request.phoneNumber = loggedUser.phoneNumber
	request.description = loggedUser.description
	render 'user/edit.vm', request, response
}

def post_update() {
	(isValid, errors) = userValidation(params)
	if (isValid) {
		usersManager.updateUser(request.user.email, params.name, params.phoneNumber, params.description)
		redirect '/dashboard'
	} else {
		request.errors = errors
		request.name = params.name
		request.phoneNumber = params.phoneNumber
		request.description = params.description
		render 'user/edit.vm', request, response
	}
}

def get_upload() {
	request.upload_url = blobstore.createUploadUrl("/upload")
	render 'user/upload.vm', request, response
}

def notfound() {
	render 'notfound.vm', request, response	
}

"$actionCall"()
