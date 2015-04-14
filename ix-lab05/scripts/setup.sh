export HADOOP_HOME=/usr/lib/hadoop
export MAHOUT_HOME=/usr/local/lib/mahout
export JAVA_HOME=/usr/lib/jvm/default-java

export MAHOUT_JAR=$MAHOUT_HOME/mahout-mrlegacy-1.0-SNAPSHOT-job.jar

alias mahout=$MAHOUT_HOME/bin/mahout
alias hadoop='HADOOP_CLASSPATH=$MAHOUT_JAR hadoop'
