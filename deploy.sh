#! /bin/bash

echo "Please, before continue, edit the application version"
echo "Press ENTER to continue"
read a
vim src/main/webapp/WEB-INF/appengine-web.xml && \
echo "Building and deploying application" && \
mvn clean test war:exploded && appcfg.sh update target/deploy
