@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  cache-throughput startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and CACHE_THROUGHPUT_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

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
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\Proyecto_Sistemas_Distribuidos-1.0-SNAPSHOT.jar;%APP_HOME%\lib\grpc-all-1.0.2.jar;%APP_HOME%\lib\ssj-3.2.0.jar;%APP_HOME%\lib\commons-math3-3.1.jar;%APP_HOME%\lib\grpc-auth-1.0.2.jar;%APP_HOME%\lib\grpc-context-1.0.2.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.0.2.jar;%APP_HOME%\lib\grpc-stub-1.0.2.jar;%APP_HOME%\lib\grpc-netty-1.0.2.jar;%APP_HOME%\lib\grpc-protobuf-1.0.2.jar;%APP_HOME%\lib\grpc-okhttp-1.0.2.jar;%APP_HOME%\lib\grpc-protobuf-nano-1.0.2.jar;%APP_HOME%\lib\grpc-core-1.0.2.jar;%APP_HOME%\lib\jfreechart-1.0.12.jar;%APP_HOME%\lib\colt-1.2.0.jar;%APP_HOME%\lib\optimization-1.3.jar;%APP_HOME%\lib\google-auth-library-credentials-0.4.0.jar;%APP_HOME%\lib\guava-19.0.jar;%APP_HOME%\lib\protobuf-lite-3.0.1.jar;%APP_HOME%\lib\netty-codec-http2-4.1.6.Final.jar;%APP_HOME%\lib\protobuf-java-util-3.0.2.jar;%APP_HOME%\lib\protobuf-java-3.0.2.jar;%APP_HOME%\lib\okio-1.6.0.jar;%APP_HOME%\lib\okhttp-2.5.0.jar;%APP_HOME%\lib\protobuf-javanano-3.0.0-alpha-5.jar;%APP_HOME%\lib\jsr305-3.0.0.jar;%APP_HOME%\lib\jcommon-1.0.15.jar;%APP_HOME%\lib\concurrent-1.3.4.jar;%APP_HOME%\lib\netty-codec-http-4.1.6.Final.jar;%APP_HOME%\lib\netty-handler-4.1.6.Final.jar;%APP_HOME%\lib\gson-2.3.jar;%APP_HOME%\lib\netty-codec-4.1.6.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.6.Final.jar;%APP_HOME%\lib\netty-transport-4.1.6.Final.jar;%APP_HOME%\lib\netty-common-4.1.6.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.6.Final.jar

@rem Execute cache-throughput
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %CACHE_THROUGHPUT_OPTS%  -classpath "%CLASSPATH%" cacheService.ClienteThroughput %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable CACHE_THROUGHPUT_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%CACHE_THROUGHPUT_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
