setlocal
set CHEETAH_GATEWAY_TITLE=cheetah_gateway
set GATEWAY_JAR_DIR=%~dp0%..\Release\gateway


if defined JAVA_HOME (
 set _EXECJAVA="%JAVA_HOME%\bin\java"
)

if not defined JAVA_HOME (
 echo "JAVA_HOME not set."
 set _EXECJAVA=java
)

start "%MDC_GATEWAY_TITLE%" %_EXECJAVA%  -jar %GATEWAY_JAR_DIR%\gateway.jar
endlocal