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

if [ ! -d "${JAVA_HOME}" ] || [ ! -f "${JAVA_HOME}/bin/java" ]; then
    echo ""
    echo "JAVA_HOME environment variable not set."
    echo ""
    exit 1 
fi

NARVARO_HOME="$(basename pwd)"

"${JAVA_HOME}/bin/java" -DnarvaroHome="${NARVARO_HOME}" -jar ../lib/startup.jar
