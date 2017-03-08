set /p CURRENTVERSION=<../VERSION
java -jar %CD%/../omtd-store-rest-client/target/omtd-store-rest-client-%CURRENTVERSION%-exec.jar 
pause;