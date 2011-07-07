#!/bin/sh
#
# $Id: cmd.tagcreate.sh 12360 2010-06-30 08:14:48Z olli $
#

TAG=$1

svn ls $(repositoryRoot "$REPOBASE")/tags/$TAG >/dev/null 2>&1
if [ $? -eq 0 ]; then
  echo "Tag does already exist. Aborting.";
  exit 1;
fi

# Now create...
echo "===> Creating directory structure for tag $TAG ..."
LANG=C svn mkdir "$(repositoryRoot)"/tags/$TAG
