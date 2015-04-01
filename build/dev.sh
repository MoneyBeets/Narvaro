#!/usr/bin/env bash
#
# Development Script
#
# This script is used for development of Narvaro.
#
# Script will clean the target directory, then build Narvaro,
#  after-which this script will launch Narvaro.
#
# This script is meant as a convenience for developing Narvaro.
#

set -o errexit

if [ ! -d "${JAVA_HOME}" ]; then
    echo ""
    echo "JAVA_HOME environment variable not set"
    echo ""
    exit 1
fi

# clean Narvaro target dir
ant clean

# build Narvaro
ant narvaro

# cd into Narvaro target bin/ directory
cd ../target/narvaro/bin

# launch Narvaro
./run.sh

