import br.octahedron.straight.modules.ManagerBuilder
import java.util.regex.Pattern
import java.util.regex.Matcher

def actions = ['create', 'new', 'dashboard']
usersManager = ManagerBuilder.getUserManager()

// validation patterns
namePattern = Pattern.compile('([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûçÇ] *){2,}')
phonePattern = Pattern.compile('^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$')

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def post_create() {
	// do validation
	phone = params.phoneNumber.trim()
	name = params.name.trim()
	isValid = true
	errors = []
	count = 0
	if (!namePattern.matcher(name).matches()) {
	    errors[count++] = "Nome inválido"
	    isValid = false
	}
	if (!phonePattern.matcher(phone).matches()) {
	    errors[count++] = "Telefone inválido"
	    isValid = false
	}
	
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

"$actionCall"()