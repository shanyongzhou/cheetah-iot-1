setlocal
set EUREKA_PROCESS_TITLE=Registration-server-eureka
set CONFIG_CENTER_TITLE=Config-server
set SENTINEL_DASHBOARD_TITLE=Sentinel-dashboard
set CENTER_JAR_DIR=%~dp0%..\Release\center


if defined JAVA_HOME (
 set _EXECJAVA="%JAVA_HOME%\bin\java"
)

if not defined JAVA_HOME (
 echo "JAVA_HOME not set."
 set _EXECJAVA=java
)

start "%EUREKA_PROCESS_TITLE%" %_EXECJAVA%  -jar %CENTER_JAR_DIR%\eureka-server.jar
start "%CONFIG_CENTER_TITLE%" %_EXECJAVA%  -jar %CENTER_JAR_DIR%\config-center.jar
start "%SENTINEL_DASHBOARD_TITLE%" %_EXECJAVA% -Dserver.port=8081 -Dcsp.sentinel.dashboard.server=localhost:8081 -Dproject.name=sentinel-dashboard -Dsentinel.zookeeper.address=localhost:2181 -jar %CENTER_JAR_DIR%\sentinel-dashboard.jar
endlocal