#!/usr/bin/sh

# assume we are in a standard UNIX environment...

UCL=$(find ~/ -type f -name 'ucl.log')

if [ -e "$UCL" ]; then
    cut -d' ' -f2- "$UCL" | tr -sc '[:alpha]' '\n' | tr '[:upper:]' '[:lower:]' \
        | awk -- 'first!=""&&second!="" {print first, second} {first=second; second=$0;}' | sort \
        | uniq -c | sort -nr > ucl_bigrams
fi
