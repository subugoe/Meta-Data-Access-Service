#!/bin/bash

mongo MetsTestDB3 --eval "db.dropDatabase()"

current=`pwd`
cd /Users/jpanzer/Documents/data/digizeit

#curl -F file=@PPN513009361.xml http://127.0.0.1:8080/documents/ingest/
for f in PPN513009361*.xml; do curl -F file=@$f http://127.0.0.1:8080/documents/ingest/ ; done

#curl -F file=@PPN522561411.xml http://127.0.0.1:8080/documents/ingest/
for f in PPN522561411*.xml; do curl -F file=@$f http://127.0.0.1:8080/documents/ingest/ ; done

#curl -F file=@PPN511864582.xml http://127.0.0.1:8080/documents/ingest/
for f in PPN511864582*.xml; do curl -F file=@$f http://127.0.0.1:8080/documents/ingest/ ; done

cd $current


