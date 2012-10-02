set SCRIPT_DIR=%~dp0
java -Dfile.encoding=UTF8 -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m -Xmx1024M -Xss2M -XX:+UseConcMarkSweepGC -jar "%SCRIPT_DIR%\sbt-launch-0.11.3.jar" %*
