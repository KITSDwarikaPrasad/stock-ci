#!/bin/bash

function usage
{
echo '''usage: sh csvsort.sh --Args
 Args : 
  -s --source : source file for sorting
  -d --dest : destination file for sorting
  -h --help'''
}


if ["$1" = ""]; then
        usage
        exit 1
fi

while [ "$1" != "" ]; do
    case $1 in
        -s | --source  )          shift
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


((end_time=${SECONDS}+$duration))

while ((${SECONDS} < ${end_time}))
do
  echo "checking for the file.."
  if [[ -r ${directory}/${filename} ]]
  then
    echo "File has arrived."
    exit 0
  fi
  sleep ${interval}
done

echo "File did not arrive."
exit 1
