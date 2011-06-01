def actions = ['create', 'new']
def action = "notfound"

if (params.action) {
	action = "action_" + params.action
}

def action_create() {
	usersFacade.createUser(request.user.email, params.name, params.phoneNumber, params.avatar, params.description)
	redirect '/dashboard'
}

def action_new() {
	render 'user/new.vm', request, response
}

"$action"()