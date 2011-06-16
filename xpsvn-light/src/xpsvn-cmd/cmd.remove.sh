#!/bin/sh
#
# $Id: cmd.remove.sh 11615 2009-11-04 15:16:40Z kiesel $
#

# Parse command line options
FORCE=""
while getopts 'f' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    f)  FORCE="--force" ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

assertHaveActiveTag

TAGDIR=$(tmpTagDir)/current-tag

while [ ! -z $1 ]; do
  TARGET=$1
  
  [ $VERBOSE ] && echo "Removing $TARGET in $TAGDIR"
  
  LANG=C svn remove $FORCE $TAGDIR/$TARGET
  shift 1
done
