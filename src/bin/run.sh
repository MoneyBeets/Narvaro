#!/usr/bin/env bash
#
# Narvaro: @VERSION@
# Build Date: @DATE@
# Commit Head: @HEAD@
# JDK: @JDK@
# ANT: @ANT@
#
# Narvaro Launch Script
#
# This script launches the Narvaro application.
#

set -o errexit

javaNotSet() {
    echo ""
    echo "JAVA_HOME environment variable not set."
    echo ""
    exit 1 
}

if [ ! -d "${JAVA_HOME}" ]; then
    javaNotSet
fi
if [ ! -f "${JAVA_HOME}/bin/java" ]; then
     javaNotSet
fi

"${JAVA_HOME}/bin/java" -DnarvaroHome="${NARVARO_HOME}" -jar ../lib/startup.jar
