#! /bin/bash

version=maintenance
appid=dev-figgo

alert() {
	cat << EOF
Olá, você está preste a realizar um novo deploy do figgo.

Este script irá auxiliá-lo nesta tarefa! 

[Enter para continuar]
EOF
	read 

if [ ${appid} == "figgo-octa" ]
then
	cat << EOF
Você está prestes a fazer um deploy no ambiente de produção.

Para cancelar pressione CTRL+C, para continuar Enter.
EOF
	read
fi
	return 0
}
build_project() {
	/bin/bash ctpx clean && \
	mvn clean test war:exploded && \
	return $?
}
deploy_app() {
	appcfg.sh -A ${appid} -V ${version} update target/deploy
	return $?
}
change_default() {
	read -p "Deseja alterar a versão default do Figgo para a nova versão? [s/n] " resp
	if [ $resp = "s" ]; then
		appcfg.sh -A ${appid} -V ${version} set_default_version target/deploy
		return $?
	else
		return 0
	fi
}

if [ $# -eq 2 ] && [ $1 == "--app" ]
then
	appid=$2
fi

alert && \
build_project && \
deploy_app && \
change_default
exit

