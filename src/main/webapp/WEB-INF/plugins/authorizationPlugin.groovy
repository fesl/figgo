def properties = new ConfigSlurper().parse(new File('WEB-INF/plugins/authorization.properties').toURL())
def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.authorizationPlugin")

before {
	def controller = request.requestURI[1..-8]
	def controllerAction = request.method.toLowerCase() + "_" + request.parameterMap.action?.getAt(0)
	def authorizationAction = properties."$controller"."$controllerAction"
	// Enum."$authorizationAction"
}
