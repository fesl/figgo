#/bin/bash

#
# This file is part of Figgo
# 
# TODO description
#
#

if [ $# -ne 2 ]; then
	echo "Uso: <domain> <dir>"
	exit 1;
fi

domain=$1
dir=$2
cd "$dir"

for file in *.csv; do
	sed -i '/^Saldo*\|,,,,"MR$*\|^Data*\|,,,"MR$*/d' $file
	sed -i 's/MR\$ //g' $file
	sed -i 's/MR\$//g' $file
	#sed -i 's/\"//g' $file
	#awk -F, '{ print $1 "::" $2 "::" $3"."$4 > FILENAME }' $file 
	if [[ $file =~ .-in\.csv ]]; then
		orig=${domain}"@figgo.com.br"
		dest=$(echo $file | cut -d'-' -f1)"@gmail.com"
		mv $file ${orig}-${dest}
	elif [[ $file =~ .*-out\.csv ]]; then
		orig=$(echo $file | cut -d'-' -f1)"@gmail.com"
		dest=${domain}"@figgo.com.br"
		mv $file ${orig}-${dest}
	fi
done

cd - 
java -cp target/migration_tools-1.0.jar br.figgo.TransformFile $dir

