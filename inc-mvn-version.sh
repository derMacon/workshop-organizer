#!/bin/bash

#get current hash and see if it already has a tag
GIT_COMMIT=`git rev-parse HEAD`
NEEDS_TAG=`git describe --contains $GIT_COMMIT 2>/dev/null`

#only tag if no tag already
#to publish, need to be logged in to npm, and with clean working directory: `npm login; git stash`
if [ -z "$NEEDS_TAG" ]; then
	echo 'no git tag for current commit'

	echo 'generate a new one after incrementing maven version'
	#increment only if needed
	mvn --batch-mode release:update-versions versions:set -DremoveSnapshot

	MVN_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)

	git tag $MVN_VERSION
	#git push --tags
	#git push

	echo 'created new git tag'
else
  echo "Already a tag on this commit"
fi

exit 0
