echo off

java -cp %CLASSPATH%;%~dp0Amateur-1.1.jar -Djava.security.manager -Djava.security.policy==%~dp0AllPermission.policy com.elharo.quicktime.Main

