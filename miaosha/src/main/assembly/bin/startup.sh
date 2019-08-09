#!/bin/bash
. /etc/profile
#check JAVA_HOME & java
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi

if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi

if $noJavaHome ; then
    echo
    echo "Error: JAVA_HOME environment variable is not set."
    echo
    exit 1
fi

cd `dirname $0`/..
BASE_HOME=`pwd`

if [ -z "$BASE_HOME" ] ; then
    echo
    echo "Error: BASE_HOME environment variable is not defined correctly."
    echo
    exit 1
fi

#==============================================================================

#set JAVA_OPTS
JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn256m -Xss256k"
#performance Options
JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableAttachMechanism"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.local.only=true"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.rmi.port=12244"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=12244"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
#GC Log Options
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
#debug Options
#JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=8065,server=y,suspend=n"
#==============================================================================
TEMP_CLASSPATH="$BASE_HOME/conf:$BASE_HOME/lib/classes"
for i in "$BASE_HOME"/lib/*.jar
do
    TEMP_CLASSPATH="$TEMP_CLASSPATH:$i"
done
#=================================================================
#startup server
RUN_CMD="$JAVA_HOME/bin/java"
RUN_CMD="$RUN_CMD -classpath $TEMP_CLASSPATH"
RUN_CMD="$RUN_CMD $JAVA_OPTS"
RUN_CMD="$RUN_CMD com.miaosha.project.App 1>>/dev/null 2>&1 &"

echo $RUN_CMD
eval $RUN_CMD
echo "miaosha server start up!"



