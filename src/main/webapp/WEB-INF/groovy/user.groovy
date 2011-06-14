import br.octahedron.straight.modules.ManagerBuilder

def actions = ['create', 'new', 'dashboard']
usersManager = ManagerBuilder.getUserManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def post_create() {
	usersManager.createUser(request.user.email, params.name, params.phoneNumber, params.avatar, params.description)
	redirect '/dashboard'
}

def get_new() {
	render 'user/new.vm', request, response
}

def get_dashboard() {
	request.user = usersManager.getUser(request.user.email)
	render 'user/dashboard.vm', request, response
}

"$actionCall"()