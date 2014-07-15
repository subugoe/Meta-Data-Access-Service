[![Build Status](https://travis-ci.org/subugoe/Meta-Data-Access-Service.svg?branch=master)](https://travis-ci.org/subugoe/Meta-Data-Access-Service)

# Meta Data Access Service (MeDAS)

## Vision Statement

Die Vision von MeDAS (Meta Data Access System) ist ein Datenmanagement auf der Basis von Objektbeschreibungen (z.B. aus dem Bibliotheksbereich METS/MODS und TEI), d.h. die Entwicklung eines generischen Metadaten Servers, welcher CRUD-Operationen für digitale Objekten auf Basis Ihrer Objektbeschreibungen realisiert: Bereitstellung einer einheitlichen Manipulationsschnittstelle für unterschiedliche Objekttypen, Suche über Metadaten, Abbildung von Beziehungen und Navigation über diese, oder das Mapping unterschiedlicher Beschreibungselemente (z.B. METS <-> TEI). Wesentlich ist, das die Schnittstelle einfacher und komfortabler zu benutzen ist als die Originalbeschreibungen, und die Komplexität der zugrundeliegenden Beschreibung zugleich verbirgt. Für spezifische Anforderungen bleibt der Zugriff über die Originalbeschreibung erhalten. 

## Personas

## Fragen


## User Stories

* Als Online-Nutzer kann ich eine **Liste gespeicherter Bereiche** mit Identifizierer und Kurzbeschreibung abfragen (Zeitschriften, eLearning Objekte, Digital Born Objekte, Museums und Archiv Objekte, etc.) um mir einen Überblick über die im Repositorium gespeicherten Bereichen zu machen.
* Als Online-Nutzer kann ich **zu einem Bereich eine Liste von Facetten** abfragen, d.h. einschränkende Kriterien wie z.B. Publikationsdatum, -ort, Sprache oder Kategorie, um darüber Anfragen einzuschränken.
* Als Online-Nutzer kann ich zu einem Bereich **Objekte** mit Identifizierer und Basisinformationen abfragen (z.B. alle Zeitschriften). Die Abfrage kann ggf. anhand von Facetten gefiltert werden.
* Als Online-Nutzer kann ich **zu einem Objekt eine Liste von Facetten** abfragen, d.h. einschränkende Kriterien bzgl. verbundener Objekte (z.B. Zeitschriften, Bände, Artikel, Host, etc.).
* Als Online-Nutzer kann ich zu einem Objekt eine **Liste in Beziehung stehender Objekte** abfragen. Die Abfrage kann anhand von Facetten gefiltert werden.
* Als Online-Nutzer kann ich zu einem Objekt **den Scan** oder **den Volltext** abfragen.


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

