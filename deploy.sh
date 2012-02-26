#! /bin/bash

version=maintenance

alert() {
	cat << EOF
Olá, você está preste a realizar um novo deploy do figgo.

Este script irá auxiliá-lo nesta tarefa! 

[Enter para continuar]
EOF
	read 
	return 0
}
build_project() {
	/bin/bash ctpx clean && \
	mvn clean test war:exploded && \
	return $?
}
deploy_app() {
	appcfg.sh -V $version update target/deploy
	return $?
}
change_default() {
	read -p "Deseja alterar a versão default do Figgo para a nova versão? [s/n] " resp
	if [ $resp = "s" ]; then
		appcfg.sh -V $version set_default_version target/deploy
		return $?
	else
		return 0
	fi
}

alert && \
build_project && \
deploy_app && \
change_default
exit

