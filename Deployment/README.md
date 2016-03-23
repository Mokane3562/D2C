Requires: 
- Apache Tomcat 8 (Tested on 8.0.32, included in this deployment)
- JRE version 8 (Tested on OpenJDK 1.8.0_66)
- MySQL (Tested on version 5.6.28 running on debian-linux-gnu)

for a fresh install: 
-Start mysql server service if it is not already running
-Start mysql console
-Copy the contents of d2c.sql
-Paste the contents to the console 
-Ensure that you set your CATALINE_HOME and JAVA_HOME variables to the locations to your Tomcat 8 and Java 8 locations respectively
-Start your tomcat server by running the startup script (the provided server can be started on linux by running the startup.sh script in the bin folder)
-Open your browser and go to "localhost:8080"
-Click on "Manager app" and enter the admin user and password (the provided erver uses admin as the user and password)
-Scroll down and click on the browse button under "WAR file to deploy"
-Browse to the d2c.war file and select it
-Click deploy
-You can now check out the app at localhost:8080/d2c

restarting after shutdown:
-Start mysql server service if it is not already running
-Run the Apache Tomcat 8 startup.sh file
-You can now check out the app at localhost:8080/d2c
