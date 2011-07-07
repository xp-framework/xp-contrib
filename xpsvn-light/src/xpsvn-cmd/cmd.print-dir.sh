#!/bin/sh
#
# $Id: cmd.print-dir.sh 10914 2009-03-26 10:21:15Z kiesel $
#

assertHaveActiveTag

echo "$(tmpTagDir)"/current-tag
