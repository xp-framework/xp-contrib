#!/bin/sh
#
# $Id: cmd.tagcommit.sh 12309 2010-06-25 11:22:03Z kiesel $
#

assertHaveActiveTag

# Parse command line options
COMMENT=""
while getopts 'm:' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    m)  COMMENT="$OPTARG" ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

cd "$(tmpTagDir)"/current-tag
echo -n "===> Current status in " ; pwd
LANG=C svn status

echo
read -p "Do you really want to merge this (y/n)? " desc

if [ "$desc" = "y" ]; then
  if [ "$COMMENT" != "" ] ; then
    LOG="`tempfile`"
    echo "$COMMENT" > $LOG
    LANG=C svn ci -F $LOG
    rm $LOG
  else
    LANG=C svn ci -m '- MFT'
  fi
fi
