#!/bin/bash

#****1.check JDK******
if [ "$JAVA_HOME" == "" ]; then
        echo "pls set JAVA_HOME first"
        exit
fi
date_string=`date +"%Y-%m-%d %H:%M:%S"`
echo "-----start脚本开始---"$date_string"---------"
basepath=$(cd `dirname $0`/../; pwd)

echo "工作目录："$basepath

APP_HOME=$basepath
filepath=$APP_HOME/logs
if [ ! -x "$filepath" ]; then
        mkdir  $filepath
fi
filepath=$APP_HOME/tmp
if [ ! -x "$filepath" ]; then
        mkdir $filepath
fi
MIN_MEM=128m
MAX_MEM=256m
COREDUMP_PATH=${APP_HOME}/logs

# 这里仍然需要改动配置为项目的启动类路径
MAIN_FUNC=cn.demo.thf.App

date_string=`date +"%Y-%m-%d %H:%M:%S"`

CLASSPATH="-classpath .:${APP_HOME}/etc/:${APP_HOME}/lib/* "
JAVA_OPTS="-server -Xms${MIN_MEM} -Xmx${MAX_MEM} "
CORE_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${COREDUMP_PATH}"
ALL_JAVA_OPTIONS=${JAVA_OPTS}${CLASSPATH}${CORE_OPTS}


echo "-------"$date_string"----------">../logs/log.txt
#nohup java -server -Detcpath=$ETCPATH  $MAIN_FUNC>>$APP_HOME/logs/log.txt 2>>$APP_HOME/logs/log.txt&
nohup $JAVA_HOME/bin/java ${ALL_JAVA_OPTIONS}  -Djava.io.tmpdir=${APP_HOME}/tmp/ -Dspring.config.location=${APP_HOME}/etc/ $MAIN_FUNC>>$APP_HOME/logs/log.txt 2>>$APP_HOME/logs/log.txt&



echo ---------------start脚本结束！---------------
echo
#tailf $APP_HOME/logs/log.txt
