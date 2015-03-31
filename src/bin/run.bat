

REM #
REM # Narvaro: @VERSION@
REM # Build Date: @DATE@
REM # Commit Head: @HEAD@
REM # JDK: @JDK@
REM # ANT: @ANT@
REM #
REM # Narvaro Launch Script
REM #
REM # This script launches the Narvaro application.
REM #

if "&JAVA_HOME%" == "" goto javaerror
if not exist "%JAVA_HOME%\bin\java.exe" goto javaerror
set NARVARO_HOME="%CD%\.."

start "Narvaro" "%JAVA_HOME%\bin\java" -DnarvaroHome=%NARVARO_HOME% -jar ..\lib\startup.jar
goto end

:javaerror
echo.
echo Error: JAVA_HOME environment variable not set.
echo.
goto end

:end
