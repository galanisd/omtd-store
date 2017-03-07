@echo off

if [%1]==[] goto defaultRun
REM Start rest server with the provided config file.
java -DstoreApplicationCfg=file:%CD%/%1 -jar %CD%/../omtd-store-rest/target/omtd-store-rest-0.0.1-SNAPSHOT.jar
goto :eof
:defaultRun
REM Start rest server with default config file.
java -jar %CD%/../omtd-store-rest/target/omtd-store-rest-0.0.1-SNAPSHOT.jar 
