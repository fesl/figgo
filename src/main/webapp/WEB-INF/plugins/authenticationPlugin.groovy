import br.octahedron.straight.modules.users.UsersExternalFacade
import com.google.appengine.api.users.UserServiceFactory

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.authenticatePlugin")
def userService = UserServiceFactory.getUserService()
def usersFacade = new UsersExternalFacade()

before {
	request.user = userService.getCurrentUser()
	uri = request.getAttribute("javax.servlet.forward.request_uri")
	if (!request.user) {
		response.sendRedirect userService.createLoginURL('http://' + request.serverName + ':' + request.serverPort + uri)
	} else if (!userService.userAdmin) {
		if (request.user) {
			if (uri != "/") {
				request.logout_url = userService.createLogoutURL("http://figgo.com.br/")
				if (!usersFacade.existsUser(request.user.email) && !uri.contains("/user/")) {
					response.sendRedirect 'http://figgo.com.br:' + request.serverPort + '/user/new'
				}
			} else {
				response.sendRedirect "/dashboard"
			}
		} else {
			response.sendRedirect userService.createLoginURL(uri)
		}
	}
}