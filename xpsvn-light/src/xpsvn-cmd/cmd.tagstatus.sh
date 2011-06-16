#!/bin/sh
#
# $Id: cmd.tagstatus.sh 12309 2010-06-25 11:22:03Z kiesel $
#


# Parse command line options
OPTS=""
while getopts 'v' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    v)  OPTS="$OPTS -v"  ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

assertHaveActiveTag

cd "$(tmpTagDir)"/current-tag

echo -n "Status on: "
LANG=C svn info . | grep ^URL: | cut -d ' ' -f 2
LANG=C svn status $OPTS
