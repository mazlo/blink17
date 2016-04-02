#!/usr/bin/python

import os
import time

from evaluate_each_distribution import run_evaluation

distribution = sys.argv[1] if ( len(sys.argv) > 1 and sys.argv[1] != '' ) else "equal"

def withVirtuoso():
  print "[INFO] shutting down virtuoso";
  os.system( "/home/matthaeus/Projects/disco-dataset/virtuoso/shutdown_virtuoso.sh" );
  time.sleep(10);
  print "[INFO] dropping caches";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "[INFO] starting virtuoso";
  os.system( "/home/matthaeus/Projects/disco-dataset/virtuoso/start_virtuoso.sh" );
  print "[INFO] waiting for server to start...";
  time.sleep(30);
  print "[INFO] about to start evaluation...";
  time.sleep(10);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );
  return

run_evaluation( distribution, 'virtuoso', withVirtuoso )
