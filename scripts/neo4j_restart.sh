#!/usr/bin/bash

sudo -u matthaeus -H /home/matthaeus/Projects/neo4j-community-2.2.1/bin/neo4j stop

sleep 2

sync && echo 3 > /proc/sys/vm/drop_caches
sudo -u matthaeus -H JAVA_OPTS="-Xmx4G" /home/matthaeus/Projects/neo4j-community-2.2.1/bin/neo4j start

sleep 4
