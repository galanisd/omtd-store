#!/bin/bash

PIDFILE=/tmp/omtd-store.pid
SERVICENAME="OMTD Store Service"

start() {
	[ -f $PIDFILE ] && echo $SERVICENAME" already running ($(cat $PIDFILE))" && exit 0
    if [ -z "$1" ]
  	then
    	echo "Starting "$SERVICENAME" with default config."
		sh ./LinuxStartOMTDStoreServiceWithDefaultConfig.sh > /dev/null 2>&1 &
		echo $! > $PIDFILE;
	else
		sh ./LinuxStartOMTDStoreService.sh $2 > /dev/null 2>&1 &
		echo $! > $PIDFILE;
	fi  	
	cat $PIDFILE
}

stop() {
    PID=$(cat $PIDFILE 2>/dev/null)
    [ "$PID" == "" ] && echo $SERVICENAME" not running" && exit 0
    kill -9 $PID
    rm -f $PIDFILE
}

status() {
    if [ -f $PIDFILE ]; then
        PID=$(cat $PIDFILE)
        echo $SERVICENAME" running ($PID)"
    else
        echo $SERVICENAME" not running"
    fi
}

case $1 in
    start)
        start;
    ;;
    stop)
        stop;
    ;;
    restart)
        stop && start;
    ;;
    status)
        status;
    ;;
    *)
        echo "$0 {start|stop|status|restart}"
    ;;
esac
