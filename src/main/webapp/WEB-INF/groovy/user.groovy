import br.octahedron.straight.Facade;

def actions = ['create', 'new']
def action = "notfound"

if (params.action) {
	action = "action_" + params.action
}

def action_create() {
	Facade.getInstance().createUser(params.userId, params.name, params.phoneNumber, params.avatar, params.description)
	redirect '/dashboard'
}

def action_new() {
	forward 'user/new.vm'
}

"$action"()