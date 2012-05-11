#!/bin/sh
#
# $Id: common.inc.sh 12515 2010-08-18 13:18:56Z friebe $
#

repositoryBase () {
  local $BASE
  BASE=$(realpath .)
  
  if [ -d "$BASE/.svn" ]; then
  
    # Standard SVN mode
    while [ -d "$BASE/../.svn" ]; do
      if [ -d "$BASE/../.svn" ] ; then
        BASE=$(realpath "$BASE/..")
      fi
    done

    if [ ! -d "$BASE/.svn" ]; then
      echo "Could not determine repository base. Aborting.";
      exit 1;
    fi
  else
    BASE=$(repositoryBasePostSvn16)

    if [ ! -e "$BASE/.svn/entries" ]; then
      echo "Could not determine repository base (post-svn-1.6 mode). Aborting.";
      exit 1;
    fi
  fi
  
  echo $BASE
}

repositoryBasePostSvn16 () {
  local BASE
  BASE=$(realpath .)

  while [ "$BASE" != "/" ]; do
    if [ -e "$BASE/.svn/entries" ]; then
      break;
    fi

    BASE=$(realpath "$BASE/..")
  done

  echo $BASE;
}

tmpTagDir () {
  echo "$HOME/.xpsvn/tag"
}

repositoryRoot () {
  local REPO=$1

  if [ -d "$REPO/.svn" ]; then
    svn info --xml "$1" | grep '^<root>' | ${SED} -e 's/^<root>//g' -e 's/<\/root>$//g'
  fi
}

fetchTarget() {
  local TARGET=$1
  
  if [ -z $TARGET ]; then
    TARGET="."
  fi
  
  local REAL=$(realpath "$TARGET" 2>/dev/null)
  
  if [ ! -e "$REAL" ]; then
    echo "Invalid target specified: $TARGET" >&2;
    return 1;
  fi
  
  echo $REAL;
}

activeTag() {
  TAG="$(tmpTagDir)"/current-tag
  [ -L "$TAG" ] && echo $(basename $(realpath "$TAG"))
}

assertHaveActiveTag() {
  if [ "$(activeTag)" = "" ]; then
    echo "You do not have an active tag, check out with xpsvn open <tagname>. Aborting."
    exit 1;
  fi
}

fetchFileRevision() {
  local FILE=$1
  
  svn info --xml "$FILE" | grep revision | tail -n1 | cut -d '"' -f2
}

relativeTarget () {
  local TARGET=$1
  
  echo $TARGET | ${SED} -r "s#$REPOBASE/##"
}


# Find out suitable sed executable
# Tip for FreeBSD users: install /usr/ports/textproc/gsed
SED=$(which gsed sed 2>/dev/null | head -n 1)

# Initially parse command line to find global
# options
while getopts 'vdr:' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    v)  VERBOSE="yes";;
    d)  DEBUG="yes";;
    r)  REPOBASE=$(realpath $OPTARG);;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

[ -z "$REPOBASE" ] && REPOBASE=$(repositoryBase)
[ $? -ne 0 ] && {
  echo "!!! Repository base not found or not specified: '$REPOBASE'";
  exit 1;
}

[ -z "$REPOURL" ] && REPOURL=$(repositoryRoot "$REPOBASE")
[ -z "$REPOURL" ] && {
  echo "!!! Could not determine repository URL: '$REPOURL'";
  exit 1;
}

[ "$VERBOSE" = "yes" ] && { 
  echo "===> Global repository information:"
  echo "---> Repository base: $REPOBASE"
  echo "---> Repository url: $REPOURL"
  echo
}

# Reset options indicator for further scans
OPTIND=1

