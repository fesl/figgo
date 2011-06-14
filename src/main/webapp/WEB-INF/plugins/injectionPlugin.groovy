import br.octahedron.straight.view.VelocityTemplateRender

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.injectionPlugin")
log.info "Registering facade injector plugin"

binding {
	render = { template, request, response -> VelocityTemplateRender.render(template, request, response) }
}
