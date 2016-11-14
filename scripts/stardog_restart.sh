#!/usr/bin/bash

sudo -u matthaeus -H JAVA_HOME=\"/usr/lib/java/jdk1.8.0_65/\" /home/matthaeus/Projects/stardog-4.0.5/bin/stardog-admin server stop

sleep 2

sync && echo 3 > /proc/sys/vm/drop_caches

# ensure that stardog can start again
rm -rf /home/matthaeus/Projects/disco-dataset/stardog/system.lock

sudo -u matthaeus -H JAVA_HOME="/usr/lib/java/jdk1.8.0_65/" STARDOG_HOME="/home/matthaeus/Projects/disco-dataset/stardog" /home/matthaeus/Projects/stardog-4.0.5/bin/stardog-admin server start --disable-security &

sleep 4
