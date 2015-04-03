@echo off

REM # Development Script
REM #
REM # This script is used for development of Narvaro.
REM #
REM # Script will clean the target directory, then build Narvaro,
REM #  after-which this script will launch Narvaro.
REM #
REM # This script is meant as a convenience for developing Narvaro.
REM #

if "%JAVA_HOME%" == "" goto javaerror
if not exist "%JAVA_HOME%\bin\java.exe" goto javaerror

REM # clean Narvaro target dir and build Narvaro
call ant clean

REM # build Narvaro
call ant narvaro

REM # cd into Narvaro target bin\ directory
cd ..\target\narvaro\bin
 
 REM # launch Narvaro
call run.bat

REM # cd back into build\ directory
cd ..\..\..\build

goto end

:javaerror
echo.
echo Error: JAVA_HOME environment variable not set.
echo.
goto end

:end
