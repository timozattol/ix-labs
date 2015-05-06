export HADOOP_HOME=/usr/lib/hadoop
export MAHOUT_HOME=/usr/local/lib/mahout
export JAVA_HOME=/usr/lib/jvm/default-java

LINGPIPE_JAR=$MAHOUT_HOME/lingpipe-4.1.0.jar
MAHOUT_JAR=$MAHOUT_HOME/mahout-mrlegacy-1.0-SNAPSHOT-job.jar
export JARS=$MAHOUT_JAR,$LINGPIPE_JAR

alias mahout=$MAHOUT_HOME/bin/mahout
alias hadoop='HADOOP_CLASSPATH=$MAHOUT_JAR:$LINGPIPE_JAR hadoop'
