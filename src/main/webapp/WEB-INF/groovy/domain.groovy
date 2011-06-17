import br.octahedron.straight.modules.Module
import br.octahedron.straight.modules.ManagerBuilder

actions = ['config', 'update', 'upload', 'module']
configurationManager = ManagerBuilder.getConfigurationManager()

if (actions.contains(params.action)) {
	actionCall = request.method.toLowerCase() + "_" + params.action
} else {
	actionCall = "notfound"
}

def get_config() {
	request.domain = configurationManager.getDomainConfiguration()
	request.modules = configurationManager.getModulesInfoService()
	render 'domain/edit.vm', request, response
}

def get_upload() {
	request.upload_url = blobstore.createUploadUrl('/blob/domain/upload')
	render 'domain/upload.vm', request, response
}

def post_update() {
	configurationManager.updateDomainConfiguration(params.name, params.url, params.maillist, params.description)
	redirect '/'
}

// DOMAIN SPECIFIC CONFIGURATION
def get_module() {
	request.name = params.module
	request.module = configurationManager.getModuleConfiguration(Module.valueOf(params.module.toUpperCase()))
	render 'module/config.vm', request, response
}

def post_module() {
	def errors = []
	params.each() { key, value -> 
		try {
			if (key.startsWith("__")) {
				configurationManager.setModuleProperty(Module.valueOf(params.module.toUpperCase()), key.substring(2), value)
			}
		} catch (IllegalArgumentException e) {
			errors.add(e.getMessage())
		}
	}
	if (errors.isEmpty()) {
		redirect '/'
	} else {
		request.errors = errors
		request.name = params.module
		request.module = configurationManager.getModuleConfiguration(Module.valueOf(params.module.toUpperCase()))
		render 'module/config.vm', request, response
	}
}

def notfound() {
	render 'notfound.vm', request, response
}

"$actionCall"()
