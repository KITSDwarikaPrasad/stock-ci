#!/bin/bash

function usage
{
echo '''usage: sh csvsort.sh --Args
 Args : 
  -s --source : Source file for sorting
  -d --dest : Destination file for sorting
  -h --help'''
}


if ["$1" = ""]; then
        usage
        exit 1
fi

while [ "$1" != "" ]; do
    case $1 in
        -s | --source  )        shift
                                source=$1
                                ;;
        -d | --dest )           shift
                                destination=$1
                                ;;
        -h | --help )           usage
                                exit
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

sort -t',' $source -o $destination

sleep 180