def properties = new ConfigSlurper().parse(new File('WEB-INF/plugins/authorization.properties').toURL())
def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.authorizationPlugin")
def authorizationManager = ManagerBuilder.getAuthorizationManager()

before {
	def controller = request.requestURI[1..-8]
	def controllerAction = request.method.toLowerCase() + "_" request.parameterMap.module?.getAt(0) + request.parameterMap.action?.getAt(0)
	def activity = properties."$controller"."$controllerAction"
	if (controller != "user" && !authorizationManager.isAuthorized(request.serverName, request.user.email, activity) {
		response.sendRedirect '/500'
	}
}
