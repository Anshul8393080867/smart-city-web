@echo off
setlocal enabledelayedexpansion

if "%TOMCAT_HOME%"=="" (
    if exist "C:\apache-tomcat-10*" (
        for /d %%D in ("C:\apache-tomcat-10*") do set "TOMCAT_HOME=%%D"
    ) else if exist "C:\Program Files\Apache Software Foundation\Tomcat 10*" (
        for /d %%D in ("C:\Program Files\Apache Software Foundation\Tomcat 10*") do set "TOMCAT_HOME=%%D"
    )
)

if "%TOMCAT_HOME%"=="" (
    echo Set TOMCAT_HOME
    exit /b 1
)

if not exist "%TOMCAT_HOME%" (
    echo Set TOMCAT_HOME
    exit /b 1
)

echo Deploying database schema...
mysql -u root -p < schema.sql
if %errorlevel% neq 0 (
    echo Database deployment failed. Check credentials and MySQL service.
    exit /b %errorlevel%
)

echo Copying smart-city-web.war to %TOMCAT_HOME%\webapps\...
copy /y smart-city-web.war "%TOMCAT_HOME%\webapps\" >nul

echo Stopping Tomcat if running...
call "%TOMCAT_HOME%\bin\shutdown.bat" 2>nul

timeout /t 3 /nobreak >nul

echo Starting Tomcat...
call "%TOMCAT_HOME%\bin\startup.bat"

timeout /t 8 /nobreak >nul

for /f "delims=" %%F in ('dir /b /o-d "%TOMCAT_HOME%\logs\catalina.*.log" 2>nul') do (
    echo --- Recent lines from %%F ---
    powershell -NoProfile -Command "Get-Content -Path '%TOMCAT_HOME%\logs\%%F' -Tail 30" 2>nul
    goto :done_logs
)
:done_logs

echo Open http://localhost:8080/smart-city-web/login
