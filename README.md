[![Build Status](https://travis-ci.org/subugoe/Meta-Data-Access-Service.svg?branch=master)](https://travis-ci.org/subugoe/Meta-Data-Access-Service)

# Meta Data Access Service (MeDAS)

## Vision Statement

Die Vision von MeDAS (Meta Data Access System) ist ein Datenmanagement für Objektbeschreibungen (z.B. aus dem Bibliotheksbereich METS/MODS und TEI), d.h. die Entwicklung eines generischen Metadaten Servers, welcher CRUD-Operationen für digitale Objekten auf Basis Ihrer Objektbeschreibungen realisiert, z.B.

* die Bereitstellung einer einheitlichen Manipulationsschnittstelle für unterschiedliche Objekttypen,
* Suche über Metadaten,
* die Abbildung von Beziehungen und Navigation über diese, 
* das Mapping unterschiedlicher Beschreibungselemente (z.B. METS <-> TEI), oder
* eine Standardkonforme (Bulk) Import/Export Schnittstelle. 

Die Schnittstelle soll einfacher und komfortabler zu benutzen sein als die Originalbeschreibungen, d.h. die Komplexität der Beschreibung verbergen und *wesentliche* Nutzeranforderungen erfüllen, was als wesentlich gilt, muss von den potentiellen Nutzern festgelegt werden - derzeit DigiZeit oder ROPEN. Für spezifische Anforderungen bleibt der Zugriff über die Originalbeschreibung erhalten. 

## Unterschied zwischen aktuellem und anvisiertem Zustand
### Aktuell
* Anwendungen rufen Beschreibungen über eine HTTP Schnittstelle ab und parsen diese selbst. 

### Ziel
* Über MeDAS können bestehende Anwendungen wie gehabt weiterarbeiten, d.h. sie können direkt auf Beschreibungen arbeiten. Es ist aber auch möglich, die Prozessierung MeDAS zu überlassen und über dessen Schnittstelle auf Elemente zuzugreifen.
* MeDAS ist nicht eingeschränkt auf eine Domäne, sondern bietet eine generische Standardschnittstelle für unterschiedliche Beschreibungsformate (Zeitschriften, Bücher, Multimedia, etc.).

## Konzeption, Architektur
Konzeptionell teilt sich MeDAS in eine Serverkomponente und eine Client-Bibliothek. Die Serverkomponente behandelt das Datenmanagement und die Client-Bibliothek stellt Anwendungen Funktionen zur Interaktion mit dem Server bereit, wobei von Aspekte wie Netzwerkkommunikation, Verteilung, Persistenzmodell, etc. abstrahiert wird. Der Server bildet Domänenobjekte auf das Datenmodell des Backendsystems ab und transformiert anfragen. 

* Server
	* todo
* Client (-Bibliothek) 
	* todo

## Personas, nutzende Dienste
* ROPEN
* Repository-Systeme
* ggf. Portale, wie DigiZeit



## Fragen
* In welchem Maß ist ein Benutzer-/Rechtemanagement erforderlich?


## User Stories

* Die Nutzer-Anwendung kann eine **Liste gespeicherter Bereiche** mit Identifizierer und Kurzbeschreibung abfragen (Zeitschriften, Bücher, Vorlesungsmitschriften, eLearning Objekte, Digital Born Objekte, Museums und Archiv Objekte, etc.) um dem Benutzer einen Überblick über die im Repositorium gespeicherten Bereichen zu geben.
* Die Nutzer-Anwendung kann **zu einem Bereich eine Liste von Facetten** abfragen, d.h. einschränkende Kriterien wie z.B. Publikationsdatum, -ort, Sprache oder Kategorie, um darüber Anfragen einzuschränken.
* Die Nutzer-Anwendung kann zu einem Bereich **Liste gespeicherter Objekte** mit Identifizierer und Basisinformationen abfragen.
* Die Nutzer-Anwendung kann **zu einem Objekt eine Liste von Facetten** abfragen, d.h. einschränkende Kriterien bzgl. verbundener Objekte (z.B. Zeitschriften, Bände, Artikel, Host, Scan, Volltext, etc.).
* Die Nutzer-Anwendung kann zu einem Objekt eine **Liste in Beziehung stehender Objekte** abfragen. Die Abfrage kann anhand von Facetten gefiltert werden.
* Ein Benutzer kann über die Nutzer-Anwendung Vorlesungsmitschriften lesen. Er sucht zunächst die Vorlesung. Aus der Vorlesung wählt er die Mitschrift aus. Zur Mitschrift können Metadaten, Scans und Volltext abgerufen werden. Der Abruf kann seitenweise oder für das gesamte Dokument erfolgen und die Rückgabe von Scans kann in einem Bild-Format oder als PDF erfolgen. 
* Die Nutzer-Anwendung kann zu einem Objekt die *Elternobjekte* abrufen (z.B. Zeitschrift eines Band) .
* Die Nutzer-Anwendung kann zu einem Objekt die *Kindobjekte* abrufen (z.B. Artikel eines Band).
* Die Nutzer-Anwendung kann ein digitales Objekt per Id abfragen.
* Die Nutzer-Anwendung kann digitale Objekte per Type (z.B. Zeitschrift, Band, Artikel, Paragraph, Scan, Fulltext) und weiteren einschränkenden Parametern abfragen (z.B. isHostOf, isPrecedingOf, inResolution).
* Die Nutzer-Anwendung kann die mit einem digitalen Objekt in Beziehung stehenden Objekte abrufen. Die zurückgegebenen Objekte könnten zudem per Type (z.B. Zeitschrift, Band, Artikel, Paragraph, Scan, Fulltext) oder durch weitere einschränkende Parametern eingeschränkt werden (z.B. isHostOf, isPrecedingOf, inResolution).

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

