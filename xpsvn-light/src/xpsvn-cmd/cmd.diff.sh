#!/bin/sh
#
# $Id: cmd.diff.sh 11284 2009-07-23 14:14:28Z kiesel $
#


# Parse command line options
while getopts 'vN' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    v)  DIFF_VERBOSE="yes"  ;;
    N)  DIFF_NEWFILES="yes" ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

assertHaveActiveTag

TAGDIR=$(tmpTagDir)/current-tag

while [ ! -z $1 ]; do

  TARGET=$(fetchTarget $1)
  [ -z $TARGET ] && exit 1

  RELTARGET=$(relativeTarget $TARGET)

  [ $VERBOSE ] && echo "Diffing $RELTARGET against $TAG"

  # First, check on existance
  if [ ! -e "$TAGDIR"/$RELTARGET ]; then
    echo "A      $RELTARGET";
    PARENT=$(dirname "$TAGDIR"/$RELTARGET)
    if [ ! -e $PARENT ]; then
      echo "!      $PARENT (nonexistant)";
    fi
  fi
  
  # If target is a single file, assume verbose diff
  if [ -f $TARGET ]; then
    DIFF_VERBOSE="yes";
  fi

  DIFF_OPTIONS='-urbB --exclude=.svn --exclude=CVS --exclude="*.#*"'
  if [ "yes" != "$DIFF_VERBOSE"  ]; then DIFF_OPTIONS="$DIFF_OPTIONS --brief"; fi
  if [ "yes"  = "$DIFF_NEWFILES" ]; then DIFF_OPTIONS="$DIFF_OPTIONS -N"; fi

  LANG=C diff -I '\$Id' -I '\$Revision' $DIFF_OPTIONS "$TAGDIR"/$RELTARGET "$REPOBASE"/$RELTARGET 2>&1 | \
    grep -v '^diff' | \
    ${SED} "s#^\(Only in .*\): #\1/#" | \
    ${SED} "s#^Only in "$TAGDIR/$RELTARGET"/#D      #g" | \
    ${SED} "s#^Only in "${REPOBASE}/$RELTARGET"/#A      #g" | \
    ${SED} -r "s#Files "$TAGDIR"/"$RELTARGET"/([^ ]+) and ([^ ]+) differ#M      \\1#g"
  shift 1
done
