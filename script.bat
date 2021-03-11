@echo off
::"<path to VS2019>\VC\Auxiliary\Build\vcvars64.bat"
echo Calling vcvars64.bat to load Visual Studio environment
call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat"

echo Cleaning target directory
::mvn client:build
call mvn clean

echo Building windows executable...
call mvn -Pdesktop client:compile