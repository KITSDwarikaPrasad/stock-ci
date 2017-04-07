#!/bin/bash

sh ~/.bashrc
cd ../../src/main/ansible
pwd
ansible-playbook -i hosts/staging bods_play.yaml -e "moduleName=win_shell command=JOB_SAPR3_MicroservicenMBODS_STOCK.bat chdirTo=D:\\BODSSHARE"
#python --version
#ansible --version
#echo $PATH
#ls -ltr
