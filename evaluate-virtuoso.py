#!/usr/bin/python

import os
import time

for tps in ["1"]:
  print "shutting down virtuoso..";
  os.system( "/home/matthaeus/Projects/disco-dataset/virtuoso/shutdown_virtuoso.sh" );
  time.sleep(10);
  print "dropping caches..";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "starting virtuoso..";
  os.system( "/home/matthaeus/Projects/disco-dataset/virtuoso/start_virtuoso.sh" );
  print "waiting for server to start...";
  time.sleep(30);
  print "setting up thread pool to "+ tps;
  os.system( "sudo -u matthaeus -H sed -i 's/thread.pool.size=.*/thread.pool.size="+ tps +"/' application.properties ");
  print "about to start evaluation..";
  time.sleep(5);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );

