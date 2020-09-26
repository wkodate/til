@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  pravega-segmentstore-withGCLogging startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and PRAVEGA_SEGMENTSTORE_WITH_GC_LOGGING_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-server" "-Xms512m" "-XX:+HeapDumpOnOutOfMemoryError" "-XX:+PrintGCDetails" "-XX:+PrintGCDateStamps" "-Xloggc:PRAVEGA_APP_HOME/logs/gc.log" "-XX:+UseGCLogFileRotation" "-XX:NumberOfGCLogFiles=2" "-XX:GCLogFileSize=64m" "-Dlog.dir=PRAVEGA_APP_HOME/logs" "-Dlog.name=segmentstore" "-Dpravega.configurationFile=PRAVEGA_APP_HOME/conf/config.properties" "-Dlogback.configurationFile=PRAVEGA_APP_HOME/conf/logback.xml"

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

set CLASSPATH=%APP_HOME%\lib\pravega-segmentstore-server-host-0.2.1.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\pravega-common-0.2.1.jar;%APP_HOME%\lib\pravega-segmentstore-contracts-0.2.1.jar;%APP_HOME%\lib\pravega-client-0.2.1.jar;%APP_HOME%\lib\pravega-segmentstore-storage-0.2.1.jar;%APP_HOME%\lib\pravega-segmentstore-storage-impl-0.2.1.jar;%APP_HOME%\lib\pravega-segmentstore-server-0.2.1.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\commons-io-2.5.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\annotations-3.0.1.jar;%APP_HOME%\lib\lombok-1.16.18.jar;%APP_HOME%\lib\netty-all-4.1.15.Final.jar;%APP_HOME%\lib\pravega-shared-0.2.1.jar;%APP_HOME%\lib\pravega-shared-protocol-0.2.1.jar;%APP_HOME%\lib\pravega-shared-controller-api-0.2.1.jar;%APP_HOME%\lib\pravega-shared-metrics-0.2.1.jar;%APP_HOME%\lib\bookkeeper-server-4.5.0.jar;%APP_HOME%\lib\rocksdbjni-5.8.6.jar;%APP_HOME%\lib\hadoop-common-2.8.1.jar;%APP_HOME%\lib\hadoop-hdfs-2.8.1.jar;%APP_HOME%\lib\object-client-3.0.0.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\jcip-annotations-1.0.jar;%APP_HOME%\lib\jsr305-3.0.1.jar;%APP_HOME%\lib\commons-lang3-3.6.jar;%APP_HOME%\lib\curator-recipes-4.0.0.jar;%APP_HOME%\lib\grpc-netty-1.6.1.jar;%APP_HOME%\lib\grpc-protobuf-1.6.1.jar;%APP_HOME%\lib\grpc-stub-1.6.1.jar;%APP_HOME%\lib\metrics3-statsd-4.2.0.jar;%APP_HOME%\lib\metrics-core-3.2.5.jar;%APP_HOME%\lib\metrics-jvm-3.2.5.jar;%APP_HOME%\lib\metrics-graphite-3.2.5.jar;%APP_HOME%\lib\metrics-ganglia-3.2.5.jar;%APP_HOME%\lib\gmetric4j-1.0.10.jar;%APP_HOME%\lib\bookkeeper-stats-api-4.5.0.jar;%APP_HOME%\lib\zookeeper-3.5.3-beta.jar;%APP_HOME%\lib\commons-configuration-1.6.jar;%APP_HOME%\lib\commons-cli-1.2.jar;%APP_HOME%\lib\jna-3.2.7.jar;%APP_HOME%\lib\netty-tcnative-boringssl-static-2.0.3.Final.jar;%APP_HOME%\lib\hadoop-annotations-2.8.1.jar;%APP_HOME%\lib\commons-math3-3.1.1.jar;%APP_HOME%\lib\xmlenc-0.52.jar;%APP_HOME%\lib\httpclient-4.5.2.jar;%APP_HOME%\lib\commons-net-3.1.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\servlet-api-2.5.jar;%APP_HOME%\lib\jetty-6.1.26.jar;%APP_HOME%\lib\jetty-util-6.1.26.jar;%APP_HOME%\lib\jetty-sslengine-6.1.26.jar;%APP_HOME%\lib\jsp-api-2.1.jar;%APP_HOME%\lib\jersey-json-1.9.jar;%APP_HOME%\lib\jersey-server-1.9.jar;%APP_HOME%\lib\jets3t-0.9.0.jar;%APP_HOME%\lib\commons-lang-2.6.jar;%APP_HOME%\lib\jackson-core-asl-1.9.13.jar;%APP_HOME%\lib\jackson-mapper-asl-1.9.13.jar;%APP_HOME%\lib\avro-1.7.4.jar;%APP_HOME%\lib\protobuf-java-3.3.0.jar;%APP_HOME%\lib\hadoop-auth-2.8.1.jar;%APP_HOME%\lib\jsch-0.1.51.jar;%APP_HOME%\lib\htrace-core4-4.0.1-incubating.jar;%APP_HOME%\lib\commons-compress-1.4.1.jar;%APP_HOME%\lib\hadoop-hdfs-client-2.8.1.jar;%APP_HOME%\lib\commons-daemon-1.0.13.jar;%APP_HOME%\lib\xercesImpl-2.9.1.jar;%APP_HOME%\lib\leveldbjni-all-1.8.jar;%APP_HOME%\lib\slf4j-log4j12-1.7.5.jar;%APP_HOME%\lib\smart-client-2.1.0.jar;%APP_HOME%\lib\object-transform-1.1.0.jar;%APP_HOME%\lib\jdom2-2.0.6.jar;%APP_HOME%\lib\grpc-core-1.6.1.jar;%APP_HOME%\lib\netty-codec-http2-4.1.14.Final.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.14.Final.jar;%APP_HOME%\lib\protobuf-java-util-3.3.1.jar;%APP_HOME%\lib\proto-google-common-protos-0.1.9.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.6.1.jar;%APP_HOME%\lib\metrics-statsd-common-4.2.0.jar;%APP_HOME%\lib\commons-digester-1.8.jar;%APP_HOME%\lib\commons-beanutils-core-1.8.0.jar;%APP_HOME%\lib\httpcore-4.4.4.jar;%APP_HOME%\lib\jettison-1.1.jar;%APP_HOME%\lib\jaxb-impl-2.2.3-1.jar;%APP_HOME%\lib\jackson-jaxrs-1.8.3.jar;%APP_HOME%\lib\jackson-xc-1.8.3.jar;%APP_HOME%\lib\asm-3.1.jar;%APP_HOME%\lib\java-xmlbuilder-0.4.jar;%APP_HOME%\lib\paranamer-2.3.jar;%APP_HOME%\lib\snappy-java-1.0.4.1.jar;%APP_HOME%\lib\nimbus-jose-jwt-3.9.jar;%APP_HOME%\lib\apacheds-kerberos-codec-2.0.0-M15.jar;%APP_HOME%\lib\curator-framework-4.0.0.jar;%APP_HOME%\lib\xz-1.0.jar;%APP_HOME%\lib\okhttp-2.4.0.jar;%APP_HOME%\lib\jersey-apache-client4-1.19.jar;%APP_HOME%\lib\jersey-client-1.19.jar;%APP_HOME%\lib\lzma-sdk-4j-9.22.0.jar;%APP_HOME%\lib\grpc-context-1.6.1.jar;%APP_HOME%\lib\error_prone_annotations-2.0.19.jar;%APP_HOME%\lib\instrumentation-api-0.4.3.jar;%APP_HOME%\lib\opencensus-api-0.5.1.jar;%APP_HOME%\lib\netty-codec-http-4.1.14.Final.jar;%APP_HOME%\lib\netty-handler-4.1.14.Final.jar;%APP_HOME%\lib\netty-transport-4.1.14.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.14.Final.jar;%APP_HOME%\lib\commons-beanutils-1.7.0.jar;%APP_HOME%\lib\servlet-api-2.5-20081211.jar;%APP_HOME%\lib\jaxb-api-2.2.2.jar;%APP_HOME%\lib\json-smart-1.1.1.jar;%APP_HOME%\lib\apacheds-i18n-2.0.0-M15.jar;%APP_HOME%\lib\api-asn1-api-1.0.0-M20.jar;%APP_HOME%\lib\api-util-1.0.0-M20.jar;%APP_HOME%\lib\okio-1.4.0.jar;%APP_HOME%\lib\netty-codec-4.1.14.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.14.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.14.Final.jar;%APP_HOME%\lib\stax-api-1.0-2.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\netty-common-4.1.14.Final.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\remotetea-oncrpc-1.1.2.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\gson-2.7.jar;%APP_HOME%\lib\curator-client-4.0.0.jar;%APP_HOME%\lib\jersey-core-1.19.jar;%APP_HOME%\lib\jsr311-api-1.1.1.jar;%APP_HOME%\lib\netty-3.10.5.Final.jar

@rem Execute pravega-segmentstore-withGCLogging
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PRAVEGA_SEGMENTSTORE_WITH_GC_LOGGING_OPTS%  -classpath "%CLASSPATH%" io.pravega.controller.server.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PRAVEGA_SEGMENTSTORE_WITH_GC_LOGGING_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PRAVEGA_SEGMENTSTORE_WITH_GC_LOGGING_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
