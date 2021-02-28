#!/usr/bin/env bash

# 用这种方法打的jar包，是不包含依赖的jar包和配置文件的。所以，在执行之前需要把依赖的jar包和配置文件加入到 $CLASSPATH 中，Shell脚本如下：
BIN=`dirname $0`
BIN=`cd $BIN; pwd`
APP_HOME=`dirname $BIN`

JAVA=${JAVA_HOME}/bin/java
CLASSPATH=${APP_HOME}/conf
#JAR=${APP_HOME}/tools.jar

for f in ${APP_HOME}/lib/*.jar; do
    CLASSPATH=${CLASSPATH}:$f
done
for f in ${APP_HOME}/*.jar; do
    CLASSPATH=${CLASSPATH}:$f
done
MAINCLASS="edu.wzm.joda.JodaDemo"
exec "$JAVA" -classpath $CLASSPATH $MAINCLASS
