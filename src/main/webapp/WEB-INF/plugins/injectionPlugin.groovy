import br.octahedron.straight.modules.users.UsersExternalFacade
import br.octahedron.straight.modules.configuration.ConfigurationExternalFacade
import br.octahedron.straight.view.VelocityTemplateRender

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.injectionPlugin")
log.info "Registering facade plugin"

binding {
	usersFacade = new UsersExternalFacade() 
	configurationFacade = new ConfigurationExternalFacade()
	render = { template, request, response -> VelocityTemplateRender.render(template, request, response) }
}
