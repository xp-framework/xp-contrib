#!/bin/sh
#
# $Id: cmd.revert.sh 12311 2010-06-25 11:22:09Z kiesel $
#

# Parse command line options
RECURSE=""
while getopts 'R' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    R)  RECURSE="-R" ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

assertHaveActiveTag

TAGDIR=$(tmpTagDir)/current-tag
cd "$TAGDIR"

while [ ! -z $1 ]; do
  TARGET=$1
  
  [ $VERBOSE ] && echo "Reverting $TARGET in $TAGDIR"
  
  LANG=C svn revert $RECURSE "$TARGET"
  shift 1
done
