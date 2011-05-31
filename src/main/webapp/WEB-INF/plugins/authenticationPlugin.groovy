import br.octahedron.straight.modules.users.UsersExternalFacade
import br.octahedron.straight.modules.users.manager.UsersManager
import com.google.appengine.api.users.UserServiceFactory

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.authenticatePlugin")
def userService = UserServiceFactory.getUserService()
def usersFacade = new UsersExternalFacade()

before {
	request.user = userService.getCurrentUser()
	uri = request.getAttribute("javax.servlet.forward.request_uri")
	if (uri == "/") {
		if (request.user == null) {
			request.login_url = userService.createLoginURL("/")
		} else {
			response.sendRedirect "/dashboard"
		}
	} else {
		if (request.user) {
			request.logout_url = userService.createLogoutURL("/")
			if (!usersFacade.existsUser(request.user.email) && !uri.contains("/user") && !userService.userAdmin) {
				response.sendRedirect "/user/new"
			}
		} else {
			response.sendRedirect userService.createLoginURL(uri)
		}
	}
}