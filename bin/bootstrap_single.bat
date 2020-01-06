setlocal
set MDC_BOOTSTRAP_TITLE=MDC_bootstrap
set BOOTSTRAP_JAR_DIR=%~dp0%..\Release\bootstrap


if defined JAVA_HOME (
 set _EXECJAVA="%JAVA_HOME%\bin\java"
)

if not defined JAVA_HOME (
 echo "JAVA_HOME not set."
 set _EXECJAVA=java
)

start "%MDC_BOOTSTRAP_TITLE%" %_EXECJAVA%  -jar %BOOTSTRAP_JAR_DIR%\mdc-bootstrap.jar
endlocal