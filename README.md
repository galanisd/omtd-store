# OMTD-STORE #

omtd-store is the Store Service of OpenMinTeD; it includes the following (Maven) projects.	

  * omtd-store-api: It contains the API definition of the service (`eu.openminted.store.core.StoreService`) and two implementations of it; one that is based on local hard drive and one that uses GRNET's [PITHOS](https://okeanos.grnet.gr/services/pithos/) cloud service.
  
  * omtd-store-rest: A REST API for the Store Service.

  * omtd-store-rest-client: A REST client for omtd-store-rest.

  * omtd-store-common that includes some classes that are used in omtd-store-rest and omtd-store-rest-client.   

## Installation ##

**Step 1**: The implementation of the service for PITHOS cloud storage is based on the respective JAVA REST-client; the latter  is available at GitHub (https://github.com/grnet/e-science/tree/master/pithosfs) as a part of "pithosFS Connector for Hadoop" project. Install the respective artifact
as follows 

TO-DO: Download from...

TO-DO: Type

```
...
``` 
   
**Step 2**: Clone omtd-store by typing
  
```
git clone <repoURL>
```


**Step 3**: CD to omtd-store directory that has been created. For building the projects type

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

Scripts for starting the REST Server & Client in Windows and Linux are provided in omtd-store/scripts folder. 
For example in a Linux machine CD to scripts folder and type the following command to start the REST server that uses the local hard drive.  

```
LinuxStartOMTDStoreService.sh configLocal.properties
```

Then open a new terminal and type  

```
LinuxStartOMTDStoreClient.sh
```

to start the command line client of the service.

  
## Install it as a service ##

CD omtd-store/scripts folder. Run 

```
installAsInit.dService.sh
```
 
Then start service by running  

```
service start omtdstore
```

For stopping, restarting and getting the status of the service similar commands are also available.

```
service {stop|restart|status} omtdstore
```



 
 
