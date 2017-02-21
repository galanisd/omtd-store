# OMTD-STORE #

omtd-store is the Store Service of OpenMinTeD. The project includes the following (Maven) projects.	

  * omtd-store-api which defines the API of the service. Two implementations are provided; one that is based on local hard drive and one that uses GRNET's [PITHOS](https://okeanos.grnet.gr/services/pithos/) cloud service.
  
  * omtd-store-rest which is a REST API for the Store Service.

  * omtd-store-rest-client is a REST client for omtd-store-rest.

  * omtd-store-common that includes some classes that are used in omtd-store-rest and omtd-store-rest-client.   

## Installation ##

The implementation of the service for PITHOS cloud storage is based on the respective JAVA REST-client; the latter  is available at GitHub (https://github.com/grnet/e-science/tree/master/pithosfs) as a part of "pithosFS Connector for Hadoop" project. (TO-DO: Add info on how to install the respective JAR).
   
Clone omtd-store by typing
  
```
git clone <repoURL>
```


CD to omtd-store directory that has been created. For building the projects type

```
mvn clean install -DstoreApplicationCfg=<applicationConfigFilePath>
```


storeApplicationCfg parameter specifies for which configuration/implementation the JUnit tests will run. For example the commands below build the project and run the tests for Local hard drive and PITHOS cloud storage respectively. 
  
```
mvn clean install -DstoreApplicationCfg=file:$(pwd)/scripts/configLocal.properties
mvn clean install -DstoreApplicationCfg=file:$(pwd)/scripts/configPITHOS.properties
```  

In order to skip JUnit tests just type 

```
mvn clean install -DskipTests=true.
```


## Run ##

Examples on how to start the REST Server & Client (in Windows and Linux) are provided in omtd-store/scripts folder. For example CD to scripts folder and type the following command to start the REST server uses the local
hard drive.  

```
LinuxStartOMTDStoreService.sh configLocal.properties
```

Then type  

```
LinuxStartOMTDStoreClient.sh
```

to start the command line client of the service.
 
 
