// routes for the blobstore service example
get "/upload",  forward: "/upload.gtpl"
get "/success", forward: "/success.gtpl"
get "/failure", forward: "/failure.gtpl"

get "/favicon.ico", redirect: "/images/favicon.png"

get "/", forward: "/index.groovy"
get "/dashboard", forward: "/dashboard.groovy"
get "/bank", forward: "/bank.groovy"
get "/services", forward: "/services.groovy"

get "/user/new", forward: "/user.groovy?action=new"
post "/user/create", forward: "/user.groovy?action=create"
