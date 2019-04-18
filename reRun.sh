mvn package -Dmaven.test.skip=true
mv target/toutiao-0.0.1-SNAPSHOT.war /usr/tomcat/webapps/
/usr/tomcat/bin/shutdown.sh
/usr/tomcat/bin/startup.sh
