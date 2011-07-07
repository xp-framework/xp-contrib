#!/bin/sh
#
# $Id: cmd.tagname.sh 11139 2009-06-12 09:40:45Z friebe $
#

assertHaveActiveTag

cd "$(tmpTagDir)"/current-tag
TAGNAME=$(svn info . | grep ^URL: | cut -d ' ' -f 2)
basename "$TAGNAME"

