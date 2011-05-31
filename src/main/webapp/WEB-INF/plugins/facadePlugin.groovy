import br.octahedron.straight.modules.users.UsersExternalFacade
import br.octahedron.straight.modules.configuration.ConfigurationExternalFacade

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.facadePlugin")
log.info "Registering facade plugin"

binding {
	usersFacade = new UsersExternalFacade() 
	configurationFacade = new ConfigurationExternalFacade()
}