# Meta Data Access Server (MeDAS)

## Über das Projekt:

Das Projekt MeDAS ist mit der Entwicklung eines Metadaten Servers beschäftigt. Derzeit auf ein Mets nach Mongo Mappings ausgerichtet. Das Mets Mapping erfolgt 1:1, d.h. ein Mets-Dokument wird auf ein Mongo-Dokument abgebildet. Die Felder *_id*, *ids*, *docinfo* und *namespaces* im MongoDB Dokument werden zur einfacheren indentifizierung, Beschreibung und Übertragung der Namespaces hinzugefügt, sie haben kein direktes Pendant im Mets-Dokument. Die  METS -> Mongo Abbildung wird wie folgt vorgenommen:

- **_id**: *Feld hat **kein äquvalent** in Mets*, enthält die docid in Mongo. Immer enthalten.
- **ids**: *Feld hat **kein äquvalent** in Mets*, enthält die PIDs des Mets-Dokuments. Immer enthalten.
- **root**         ->   **mets**: Enthält Attribute des Root-Mets-Element. Optional.
- **docinfo**: *Feld hat **kein äquvalent** in Mets*, enthält eine Zusammenfassung von wichtigen Informationen (pid, title, titleShort, mets-url, tei-url, teiEnriched-url, ). Immer enthalten.
- **aus root**     ->   **namespaces**: Enthält die Namespacedeklarationen aus dem Mets-Dokument. Optionale.
- **metsHdr**      ->   **metsHdr**: Informationen über das Mets-Dokument.
- **dmdSec**       ->   **dmdSec**: Enthält die beschreibenden Metadaten aus dem Mets-Dokument. Optional.
- **amdSec**       ->   **amdSec**: Informationen, die für die Verwaltung und Verwendung wichtig sind. Enthält administrative (bzgl. Bereich Technik, Rechte, Entstehung und Änderungshistorie, ) Metadaten aus dem Mets-Dokument. Optional.
- **fileSec**      ->   **fileSec**: Listet Dateien mit Zugriffspfaden.
- **structMap**    ->   **structMap**: Bildet die physische und logische Struktur ab.
- **structLink**   ->   **structLink**: Verbindet die Elemente der einzelnen Sektionen.
- **behaviorSec**  ->   **behaviorSec**: Verbindet das beschriebende Objekt der Teile davon mit Diensten.

Das Tei Mapping erfolgt nicht 1:1, damit Suchergebnisse konkreter sind, d.h. Seiten oder Paragraphen adressieren. Daher werden Tei Dokumente auf drei Kollektionen (Document, Page und Content) abgebildet. Die Kollektion Content hält zunächst nur Absätze (Paragraphs), kann aber auch z.B. Figure-Eigenschaften aufnehmen. Figures werden zunächst nicht aufgenommen, da de die DB aufblähen, ohne direkt von Nutzen zu sein (z.B. in Suchen). Das gilt auch für die "function"-Attribute (Positionsangaben) von Worten, die Info kann jedoch bei Bedarf aus dem Original ei Dokument ermittelt werden. Worte werden aus dem Tei ausgelesen und zu Absätzen zusammengesetzt. Absätze werden volltextindexiert. Aktuell wird folgende Struktur aufgebaut:

Document:
{ _id : ObjectId, docInfo : { relatedTeiDoc : ObjectId, relatedMetsDoc : ObjectId, pageCount : n } }

Mit:
relatedTeiDoc -> ObjectId  des zugehörigen Tei Dokuments
relatedMetsDoc -> ObjectId des zugehörigen Mets Dokuments
pageCount -> Seitenzahl des Dokuments

Page:
{ _id : ObjectId, pageInfo : { relatedDocument : ObjectId, order : n} }

Mit:
relatedDocument -> ObjectId des zugehörigen Dokuments
order -> Seitennummer bzw. Position im Dokument


Content:
{ _id : ObjectId, ContentInfo : {relatedPage : ObjectId, order : n, contentType : ct, type : t } }

Mit:
relatedPage -> ObjectId der zugehörigen Seite
order -> Position auf der Seite
contentType -> {figure | paragraph}
type -> {z.Zt. nur page}



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
Damit Daten nach MongoDB geschrieben und von dort wieder bezogen werden können, muss eine MongoDB Installation verfügbar sein. Die Konfiguration des Server-Url und -Port, Datenbank und Kollection erfolgt über die genannte Property Datei.

