#!/bin/sh
#
# $Id: cmd.real-dir.sh 11141 2009-06-12 09:58:09Z friebe $
#

assertHaveActiveTag

if [ "$OS" = "Windows_NT" ] ; then
  cygpath -m `realpath "$(tmpTagDir)"/current-tag`
else
  realpath "$(tmpTagDir)"/current-tag
fi
