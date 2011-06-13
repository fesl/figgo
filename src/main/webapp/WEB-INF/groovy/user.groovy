def actions = ['create', 'new']

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def post_create() {
	usersFacade.createUser(request.user.email, params.name, params.phoneNumber, params.avatar, params.description)
	redirect '/dashboard'
}

def get_new() {
	render 'user/new.vm', request, response
}

"$actionCall"()