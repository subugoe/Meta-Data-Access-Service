[![Build Status](https://travis-ci.org/subugoe/Meta-Data-Access-Service.svg?branch=master)](https://travis-ci.org/subugoe/Meta-Data-Access-Service)

# Meta Data Access Service (MeDAS)

## Über das Projekt:

Das Projekt MeDAS ist mit der Entwicklung eines Metadaten Servers beschäftigt, der METS/MODS Daten verwaltet und eine 'einfache 'Schnittstelle auf die Daten bietet. In einem ersten Schritt werden die Daten in MongoDB gespeichert, wobei die "dmdSec" auf eine eigene Mongo-Kollektion "mods" abgebildet wird. Zudem werden ausgewählte Felder in eine "lookup" Kollektion abgebildet, was den KOmfort und die Performance bei suchen wesentlich unterstützt. Als alternativen sollen auch Elasticsearch und Riak getestet werden.


## Entwicklungsumgebung
### Konfiguration
Über die Konfigurationsdatei: src/main/resources/mongo.properties.

### Maven
Bauen des Projekts:
>mvn clean package

Deployment Optionen:
>1. Datei "mets_mongo_mapper.war" aus dem Verzeichnis "project/target/", in das Deploymentverzeichnis des Servletcontainers kopieren, im Falle von Tomcat bspw. "$TOMCAT_HOME/webapps/".
>2. Über Deploymentmechanismen des Servletcontainers.
>3. Über Mechanismen der verwendeten IDE.
>4. Über Maven.

### MongoDB
Damit Daten nach MongoDB geschrieben und von dort wieder bezogen werden können, muss eine MongoDB Installation verfügbar sein. Die Konfiguration des Server-Url und -Port, Datenbank und Kollektion erfolgt über die genannte Property Datei.

