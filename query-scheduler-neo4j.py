#!/usr/bin/python

# service stop
# sync && echo 3 > /proc/sys/vm/drop_caches
# service start

# sed -i 's/thread.pool.size=.*/thread.pool.size=15/' application.properties
# java -jar disco-evaluation-0.0.1-SNAPSHOT.jar

import os
import time

for tps in ["1"]:#,"5","10","20"]:
  print "shutting down neo4j..";
  os.system( "/home/matthaeus/Projects/neo4j-community-2.2.2/bin/neo4j stop" );
  time.sleep(10);
  print "dropping caches..";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "starting neo4j..";
  os.system( "su - matthaeus -c \"JAVA_OPTS=\\\"-Xmx4G\\\" /home/matthaeus/Projects/neo4j-community-2.2.2/bin/neo4j start\"" );
  time.sleep(10);
  print "setting up thread pool to "+ tps;
  os.system( "sed -i 's/thread.pool.size=.*/thread.pool.size="+ tps +"/' application.properties ");
  print "about to start evaluation..";
  time.sleep(5);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );

