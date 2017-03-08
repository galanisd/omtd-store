@echo off

SET /p CURRENTVERSION=<../VERSION
SET JARPATH=%CD%/../omtd-store-rest/target/omtd-store-rest-%CURRENTVERSION%.jar

if [%1]==[] goto defaultRun
REM Start REST server with the provided config file.
java -DstoreApplicationCfg=file:%CD%/%1 -jar %JARPATH%
goto :eof
:defaultRun
REM Start REST server with default config file.
java -jar %JARPATH% 
 