// routes for the blobstore service example
get "/upload",  forward: "/upload.gtpl"
get "/success", forward: "/success.gtpl"
get "/failure", forward: "/failure.gtpl"

get "/", forward: "/index.groovy"

get "/dashboard", forward: "/dashboard.groovy"

all "/services", forward: "/services.groovy?action=index"
all "/services/@action", forward: "/services.groovy?action=@action"

all "/bank", forward: "/bank.groovy?action=index"
all "/bank/@action", forward: "/bank.groovy?action=@action"

all "/user/@action", forward: "/user.groovy?action=@action"

all "/domain/@action", forward: "/domain.groovy?action=@action"

all "/admin/@module/@action", forward: "/admin.groovy?module=@module&action=@action"
