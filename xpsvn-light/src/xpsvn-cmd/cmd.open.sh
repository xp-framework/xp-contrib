#!/bin/sh
#
# $Id: cmd.open.sh 12360 2010-06-30 08:14:48Z olli $
#

TAGDIR=$(tmpTagDir)
TAG=$1

[ -z $TAG ] && exit 1

if [ ! -d "$TAGDIR" ]; then
  mkdir -p "$TAGDIR";
fi

[ -L "$TAGDIR"/current-tag ] && rm "$TAGDIR"/current-tag

if [ ! -d "$TAGDIR"/$TAG ]; then
  LANG=C svn co $(repositoryRoot "$REPOBASE")/tags/$TAG "$TAGDIR"/$TAG && ln -s $TAG "$TAGDIR"/current-tag
else
  ln -s $TAG "$TAGDIR"/current-tag
  echo "$TAG is now the active tag!"
fi
