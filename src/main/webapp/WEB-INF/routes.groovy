get "/", forward: "/index.groovy"

all "/services/@action", forward: "/services.groovy?action=@action"
all "/services", forward: "/services.groovy?action=index"

all "/bank/@action", forward: "/bank.groovy?action=@action"
all "/bank", forward: "/bank.groovy?action=index"

all "/user/@action", forward: "/user.groovy?action=@action"

all "/domain/module/@module", forward: "/domain.groovy?module=@module&action=module"
all "/domain/@action", forward: "/domain.groovy?action=@action"

all "/admin/@module/@action", forward: "/admin.groovy?module=@module&action=@action"