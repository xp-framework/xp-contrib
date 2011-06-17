#!/bin/sh
#
# $Id: cmd.tag.sh 12492 2010-08-02 07:56:50Z olli $
#

recursiveCreateEmpty() {
  local DIR=$1
  
  if [ ! -d $(dirname $DIR) ]; then
    recursiveCreateEmpty $(dirname $DIR);
  fi
  
  LANG=C svn mkdir $DIR
  return;
}

fetchFileURL() {
  local REPOBASE=$1
  local RELTARGET=$2
  
  # FIXME: This should not hardcode /trunk/ - but how do we find
  # out which branch we're on?
  echo $(repositoryRoot $REPOBASE)/trunk/$RELTARGET
}

recursiveCopy() {
  local SOURCEBASE=$1
  local TARGETBASE=$2
  local ITEM=$3

  [ $DEBUG ] && echo "---> Checking $ITEM"
  
  # Check for specific revision
  [ $ONLY_REVISION ] && REV="-r $ONLY_REVISION"

  if [ ! -e "$TARGETBASE"/$ITEM ]; then
  
    # New, so "svn copy" from repository (URL -> WC)
    [ $DEBUG ] && echo "svn copy "$REPOBASE"/$ITEM "$TARGETBASE"/$ITEM";
    [ "yes" = $ONLY_EXISTING ] && {
      echo "Skipping $ITEM, because it does not exist in tag";
      return;
    }
    
    PARENT_DIR=$(dirname "$TARGETBASE"/$ITEM);
    [ "no" = $CREATE_EMPTY_DIR ] && [ ! -d "$PARENT_DIR" ] && {
      echo "Skipping $ITEM, because the parent does not exist";
      echo "You can change this behaviour with the -c switch!";
      return;
    }
    
    # Create empty parent directory
    [ ! -d "$PARENT_DIR" ] && {
      recursiveCreateEmpty "$PARENT_DIR"
    }
    
    # If source and target are in the same repository, svn copy the file
    if [ "$REPOURL" = $(repositoryRoot "$SOURCEBASE") ]; then
      [ $DEBUG ] && echo "svn copy $REV $(fetchFileURL "$REPOBASE" "$ITEM") "$TARGETBASE"/$ITEM";
      LANG=C svn copy $REV $(fetchFileURL "$REPOBASE" "$ITEM") "$TARGETBASE"/$ITEM;
    
    # otherwise svn export it into the target
    else
      [ $DEBUG ] && echo "svn export $REV $(fetchFileURL "$REPOBASE" "$ITEM") "$TARGETBASE"/$ITEM";
      LANG=C svn export $REV $(fetchFileURL "$REPOBASE" "$ITEM") "$TARGETBASE"/$ITEM;
      LANG=C svn add "$TARGETBASE"/$ITEM;
    fi
    return;
  fi
  
  if [ -f "$TARGETBASE"/$ITEM -a ! -L "$TARGETBASE"/$ITEM ]; then
  
    # Update by `svn cat`, which print the file without local modifications
    if [ "$REV" != "" ] ; then
      [ $DEBUG ] && echo "svn merge $REV $(fetchFileURL "$REPOBASE" "$ITEM") $TARGETBASE/$ITEM"
      LANG=C svn merge $REV $(fetchFileURL "$REPOBASE" "$ITEM") "$TARGETBASE/$ITEM"
    
    # Merge differences between revisions into working copy
    else
      [ $DEBUG ] && echo "svn cat $(fetchFileURL "$REPOBASE" "$ITEM") > $TARGETBASE/$ITEM"
      LANG=C svn cat $(fetchFileURL "$REPOBASE" "$ITEM") > "$TARGETBASE/$ITEM"
    fi
    return;
  fi
  
  if [ -d "$TARGETBASE"/$ITEM ]; then
  
    # Recursively copy directory
    for i in $(ls -1 "$SOURCEBASE"/$ITEM); do
      recursiveCopy "$SOURCEBASE" "$TARGETBASE" ""$ITEM"/$i";
    done
  fi
}

# Parse command line options
ONLY_EXISTING="no"
CREATE_EMPTY_DIR="no"
ONLY_REVISION=""
while getopts 'ucr:' COMMAND_LINE_ARGUMENT ; do
  case "$COMMAND_LINE_ARGUMENT" in
    u)  ONLY_EXISTING="yes"     ;;
    c)  CREATE_EMPTY_DIR="yes"  ;; 
    r)  ONLY_REVISION="$OPTARG" ;;
    ?)  exit
  esac
done
shift $(($OPTIND - 1))

# Assert we have an active tag checked out
assertHaveActiveTag

while [ ! -z $1 ]; do
  TARGET=$(fetchTarget $1)
  [ -z "$TARGET" ] && exit 1

  RELTARGET=$(relativeTarget "$TARGET")
  recursiveCopy "$REPOBASE" "$(tmpTagDir)"/current-tag $RELTARGET
  
  shift 1
done

cd "$(tmpTagDir)"/current-tag && svn status
