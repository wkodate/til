@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  pravega-controller-withGCLogging startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PRAVEGA_CONTROLLER_WITH_GC_LOGGING_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-server" "-Xms128m" "-XX:+HeapDumpOnOutOfMemoryError" "-XX:+PrintGCDetails" "-XX:+PrintGCDateStamps" "-Xloggc:PRAVEGA_APP_HOME/logs/gc.log" "-XX:+UseGCLogFileRotation" "-XX:NumberOfGCLogFiles=2" "-XX:GCLogFileSize=64m" "-Dconfig.file=PRAVEGA_APP_HOME/conf/controller.conf" "-Dlogback.configurationFile=PRAVEGA_APP_HOME/conf/logback.xml" "-Dlog.dir=PRAVEGA_APP_HOME/logs" "-Dlog.name=controller"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\pravega-controller-0.2.1.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\pravega-common-0.2.1.jar;%APP_HOME%\lib\pravega-shared-controller-api-0.2.1.jar;%APP_HOME%\lib\pravega-client-0.2.1.jar;%APP_HOME%\lib\pravega-shared-metrics-0.2.1.jar;%APP_HOME%\lib\javax.servlet-api-4.0.0.jar;%APP_HOME%\lib\swagger-jersey2-jaxrs-1.5.16.jar;%APP_HOME%\lib\annotations-3.0.1.jar;%APP_HOME%\lib\config-1.3.1.jar;%APP_HOME%\lib\curator-framework-4.0.0.jar;%APP_HOME%\lib\curator-recipes-4.0.0.jar;%APP_HOME%\lib\curator-client-4.0.0.jar;%APP_HOME%\lib\commons-lang3-3.6.jar;%APP_HOME%\lib\jersey-container-grizzly2-http-2.26.jar;%APP_HOME%\lib\jersey-hk2-2.26.jar;%APP_HOME%\lib\jersey-media-json-jackson-2.26.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\commons-io-2.5.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\lombok-1.16.18.jar;%APP_HOME%\lib\netty-all-4.1.15.Final.jar;%APP_HOME%\lib\grpc-netty-1.6.1.jar;%APP_HOME%\lib\grpc-protobuf-1.6.1.jar;%APP_HOME%\lib\grpc-stub-1.6.1.jar;%APP_HOME%\lib\pravega-shared-0.2.1.jar;%APP_HOME%\lib\pravega-shared-protocol-0.2.1.jar;%APP_HOME%\lib\metrics3-statsd-4.2.0.jar;%APP_HOME%\lib\metrics-core-3.2.5.jar;%APP_HOME%\lib\metrics-jvm-3.2.5.jar;%APP_HOME%\lib\metrics-graphite-3.2.5.jar;%APP_HOME%\lib\metrics-ganglia-3.2.5.jar;%APP_HOME%\lib\gmetric4j-1.0.10.jar;%APP_HOME%\lib\swagger-jaxrs-1.5.16.jar;%APP_HOME%\lib\jersey-container-servlet-core-2.25.1.jar;%APP_HOME%\lib\jersey-media-multipart-2.25.1.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\jsr305-3.0.1.jar;%APP_HOME%\lib\zookeeper-3.5.3-beta.jar;%APP_HOME%\lib\javax.inject-2.5.0-b42.jar;%APP_HOME%\lib\grizzly-http-server-2.4.0.jar;%APP_HOME%\lib\jersey-common-2.26.jar;%APP_HOME%\lib\jersey-server-2.26.jar;%APP_HOME%\lib\javax.ws.rs-api-2.1.jar;%APP_HOME%\lib\hk2-locator-2.5.0-b42.jar;%APP_HOME%\lib\jersey-entity-filtering-2.26.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.8.4.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\grpc-core-1.6.1.jar;%APP_HOME%\lib\netty-codec-http2-4.1.14.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.14.Final.jar;%APP_HOME%\lib\protobuf-java-3.3.0.jar;%APP_HOME%\lib\protobuf-java-util-3.3.1.jar;%APP_HOME%\lib\proto-google-common-protos-0.1.9.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.6.1.jar;%APP_HOME%\lib\metrics-statsd-common-4.2.0.jar;%APP_HOME%\lib\swagger-core-1.5.16.jar;%APP_HOME%\lib\reflections-0.9.11.jar;%APP_HOME%\lib\mimepull-1.9.6.jar;%APP_HOME%\lib\commons-cli-1.2.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\netty-3.10.5.Final.jar;%APP_HOME%\lib\grizzly-http-2.4.0.jar;%APP_HOME%\lib\javax.annotation-api-1.2.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.1.jar;%APP_HOME%\lib\jersey-client-2.26.jar;%APP_HOME%\lib\jersey-media-jaxb-2.26.jar;%APP_HOME%\lib\validation-api-1.1.0.Final.jar;%APP_HOME%\lib\aopalliance-repackaged-2.5.0-b42.jar;%APP_HOME%\lib\hk2-api-2.5.0-b42.jar;%APP_HOME%\lib\hk2-utils-2.5.0-b42.jar;%APP_HOME%\lib\javassist-3.22.0-CR2.jar;%APP_HOME%\lib\grpc-context-1.6.1.jar;%APP_HOME%\lib\error_prone_annotations-2.0.19.jar;%APP_HOME%\lib\instrumentation-api-0.4.3.jar;%APP_HOME%\lib\opencensus-api-0.5.1.jar;%APP_HOME%\lib\netty-codec-http-4.1.14.Final.jar;%APP_HOME%\lib\netty-handler-4.1.14.Final.jar;%APP_HOME%\lib\netty-transport-4.1.14.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.14.Final.jar;%APP_HOME%\lib\gson-2.7.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.8.9.jar;%APP_HOME%\lib\swagger-models-1.5.16.jar;%APP_HOME%\lib\grizzly-framework-2.4.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\netty-codec-4.1.14.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.14.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.14.Final.jar;%APP_HOME%\lib\snakeyaml-1.17.jar;%APP_HOME%\lib\swagger-annotations-1.5.16.jar;%APP_HOME%\lib\netty-common-4.1.14.Final.jar;%APP_HOME%\lib\remotetea-oncrpc-1.1.2.jar;%APP_HOME%\lib\jackson-databind-2.8.9.jar;%APP_HOME%\lib\jackson-annotations-2.8.9.jar;%APP_HOME%\lib\jackson-core-2.8.9.jar

@rem Execute pravega-controller-withGCLogging
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PRAVEGA_CONTROLLER_WITH_GC_LOGGING_OPTS%  -classpath "%CLASSPATH%" io.pravega.controller.server.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PRAVEGA_CONTROLLER_WITH_GC_LOGGING_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PRAVEGA_CONTROLLER_WITH_GC_LOGGING_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
