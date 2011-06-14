import br.octahedron.straight.modules.ManagerBuilder
import com.google.appengine.api.users.UserServiceFactory

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.authenticatePlugin")
def userService = UserServiceFactory.getUserService()
def usersManager = ManagerBuilder.getUserManager()

before {
	request.user = userService.getCurrentUser()
	uri = request.getAttribute("javax.servlet.forward.request_uri")
	
	if (uri == "/") {
		if (request.user) {
			response.sendRedirect "/dashboard"
		} else {
			request.login_url = userService.createLoginURL('/dashboard')
		}
	} else {
		if (!request.user) {
			response.sendRedirect userService.createLoginURL(uri)
		} else {
			request.logout_url = userService.createLogoutURL('/')
			if (!usersManager.existsUser(request.user.email) && !uri.contains("/user/")) {
				response.sendRedirect '/user/new'
			}
		}
	}
}