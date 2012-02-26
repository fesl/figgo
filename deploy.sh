#! /bin/bash

alert() {
	cat << EOF
Olá, você está preste a realizar um novo deploy do figgo.

Este script irá auxiliá-lo nesta tarefa! 

[Enter para continuar]
EOF
	read 
	return 0
}
update_version() {
	actual=$(cat version)
	echo "Versão atual $actual"
	read -p "Digite a nova versão: " version
	echo ${version} > version
	return $?
}
build_project() {
	/bin/bash ctpx clean && \
	mvn clean test war:exploded && \
	return $?
}
adjust_config() {
	sed -i "/^    APPLICATION/d" target/deploy/WEB-INF/application.config && \
	sed -i "s/^    #APPLICATION/    APPLICATION/g" target/deploy/WEB-INF/application.config 
	return $?
}
deploy_app() {
	version=$(cat version)
	appcfg.sh -V $version update target/deploy
	return $?
}
change_default() {
	read -p "Deseja alterar a versão default do Figgo para a nova versão? [s/n] " resp
	if [ $resp = "s" ]; then
		version=$(cat version)
		appcfg.sh -V $version set_default_version target/deploy
		return $?
	else
		return 0
	fi
}

alert && \
update_version && \
build_project && \
adjust_config && \
deploy_app && \
change_default
exit

