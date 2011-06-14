import br.octahedron.straight.modules.ManagerBuilder

def actions = ['create', 'new', 'dashboard', 'upload', 'upload_confirm']
usersManager = ManagerBuilder.getUserManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def post_create() {
	usersManager.createUser(request.user.email, params.name, params.phoneNumber, params.description)
	redirect '/dashboard'
}

def get_new() {
	render 'user/new.vm', request, response
}

def get_dashboard() {
	request.user = usersManager.getUser(request.user.email)
	render 'user/dashboard.vm', request, response
}

def get_upload() {
	request.upload_url = blobstore.createUploadUrl("/upload")
	render 'user/upload.vm', request, response
}

"$actionCall"()