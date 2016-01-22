# Vaadin OSGi Stagemonitor

Simple Test of implementing Stagemonitor into a Vaadin + OSGi application.

## Build and install

### Karaf
The project contains a feature for Apache Karaf 3.0.5 - download [here](http://www.apache.org/dyn/closer.lua/karaf/3.0.5/apache-karaf-3.0.5.tar.gz)

### Build
Build the maven project

### Run
Start karaf and install the feature:


	feature:repo-add mvn:de.studiointeractive.samples/stagemonitor-feature-repository/1.0.0-SNAPSHOT/xml/features
	feature:install stagemonitor

Launch your browser and goto [http://localhost:8181/stagemonitor](http://localhost:8181/stagemonitor)

#<mark>The implementation doesn't work so far - it's work in progress!</mark> 