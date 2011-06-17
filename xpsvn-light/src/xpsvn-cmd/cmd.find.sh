#!/bin/sh
#
# $Id: cmd.find.sh 12310 2010-06-25 11:22:08Z kiesel $
#


# Initially parse command line to find global options
while getopts 'v' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    v)  VERBOSE="yes"  ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

RETCODE=1
REPOROOT=$(repositoryRoot "$REPOBASE")

while [ ! -z $1 ]; do
  TARGET=$1
  if [ -z $TARGET ]; then
    TARGET=".";
  fi

  TARGET=$(fetchTarget "$TARGET")
  [ -z "$TARGET" ] && exit 1;

  RELTARGET=$(relativeTarget "$TARGET")
  [ -z "$RELTARGET" ] && exit 1;

  [ $VERBOSE ] && echo "Searching file $RELTARGET..."

  for i in `svn ls "$REPOROOT"/tags/ | tr '/' ' '`; do
    LANG=C svn ls "$REPOROOT/tags/$i/$RELTARGET" 1>/dev/null 2>/dev/null
    if [ $? -eq 0 ]; then
      rev=$(fetchFileRevision "$REPOROOT"/tags/$i/$RELTARGET)
      echo "$i (revision $rev)";
      RETCODE=0
    fi
  done
  
  shift 1
done

exit $RETCODE
