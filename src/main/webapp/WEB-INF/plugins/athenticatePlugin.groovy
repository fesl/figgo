import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import groovyx.gaelyk.logging.GroovyLogger
import groovyx.gaelyk.plugins.PluginsHandler
import groovyx.gaelyk.routes.Route

def log = new GroovyLogger("authenticatePlugin")
def userService = UserServiceFactory.getUserService()

before {
	request.user = request.getUserPrincipal()
	uri = request.getAttribute("javax.servlet.forward.request_uri")
	if (uri == "/") {
		request.login_url = userService.createLoginURL("/")
	} else {
		if (request.user) {
			request.logout_url = userService.createLogoutURL("/")
			// TODO checar se usuário existe
			// se sim, segue o fluxo normal
			// se não, manda pra tela de "cadastro"
		} else {
			response.sendRedirect userService.createLoginURL(uri)
		}
	}
}