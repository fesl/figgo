import br.octahedron.straight.modules.ManagerBuilder
import java.util.regex.Pattern
import java.util.regex.Matcher

actions = ['create', 'new', 'dashboard', 'upload', 'edit', 'update']
usersManager = ManagerBuilder.getUserManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def post_create() {
	(isValid, errors) = validateUser(params)
	if (isValid) {
		usersManager.createUser(request.user.email, params.name?.trim(), params.phoneNumber?.trim(), params.description)
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
	(isValid, errors) = validateUser(params)
	if (isValid) {
		usersManager.updateUser(request.user.email, params.name?.trim(), params.phoneNumber?.trim(), params.description)
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
	request.upload_url = blobstore.createUploadUrl('/blob/user/upload')
	render 'user/upload.vm', request, response
}

def notfound() {
	render 'notfound.vm', request, response	
}

// validation patterns
namePattern = Pattern.compile('([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûçÇ] *){2,}')
phonePattern = Pattern.compile('^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$')

def validateUser(params) {
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
	return [isValid, errors]
}

"$actionCall"()
