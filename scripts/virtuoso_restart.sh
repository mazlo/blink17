#!/usr/bin/bash

sudo /usr/local/virtuoso-opensource/bin/isql 1111 dba dba exec="shutdown();"

sleep 4

sync && echo 3 > /proc/sys/vm/drop_caches

sudo /usr/local/virtuoso-opensource/bin/virtuoso-t -c /usr/local/virtuoso-opensource/var/lib/virtuoso/db/virtuoso.ini 

sleep 10
