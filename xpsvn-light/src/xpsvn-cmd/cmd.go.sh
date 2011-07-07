#!/bin/sh
#
# $Id: cmd.go.sh 10814 2009-03-09 17:01:47Z kiesel $
#

assertHaveActiveTag

echo "---> Starting temporary shell, use 'exit' to return where you came from..."
cd "$(tmpTagDir)"/current-tag && $SHELL
