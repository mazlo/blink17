#!/usr/bin/python

# service stop
# sync && echo 3 > /proc/sys/vm/drop_caches
# service start

# sed -i 's/thread.pool.size=.*/thread.pool.size=15/' application.properties
# java -jar disco-evaluation-0.0.1-SNAPSHOT.jar

import os
import time

for tps in ["1","5","10","15","20"]:
  print "shutting down apache..";
  os.system( "sudo -u matthaeus -H /home/matthaeus/Projects/apache-tomcat-6.0.43/bin/shutdown.sh" );
  time.sleep(10);
  print "dropping caches..";
  os.system( "sync && echo 3 > /proc/sys/vm/drop_caches" );
  print "starting apache..";
  os.system( "sudo -u matthaeus -H JRE_HOME=\"/usr/lib/java/jdk1.7.0_75\" JAVA_OPTS=\"-Xms6G -Xmx6G\" /home/matthaeus/Projects/apache-tomcat-6.0.43/bin/startup.sh" );
  time.sleep(10);
  print "query to initialize db.."
  os.system( "curl http://localhost:8080/openrdf-workbench/repositories/in-memory-store/query?action=exec\&queryLn=SPARQL\&query=SELECT%28COUNT%28%3Fz%29AS%3Fc%29WHERE{%3Fx%20%3Fy%20%3Fz}LIMIT10" );
  time.sleep(10);
  print "setting up thread pool to "+ tps;
  os.system( "sed -i 's/thread.pool.size=.*/thread.pool.size="+ tps +"/' application.properties ");
  print "about to start evaluation..";
  time.sleep(5);
  os.system( "java -jar disco-evaluation-0.0.1-SNAPSHOT.jar" );

