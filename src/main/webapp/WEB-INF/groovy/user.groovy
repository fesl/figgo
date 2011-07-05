import br.octahedron.straight.modules.ManagerBuilder
import java.util.regex.Pattern
import java.util.regex.Matcher

actions = ['create', 'new', 'upload', 'edit', 'update', 'search']
usersManager = ManagerBuilder.getUserManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_new() {
	render 'user/new.vm', request, response
}

def get_edit() {
	request.user = usersManager.getUser(request.user.email)
	request.name = request.user.name
	request.phoneNumber = request.user.phoneNumber
	request.description = request.user.description
	render 'user/edit.vm', request, response
}

def get_search() {
	def result = usersManager.getUsersStartingWith(params.term)
	renderJSON result, request, response
}

def get_upload() {
	request.user = usersManager.getUser(request.user.email)
	request.upload_url = blobstore.createUploadUrl('/blob/user/upload')
	render 'user/upload.vm', request, response
}

def post_create() {
	def errors = validateUser(params)
	if (errors.isEmpty()) {
		usersManager.createUser(request.user.email, params.name?.trim(), params.phoneNumber?.trim(), params.description)
		redirect '/'
	} else {
		request.errors = errors
		request.name = params.name
		request.phoneNumber = params.phoneNumber
		request.description = params.description
		render 'user/new.vm', request, response
	}
}

def post_update() {
	def errors = validateUser(params)
	if (errors.isEmpty()) {
		usersManager.updateUser(request.user.email, params.name?.trim(), params.phoneNumber?.trim(), params.description)
		redirect '/'
	} else {
		request.errors = errors
		request.name = params.name
		request.phoneNumber = params.phoneNumber
		request.description = params.description
		render 'user/edit.vm', request, response
	}
}

def notfound() {
	render 'notfound.vm', request, response	
}

// validation patterns
namePattern = Pattern.compile('([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûÃÕãõçÇ] ){2,}')
phonePattern = Pattern.compile('^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$')

def validateUser(params) {
	def phone = params.phoneNumber.trim()
	def name = params.name.trim()
	def errors = []
	if (!namePattern.matcher(name).matches()) {
	    errors.add("Nome inválido")
	}
	if (!phonePattern.matcher(phone).matches()) {
	    errors.add("Telefone inválido")
	}
	return errors
}

"$actionCall"()
