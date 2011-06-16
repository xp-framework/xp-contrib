#!/bin/sh
#
# $Id: cmd.tagupdate.sh 12309 2010-06-25 11:22:03Z kiesel $
#

assertHaveActiveTag

cd "$(tmpTagDir)"/current-tag
LANG=C svn update
