// routes for the blobstore service example
get "/upload",  forward: "/upload.gtpl"
get "/success", forward: "/success.gtpl"
get "/failure", forward: "/failure.gtpl"

get "/", forward: "/index.groovy"
get "/dashboard", forward: "/dashboard.groovy"
get "/bank", forward: "/bank.groovy"
get "/services", forward: "/services.groovy"

all "/user/@action", forward: "/user.groovy?action=@action"

all "/domain/@action", forward: "/domain.groovy?action=@action"

all "/admin/@module/@action", forward: "/admin.groovy?module=@module&action=@action"
