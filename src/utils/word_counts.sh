#!/usr/bin/sh

# assume that the standard utilities are installed!

UCL=$(find ~/ -type f -name "ucl.log")

if [ -e "$UCL" ]; then
    cut -d' ' -f2- "$UCL" | tr -sc '[:alpha:]' '\n' | tr '[:upper:]' '[:lower:]' | sort | uniq -c |
    sort -n -r > term_frequencies
else
    echo "unable to find raw datafile"
fi
