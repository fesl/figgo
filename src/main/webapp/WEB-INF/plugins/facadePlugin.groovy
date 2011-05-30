import br.octahedron.straight.view.plugins.FacadePluginManager

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.facadePlugin")
log.info "Registering facade plugin"

binding {
	usersManager = FacadePluginManager.instance.usersManager
}