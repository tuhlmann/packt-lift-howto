set SCRIPT_DIR=%~dp0
set JREBEL_HOME=C:/ewu/jrebel

java -Dfile.encoding=UTF8 -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m -Xmx1024M -Xss2M -XX:+UseConcMarkSweepGC -server -noverify -javaagent:%JREBEL_HOME%/jrebel.jar -Drebel.lift_plugin=true -jar "%SCRIPT_DIR%\sbt-launch-0.11.3.jar" %*
