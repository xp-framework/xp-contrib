#!/bin/sh
#
# $Id: cmd.tagdiff.sh 12309 2010-06-25 11:22:03Z kiesel $
#

assertHaveActiveTag

if [ ! -z $1 ]; then
  while [ ! -z $1 ]; do
    TARGET=$(fetchTarget $1)
    [ -z "$TARGET" ] && exit 1

    RELTARGET=$(relativeTarget "$TARGET")
    ( cd "$(tmpTagDir)"/current-tag && LANG=C svn diff $RELTARGET )

    shift 1
  done
else
  cd "$(tmpTagDir)"/current-tag
  LANG=C svn diff $TARGET
fi

