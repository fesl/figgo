def properties = new ConfigSlurper().parse(new File('WEB-INF/plugins/authorization.properties').toURL())

before {
	def controller = request.requestURI[1..-8]
	def controllerAction = request.method.toLowerCase() + "_" + request.parameterMap.module?.getAt(0) + request.parameterMap.action?.getAt(0)
	def activity = properties."$controller"."$controllerAction"
	request.activity = activity
}
