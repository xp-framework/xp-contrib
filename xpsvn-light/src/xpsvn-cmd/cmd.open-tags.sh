#!/bin/sh
#
# $Id: cmd.open-tags.sh 11029 2009-04-28 13:34:57Z olli $
#

TAGDIR=$(tmpTagDir)

if [ ! -d "$TAGDIR" ]; then
  mkdir -p "$TAGDIR";
fi

[ -h "$TAGDIR"/current-tag ] || [ -d "$TAGDIR" ] || exit

ACTTAG=$(activeTag)
ls -1 "$TAGDIR" | grep -v current-tag | while read TAG; do
  if [ "$TAG" = "$ACTTAG" ]; then
    echo "$TAG (active)"
  else
    echo $TAG
  fi
done
