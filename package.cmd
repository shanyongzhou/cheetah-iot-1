title "packaging cloud service....."
set currentDir=%cd%
set releaseDir=%cd%\Release
set CENTER_DIR=%cd%\center
set BOOTSTRAP_DIR=%cd%\bootstrap
set APP_SERVICE_DIR=%cd%\app

call mvn clean
call mvn package -Dmaven.test.skip=true

cd/d %CENTER_DIR%\sentinel-dashboard
call mvn clean
call mvn package -Dmaven.test.skip=true

if exist %releaseDir% (
	rd/s/q %releaseDir%
)

xcopy %CENTER_DIR%\config\target\*.jar %releaseDir%\center\
xcopy %CENTER_DIR%\eureka\target\*.jar %releaseDir%\center\
xcopy %CENTER_DIR%\sentinel-dashboard\target\*.jar %releaseDir%\center\

xcopy %BOOTSTRAP_DIR%\mdc-bootstrap\target\*.jar %releaseDir%\bootstrap\
xcopy %currentDir%\gateway\target\*.jar %releaseDir%\gateway\

xcopy %APP_SERVICE_DIR%\demo\target\*.jar %releaseDir%\app\

exit

