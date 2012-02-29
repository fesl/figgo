#!/bin/bash 

debug=""
if [ $1 == "--debug" ]; then
	debug="-Ddbg=true"
	shift
fi

gae_sdk=/usr/lib/gae-sdk/java/
jars="$gae_sdk/lib/appengine-remote-api.jar:$gae_sdk/lib/appengine-tools-api.jar:$gae_sdk/lib/impl/appengine-api.jar:target/migration_tools-1.0.jar"

java ${debug} -cp $jars br.figgo.UploadBankData $*
