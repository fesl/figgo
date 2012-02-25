#! /bin/bash

cat << EOF
Olá, este script irá auxiliá-lo a realizar um deploy do figgo.

O primeiro passo antes do deploy é ajustar o numero de versão no arquivo de 
configuração do Appengine. Se você tem alguma dúvida de como atribuir o número
da versão, olhe o arquivo 'VERSION_NUMBER'

Depois será necessário alterar o arquivo de configuração da applicação para o ambiente
de produção.

Este script irá lhe auxiliar com estas tarefas!

[Enter]
EOF
read 

./ctpx clean && \
vim src/main/webapp/WEB-INF/appengine-web.xml && \
vim src/main/webapp/WEB-INF/application.config && \
echo "Building and deploying application" && \
mvn test war:exploded && appcfg.sh update target/deploy && \
git checkout src/main/webapp/WEB-INF/appengine-web.xml src/main/webapp/WEB-INF/application.config
