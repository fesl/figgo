import br.octahedron.straight.view.VelocityTemplateRender
import br.octahedron.straight.modules.ManagerBuilder

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.injectionPlugin")
log.info "Registering facade injector plugin"

binding {
	render = { template, request, response -> VelocityTemplateRender.render(template, request, response) }
	authorize = { request, response -> 
		if (!ManagerBuilder.getAuthorizationManager().isAuthorized(request.serverName, request.user.email, request.activity)) {
			response.sendRedirect '/500'
		}
	}
}
