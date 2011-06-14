import br.octahedron.straight.view.VelocityTemplateRender
import java.util.regex.Pattern
import java.util.regex.Matcher

def log = new groovyx.gaelyk.logging.GroovyLogger("br.octahedron.straight.view.plugins.injectionPlugin")
log.info "Registering facade injector plugin"

// validation patterns
def namePattern = Pattern.compile('([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûçÇ] *){2,}')
def phonePattern = Pattern.compile('^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$')

binding {
	render = { template, request, response -> VelocityTemplateRender.render(template, request, response) }
	userValidation = { params ->
		phone = params.phoneNumber.trim()
		name = params.name.trim()
		isValid = true
		errors = []
		count = 0
		if (!namePattern.matcher(name).matches()) {
		    errors[count++] = "Nome inválido"
		    isValid = false
		}
		if (!phonePattern.matcher(phone).matches()) {
		    errors[count++] = "Telefone inválido"
		    isValid = false
		}
		return [isValid, errors]
	}
}
