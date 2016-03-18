#!/usr/bin/python

# service stop
# sync && echo 3 > /proc/sys/vm/drop_caches
# service start

# sed -i 's/thread.pool.size=.*/thread.pool.size=15/' application.properties
# java -jar disco-evaluation-0.0.1-SNAPSHOT.jar

import os
import time

for tps in ["1","5","10","15","20"]:
  print "shutting down stardog..";
  os.system( "sudo -u matthaeus -H JAVA_HOME=\"/usr/lib/java/jdk1.8.0_65/\" /home/matthaeus/Projects/stardog-4.0.5/bin/stardog-admin server stop" );
  time.sleep(10);
  print "dropping caches..";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "starting stardog..";
  os.system( "sudo -u matthaeus -H JAVA_HOME=\"/usr/lib/java/jdk1.8.0_65/\" STARDOG_HOME=\"/home/matthaeus/Projects/disco-dataset/stardog\" /home/matthaeus/Projects/stardog-4.0.5/bin/stardog-admin server start --disable-security" );
  time.sleep(10);
  print "setting up thread pool to "+ tps;
  os.system( "sudo -u matthaeus -H sed -i 's/thread.pool.size=.*/thread.pool.size="+ tps +"/' application.properties ");
  print "about to start evaluation.. 5 seconds from now";
  time.sleep(5);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );

