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

def withMysql():
  print "[INFO] shutting down mysql"
  os.system( "service mysqld stop" );
  time.sleep(10);
  print "[INFO] dropping caches";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "[INFO] starting mysql"
  os.system( "service mysqld start" );
  print "[INFO] about to start evaluation...";
  time.sleep(10);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );
  return

run_evaluation( distribution, 'mysql', withMysql, weights=['100_0'] )
