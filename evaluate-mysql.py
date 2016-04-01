#!/usr/bin/python

# service stop
# sync && echo 3 > /proc/sys/vm/drop_caches
# service start

# sed -i 's/thread.pool.size=.*/thread.pool.size=15/' application.properties
# java -jar disco-evaluation-0.0.1-SNAPSHOT.jar

import os
import time

for tps in ["1"]:
  os.system( "service mysqld stop" );
  print "dropping caches..";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  os.system( "service mysqld start" );
  time.sleep(5);
  print "setting up thread pool to "+ tps;
  os.system( "sudo -u matthaeus -H sed -i 's/thread.pool.size=.*/thread.pool.size="+ tps +"/' application.properties ");
  print "about to start evaluation..";
  time.sleep(5);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );
