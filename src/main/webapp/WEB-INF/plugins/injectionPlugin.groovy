import br.octahedron.straight.view.VelocityTemplateRender
import br.octahedron.straight.view.JSONRender

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.injectionPlugin")
log.info "Registering facade injector plugin"

binding {
	render = { template, request, response -> VelocityTemplateRender.render(template, request, response) }
	renderJSON = { object, request, response -> JSONRender.render(object, request, response) }
	extractDomainName = { serverName -> return (serverName.indexOf('.') >= 0) ? serverName.substring(0, serverName.indexOf('.')) : serverName }
}
