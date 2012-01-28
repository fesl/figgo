#! /bin/bash

cat << EOF
Olá, este script irá auxiliá-lo a realizar um deploy do figgo.
Antes de mais nada é interessante que você certifique-se este branch contém
as mudanças que você deseja fazer o deploy.
Talvez você precise fazer um 'git rebase master' ou pegar apenas os patches
que você deseja. Assegure-se disso antes de continuar.

[Enter]
EOF
read 

cat <<EOF
O primeiro passo antes do deploy é ajustar o numero de versão no arquivo de 
configuração do Appengine. Se você tem alguma dúvida de como atribuir o número
da versão, olhe o arquivo 'VERSION_NUMBER'

[Enter]
EOF
read 

vim src/main/webapp/WEB-INF/appengine-web.xml && \
echo "Building and deploying application" && \
mvn clean test war:exploded && appcfg.sh update target/deploy
