#!/bin/bash

sh ~/.bashrc
cd ../../src/main/ansible
pwd
#ansible-playbook -i hosts/staging jenkins_play.yaml --vault-password-file=~/doNotDelete/.vault_pass
ansible-playbook -i hosts/staging bods_play.yaml -e "source=SAPR3toStockAPI.csv destination=SAPR3toStockAPI.csv_Actual chdirTo=/BODSSHARE/UKBQ/DSOUT/StockService/" -vvvv
#python --version
#ansible --version
#echo $PATH
#ls -ltr