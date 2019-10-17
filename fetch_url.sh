#!/bin/sh

url="$1"
tCount="$2"
rCount="$3"
java -jar target/java-app-1.0-SNAPSHOT.jar $url $tCount $rCount 
