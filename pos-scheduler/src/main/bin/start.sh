#!/bin/bashecho
basepath=$(cd `dirname $0`; pwd)
echo $basepath
cd $basepath
cd ../
java -jar -Xms128m -Xmx512m pos-scheduler.jar
read -n 1