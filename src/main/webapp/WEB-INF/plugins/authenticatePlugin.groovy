import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import br.octahedron.straight.Facade;

def userService = UserServiceFactory.getUserService()

before {
	request.user = request.getUserPrincipal()
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
			if (!Facade.getInstance().existsUser(request.user.name) && !uri.contains("/user")) {
				response.sendRedirect "/user/new"
			}
		} else {
			response.sendRedirect userService.createLoginURL(uri)
		}
	}
}