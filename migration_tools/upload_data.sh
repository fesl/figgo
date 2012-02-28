#!/bin/bash 

gae_sdk=/usr/lib/gae-sdk/java/
jars="$gae_sdk/lib/appengine-remote-api.jar:$gae_sdk/lib/appengine-tools-api.jar:$gae_sdk/lib/impl/appengine-api.jar:target/migration_tools-1.0.jar"

java -cp $jars br.figgo.UploadBankData $*
