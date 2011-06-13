def properties = new ConfigSlurper().parse(new File('WEB-INF/plugins/authorization.properties').toURL())
def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.authorizationPlugin")

enum Teste {
	OLA
}

before {
	def controller = request.requestURI[1..-8]
	def controller_action = request.method.toLowerCase() + "_" + request.parameterMap.action[0]
	def authorization_action = properties."$controller"."$controller_action"
	log.info Teste."$authorization_action".toString()
}
