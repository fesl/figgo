// routes for the blobstore service example
get "/upload",  forward: "/upload.gtpl"
get "/success", forward: "/success.gtpl"
get "/failure", forward: "/failure.gtpl"

get "/favicon.ico", redirect: "/images/favicon.png"

get "/", forward: "/index.groovy"
get "/dashboard", forward: "/dashboard.groovy"