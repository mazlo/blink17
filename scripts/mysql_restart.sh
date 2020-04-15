#!/usr/bin/bash

service mysqld stop
sleep 1
sync && echo 3 > /proc/sys/vm/drop_caches
service mysqld start
sleep 2
