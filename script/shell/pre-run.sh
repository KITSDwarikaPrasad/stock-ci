#!/bin/sh
#cp -f -v /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv ~/SAPR3toStockAPI.csv
#echo 'fetching 1000 line from remote file to SAPR3toStockAPI_1000.csv'
#sed -n '1,500p' /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv > ~/SAPR3toStockAPI_1000.csv
#mv ~/SAPR3toStockAPI_1000.csv? ~/SAPR3toStockAPI_1000.csv
#tr -d '[\000-\010\014\016-\037\177]' < ~/SAPR3toStockAPI_1000.csv? > ~/SAPR3toStockAPI_1000.csv
#echo 'fetching 1010 line from remote file to SAPR3toStockAPI_1010.csv'
#sed -n '1,400p' /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv > ~/SAPR3toStockAPI_1010.csv
#tr -d '[\000-\010\014\016-\037\177]' < ~/SAPR3toStockAPI_1010.csv? > ~/SAPR3toStockAPI_1010.csv
#mv ~/SAPR3toStockAPI_1010.csv? ~/SAPR3toStockAPI_1010.csv
#cp SAPR3toStockAPI.csv  SAPR3toStockAPI_bkp.csv
#ls -l
#sed -n '1000,1500p' /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv > ~/SAPR3toStockAPI_500.csv
echo starting sorting of csv file at $(date +"%x %r %Z")
sort -t ',' /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI.csv -o /BODSSHARE/UKBQ/DSOUT/StockService/SAPR3toStockAPI_sorted.csv
echo Sorting finished at $(date +"%x %r %Z")