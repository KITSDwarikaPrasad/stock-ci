#!/bin/bash

function usage
{
echo '''usage: sh poll_the_file.sh --Args
 Args : 
  -p --path : Path to the directory for polling
  -f --file : Path to the file for polling
  -i --interval : Interval/gap  in seconds for between each poll_the_file
  -d --duration : total duration in seconds till which polling to be done
  -h --help'''
}


if ["$1" = ""]; then
        usage
        exit 1
fi

while [ "$1" != "" ]; do
    case $1 in
        -p | --path  )          shift
                                directory=$1
                                ;;
        -f | --file )           shift
                                filename=$1
                                ;;
        -i | --interval )       shift
                                interval=$1
                                ;;
        -d | --duration )       shift
                                duration=$1
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
