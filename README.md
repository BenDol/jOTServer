jOTServer [![Build Status](https://travis-ci.org/BenDol/jOTServer.svg?branch=master)](https://travis-ci.org/BenDol/jOTServer)
=========

Java Open Tibia server implementation made using SQL database for storage, AspectJ, JUnit, Apache MINA, StripesFramework, and Maven. This project was forked from [John David (Jiddo)](http://www.jiddo.net/index.php/projects/tool-library-projects/79-jotserver)

## Compilation ##

In order to compile the project, ensure that Maven is installed on your system. 
Then, from the jotserver/server folder, run:
mvn assembly:assembly

That should produce a runnable jar file at:
jotserver/server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar

In order to run the server, run the runnable jar file with the current working
directory being jotserver/server. 

The web interface is located in the jotserver/web folder and can be run or 
deployed separately. 
