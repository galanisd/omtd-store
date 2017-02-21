# omtd-store-all #

### Overview ###

omtd-store is the Store Service of OpenMinTeD. It includes the following (Maven) projects.

* omtd-store-api which defines the API of the service. Two implementations are provided; one that is based on local hard drive and one that uses GRNET's [PITHOS](https://okeanos.grnet.gr/services/pithos/) cloud service.
  
* omtd-store-rest which is a REST API for the Store Service.

* omtd-store-rest-client is a REST client for omtd-store-rest.

* omtd-store-common that includes some classes that are used in omtd-store-rest and omtd-store-rest-client.   

### Installation ###

* The implementation of the service for PITHOS cloud storage is based on the respective JAVA REST-client; the latter  is available at GitHub (https://github.com/grnet/e-science/tree/master/pithosfs) as a part of "pithosFS Connector for Hadoop" project. (TO-DO: Add info on how to install the respective JAR).
   
* Clone omtd-store by typing git clone repoURL. 

* CD to omtd-store directory that has been created. For building the projects type mvn clean install -DstoreApplicationCfg=file:$(pwd)/scripts/configLocal.properties or mvn clean install -DstoreApplicationCfg=file:$(pwd)/scripts/configPITHOS.properties. storeApplicationCfg parameter specifies for which configuration/implementation (Local hard disk, PITHOS) the JUnit tests will run. In order to skip JUnit test just type mvn clean install -DskipTests=true.

### Run ###

Examples on how to start the REST Server & Client (in Windows and Linux) are provided in omtd-store/scripts folder.


 
