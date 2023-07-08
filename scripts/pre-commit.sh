#!/bin/sh

R='\033[0;31m'
CLEAN='\033[0;0m'

echo "Running spotlessCheck."
./gradlew spotlessCheck || (./gradlew spotlessApply && (echo -e "${R}Code was not formatted. Please add staged files and try again${CLEAN}" && exit 1))