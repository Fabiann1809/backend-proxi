#!/bin/sh
# Maven wrapper script
set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ -r "$MAVEN_WRAPPER_PROPERTIES" ]; then
  DISTRIBUTION_URL=$(sed -n 's/^distributionUrl=//p' "$MAVEN_WRAPPER_PROPERTIES")
fi

MAVEN_HOME="${MAVEN_USER_HOME:-${user.home}/.m2/wrapper}/dists/$(basename "$DISTRIBUTION_URL" .zip)"

if [ -x "$MAVEN_HOME/bin/mvn" ]; then
  exec "$MAVEN_HOME/bin/mvn" "$@"
fi

if command -v mvn > /dev/null 2>&1; then
  exec mvn "$@"
fi

echo "ERROR: Maven not found. Install Maven or run: ./mvnw" >&2
exit 1
