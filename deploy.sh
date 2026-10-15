#!/usr/bin/env bash
set -e

if [ -z "$TOMCAT_HOME" ]; then
    for candidate in /opt/tomcat /usr/local/tomcat "$HOME/tomcat"; do
        if [ -d "$candidate" ]; then
            TOMCAT_HOME="$candidate"
            break
        fi
    done
fi

if [ -z "$TOMCAT_HOME" ] || [ ! -d "$TOMCAT_HOME" ]; then
    echo "Set TOMCAT_HOME"
    exit 1
fi

echo "Deploying database schema..."
mysql -u root -p < schema.sql

echo "Copying smart-city-web.war to $TOMCAT_HOME/webapps/..."
cp smart-city-web.war "$TOMCAT_HOME/webapps/"

echo "Stopping Tomcat if running..."
"$TOMCAT_HOME/bin/shutdown.sh" 2>/dev/null || true

sleep 3

echo "Starting Tomcat..."
"$TOMCAT_HOME/bin/startup.sh"

sleep 8

if [ -f "$TOMCAT_HOME/logs/catalina.out" ]; then
    echo "--- Last 30 lines of catalina.out ---"
    tail -n 30 "$TOMCAT_HOME/logs/catalina.out"
fi

echo "Open http://localhost:8080/smart-city-web/login"
