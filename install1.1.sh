#########################################################################
# File Name: install_fabric1.1.sh
# Author: Liberty
# mail: cry_dear@yeah.net
# Created Time: 2018年05月16日 星期三 10时33分28秒
#########################################################################

#!/bin/bash

clear

BASH_PATH=$(cd `dirname $0`; pwd)
# INSTALL_PATH=/usr/local
INSTALL_PATH=/data

echo -e "\033[32m make sure file path------->$BASH_PATH"
echo -e "\033[32m ......................................."

INSTALL_SURE=N

echo -e "\033[32m if you want to install the HYperledger Fabric1.1."
echo -e "\033[32m please input the keyboard 'Y'."
echo -e "\033[32m else please input any things."
echo -e -n "\033[36m please input:"

read INSTALL_SURE

if [[ "$INSTALL_SURE" != "Y" ]]; then
	echo -e "\033[31m stop install.................................."
	exit 1
fi
		
echo -e "\033[32m start install....................................."
		
cd $BASH_PATH
		
which go
		
if [ $? -eq 0 ]; then
	echo -e "\033[31m The Golang have installed."
	exit 1
fi
				
tar xzvf ./go1.9.5.linux-amd64.tar.gz -C $INSTALL_PATH/
				
GOROOT=$INSTALL_PATH/go
mkdir -p $INSTALL_PATH/GOPATH
GOPATH=$INSTALL_PATH/GOPATH
				
echo -e "\033[32m GOROOT PATH:--------->$GOROOT"
echo -e "\033[32m GOPATH PATH:--------->$GOPATH"
				
echo "The Golang environment variables not exist, starting define it."
echo "export GOROOT="$GOROOT>>/etc/profile
echo "export GOPATH="$GOPATH>>/etc/profile
echo "export PATH=\$GOROOT/bin:\$GOPATH/bin:\$PATH">>/etc/profile
echo "The Golang environment  variables finished."

. /etc/profile

source /etc/profile
				
cp $BASH_PATH/CentOS7-Base-163.repo  /etc/yum.repos.d/
				
cp $BASH_PATH/docker-ce.repo  /etc/yum.repos.d/
				
yum makecache
				
yum -y update
				
yum -y install git


# tar -xzvf git-2.9.2.tar.gz -C ../

# cd ../git-2.9.2

# GIT_HOME=$(cd `dirname $0`; pwd)

# echo "export GIT_HOME="$GIT_HOME>>/etc/profile
# echo "export PATH=\$GIT_HOME/bin:\$PATH">>/etc/profile

# . /etc/profile

# source /etc/profile

which docker
				
if [ $? -eq 0 ]; then
	echo "The docker have installed, will quit..."
	exit 1
fi
						
KERNEL_VERSION=`uname -r | awk -F "." '{print $1}'`
						
echo "\033[36m Linux kernel version: $KERNEL_VERSION"
						
if [ $KERNEL_VERSION -lt 3 ]; then
	echo "\033[31m linux kernel too low for docker.will quit..."
	exit 1
fi
								
yum install -y yum-utils device-mapper-persistent-data lvm2
								
yum install -y docker-ce-18.03.1.ce
# yum install –-installroot=/data -y docker-ce-18.03.1.ce
								
systemctl start docker

mv $BASH_PATH/docker.service /usr/lib/systemd/system/

systemctl daemon-reload

systemctl  restart docker.service
								
systemctl enable docker
								
mv $BASH_PATH/docker-compose /usr/local/bin
								
chmod +x /usr/local/bin/docker-compose
								
docker-compose -version
							
if [ $? -ne 0 ]; then
	echo -e "\033[31m docker-compose install failed."
	exit 1
fi
								
echo -e "\033[36m ..................finish........................"
								
cd $GOPATH
								
mkdir -p src/github.com/hyperledger
								
cd ./src/github.com/hyperledger
								
# git clone https://github.com/hyperledger/fabric.git
								
# git checkout release-1.1

tar -xzvf $BASH_PATH/fabric-release-1.1.tar.gz -C ./

mv fabric-release-1.1 fabric

git clone https://github.com/hyperledger/fabric-samples.git
								
git checkout release-1.1

cd fabric-samples
					
tar -xzvf $BASH_PATH/hyperledger-fabric-linux-amd64-1.1.0.tar.gz -C ./

cd bin

FABRIC_BIN=$(cd `dirname $0`; pwd)

echo "export FABRIC_BIN="$FABRIC_BIN>>/etc/profile

echo "export PATH=\$FABRIC_BIN:\$PATH">>/etc/profile

. /etc/profile

source /etc/profile

cd  $BASH_PATH
tar -xzvf images.tar.gz -C ./
								
cd images

ls *.tar>ls.log

for i in $(cat ls.log)
do
	docker load < ./$i
done

while :
do
	docker images>image_num.log
	IMAGE_NUM=`cat image_num.log|wc -l`
	if [ $IMAGE_NUM -gt 18 ];then
		echo -e "\033[33m images load finish"
		break
	fi
done

rm -rf image_num.log

cd ..

rm -rf images

reboot
