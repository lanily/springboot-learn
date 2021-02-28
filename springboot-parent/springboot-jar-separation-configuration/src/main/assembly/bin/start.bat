@ echo off
cd %~dp0
cd ../
set APP_HOME=%cd%

cd tools
set ETCPATH=%APP_HOME%\etc

set MAIN_FUNC=cn.demo.thf.App
set MIN_MEM=128m
set MAX_MEM=256m
set COREDUMP_PATH=%APP_HOME%\logs

set CLASSPATH=-classpath %APP_HOME%\etc\;%APP_HOME%\lib\*
set JAVA_OPTS=-server -Xms%MIN_MEM% -Xmx%MAX_MEM%
set CORE_OPTS=-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%COREDUMP_PATH%"
set ALL_JAVA_OPTIONS=%JAVA_OPTS%%CLASSPATH%%CORE_OPTS%

java -classpath %APP_HOME%\etc\;%APP_HOME%\lib\* -Dspring.config.location=%APP_HOME%\etc\ %MAIN_FUNC%

echo "******************************"%APP_HOME***********************************"


pause
