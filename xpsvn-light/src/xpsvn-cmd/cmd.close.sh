#!/bin/sh
#
# $Id: cmd.close.sh 11029 2009-04-28 13:34:57Z olli $
#

TAGDIR=$(tmpTagDir)
TAG=$1

if [ -z $TAG ]; then
  assertHaveActiveTag
  TAG=$(activeTag)
fi

[ "$(activeTag)" = "$TAG" ] && rm "$TAGDIR"/current-tag

rm -rf "$TAGDIR"/$TAG
