#!/bin/bash

current=`pwd`
cd /Users/jpanzer/Documents/data/digizeit

for f in PPN*_*.xml; do curl -F file=@$f http://10.0.3.83:6789/medas/documents/ingest/ ; done

cd $current
