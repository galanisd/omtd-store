FROM ubuntu:14.04

# Install java.
# -- -- --- - -- -- -- --- - -- 
RUN apt-get update && apt-get -y upgrade && apt-get -y install software-properties-common && add-apt-repository ppa:webupd8team/java -y && apt-get update
RUN (echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections) && apt-get install -y oracle-java8-installer oracle-java8-set-default
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle
ENV PATH $JAVA_HOME/bin:$PATH

# Prepare omtdstore installation. 
# -- -- --- - -- -- -- --- - -- 
# Create target dir.
RUN mkdir /opt/omtd-store
# Copy everything to target dir.
COPY . /opt/omtd-store/
# Set working dir and prepare installation. 
WORKDIR /opt/omtd-store/scripts/
RUN chmod 755 installAsInit.dService.sh 
#RUN ./installAsInit.dService.sh

# -- -- --- - -- -- -- --- - -- 
# Ready. Start it!
ENTRYPOINT pwd && ./installAsInit.dService.sh config.properties && service omtdstore start && /bin/bash

