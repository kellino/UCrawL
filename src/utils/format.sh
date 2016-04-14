#!/usr/bin/sh

# unforunately, python2.7 doesn't easily allow for the stripping of the unicode symbol u'
# when writing to file (it is usually removed when printed to console however. Rather than
# trying to write a clever solution to this is python, much easier just to process the file
# afterwards

LINKS=$(find ~/ -type f -name "link_list")

if [ -e "$LINKS" ]; then
    sed -i 's/set(/= /g; s/u'//g; s/'//g; s/)//g' "$LINKS"
fi
