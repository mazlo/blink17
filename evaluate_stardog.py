#!/usr/bin/python

# service stop
# sync && echo 3 > /proc/sys/vm/drop_caches
# service start

# sed -i 's/thread.pool.size=.*/thread.pool.size=15/' application.properties
# java -jar disco-evaluation-0.0.1-SNAPSHOT.jar

import os
import time
import sys

from evaluate_each_distribution import run_evaluation

distribution = sys.argv[1] if ( len(sys.argv) > 1 and sys.argv[1] != '' ) else "equal"

def withStardog():
  print "[INFO] shutting down stardog";
  os.system( "sudo -u matthaeus -H JAVA_HOME=\"/usr/lib/java/jdk1.8.0_65/\" /home/matthaeus/Projects/stardog-4.0.5/bin/stardog-admin server stop" );
  time.sleep(10);
  print "[INFO] dropping caches";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "[INFO] starting stardog";
  os.system( "sudo -u matthaeus -H JAVA_HOME=\"/usr/lib/java/jdk1.8.0_65/\" STARDOG_HOME=\"/home/matthaeus/Projects/disco-dataset/stardog\" /home/matthaeus/Projects/stardog-4.0.5/bin/stardog-admin server start --disable-security" );
  print "[INFO] about to start evaluation...";
  time.sleep(10);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );
  return

run_evaluation( distribution, 'stardog', withStardog )
