#! /bin/bash

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
update_version() {
	if [ ${appid} == "dev-figgo" ]
	then
		version=$(date +%Y%m%d-%H%M)
	else
		actual=$(cat version)
		echo "Versão atual $actual"
		read -p "Digite a nova versão (ou enter para mesma versao): " version
	fi
	if [ ${version} ]
	then
		echo ${version} > version
	fi
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

	if [ $? -eq 0 ] && [ ${appid} == "dev-figgo" ]
	then
		sed -i "s/figgo.com.br/oggif.com.br/g" target/deploy/WEB-INF/application.config 
	fi
	return $?
}
deploy_app() {
	version=$(cat version)
	appcfg.sh -A ${appid} -V ${version} update target/deploy
	return $?
}
change_default() {
	read -p "Deseja alterar a versão default do Figgo para a nova versão? [s/n] " resp
	if [ $resp = "s" ]; then
		version=$(cat version)
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
update_version && \
adjust_config && \
deploy_app && \
change_default

res=$?
if [ ${appid} == "dev-figgo" ]
then
	git checkout -- version
fi
exit ${res}

