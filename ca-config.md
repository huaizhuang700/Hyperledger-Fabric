# Hyperledger Fabric Root Config

*如果在ubunt操作系统中，只需安装：*
```
apt install libltdl3-dev
```
*如果在centos操作系统中，只需安装：*
```
 yum install libtool-ltdl-devel*
```

## 根Ca服务器
_建议对于根Ca的处理，只用于颁发orderer和peer的证书，我在处理的时候是颁发的orderer证书，颁发peer证书的话同理_
* 进入自己的根Ca目录，比如：
```
cd /data/root-ca-admin
```
  * 在命令行运行根Ca服务器，命令行会显示Ca的信息。
```
fabric-ca-server start -b admin:pass --cfg.affiliations.allowremove  --cfg.identities.allowremove &
```
  * 在后台运行根Ca服务器，命令行不会显示Ca的信息，但是在启动目录会有日志文件。根据日志文件可以查看到信息。 
```
nohup fabric-ca-server start -b admin:pass --cfg.affiliations.allowremove  --cfg.identities.allowremove & 2>&1 &
```

* 进入数据存放目录（可根据自行配置目录）
```
cd /data/
```
* 创建存放证书目录
```
mkdir -p `pwd`/fabric-ca-files/admin
```
* 登记根证书管理的管理员(-H 命令指定存放目录)
```
fabric-ca-client enroll -u http://admin:pass@localhost:7054 -H `pwd`/fabric-ca-files/admin
```
  * 查看组织关系命令
```
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation list
```
  * 删除组织关系命令
```
fabric-ca-client -H `pwd`/fabric-ca-files/admin  affiliation remove --force  org1
```
  * 添加组织关系命令,比如：
```
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation add io
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation add io.wheel
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation add io.wheel.org1
```
* 创建msp的文件存放目录
```
mkdir -p ./fabric-ca-files/wheel.io/msp
```
* 形成Msp证书链
```
fabric-ca-client getcacert -M `pwd`/fabric-ca-files/wheel.io/msp
```
* 修改orderer管理员配置文件
_将fabric-ca-files/admin/fabric-ca-client-config.yaml目录下的文件部分，修改为：_
```
id:
  name: Admin@wheel.io
  type: client
  affiliation: io.wheel
  maxenrollments: 0
  attributes:
    - name: hf.Registrar.Roles
      value: client,orderer,peer,user
    - name: hf.Registrar.DelegateRoles
      value: client,orderer,peer,user
    - name: hf.Registrar.Attributes
      value: "*"
    - name: hf.GenCRL
      value: true
    - name: hf.Revoker
      value: true
    - name: hf.AffiliationMgr
      value: true
    - name: hf.IntermediateCA
      value: true
    - name: role
      value: admin
      ecert: true
```
* 注册orderer节点的管理员证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/admin --id.secret=password
```
* 创建orderer的管理员目录文件
```
mkdir -p ./fabric-ca-files/wheel.io/admin
```
* 登记证书文件
```
fabric-ca-client enroll -u http://Admin@wheel.io:password@localhost:7054  -H `pwd`/fabric-ca-files/wheel.io/admin
```
* 修改orderer0的配置文件
_修改fabric-ca-files/wheel.io/admin/fabric-ca-client-config.yaml:_
```
id:
  name: orderer0.wheel.io
  type: orderer
  affiliation: io.wheel
  maxenrollments: 0
  attributes:
    - name: role
      value: orderer
      ecert: true
```
* 注册orderer0管理员的证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/wheel.io/admin --id.secret=password
```
* 创建orderer0的证书文件目录
```
mkdir ./fabric-ca-files/wheel.io/orderer0
```
* 登记orderer0的管理员证书
```
fabric-ca-client enroll -u http://orderer0.wheel.io:password@localhost:7054 -H `pwd`/fabric-ca-files/wheel.io/orderer0
```

_如果做orderer集群的化，那么就得颁发orderer2的证书_
* 修改orderer1的配置文件
_修改orderer1的配置文件fabric-ca-files/wheel.io/admin/fabric-ca-client-config.yaml_
```
id:
  name: orderer1.wheel.io
  type: orderer
  affiliation: io.wheel
  maxenrollments: 0
  attributes:
    - name: role
      value: orderer
      ecert: true
```
* 注册orderer1的证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/wheel.io/admin --id.secret=password
```
* 创建orderer1的证书文件目录
```
mkdir ./fabric-ca-files/wheel.io/orderer1
```
* 登记orderer1的证书
```
fabric-ca-client enroll -u http://orderer1.wheel.io:password@localhost:7054 -H `pwd`/fabric-ca-files/wheel.io/orderer1
```

_接下来就是，替换对应的证书文件夹下的证书文件即可_


## 中间Ca服务器

##### org1的证书颁发

* 进入自己的根Ca目录，比如：
```
cd /data/root-ca-admin
```
  * 在命令行运行根Ca服务器，命令行会显示Ca的信息。
```
fabric-ca-server start -b admin:pass -u http://admin:pass@192.168.1.209:7054 --cfg.affiliations.allowremove  --cfg.identities.allowremove &
```
  * 在后台运行根Ca服务器，命令行不会显示Ca的信息，但是在启动目录会有日志文件。根据日志文件可以查看到信息。 
```
nohup fabric-ca-server start -b admin:pass -u http://admin:pass@192.168.1.209:7054 --cfg.affiliations.allowremove  --cfg.identities.allowremove & 2>&1 &
```

* 进入数据存放目录（可根据自行配置目录）
```
cd /data/
```
* 创建存放证书目录
```
mkdir -p `pwd`/fabric-ca-files/admin
```
* 登记根证书管理的管理员(-H 命令指定存放目录)
```
fabric-ca-client enroll -u http://admin:pass@localhost:7054 -H `pwd`/fabric-ca-files/admin
```
  * 查看组织关系命令
```
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation list
```
  * 删除组织关系命令
```
fabric-ca-client -H `pwd`/fabric-ca-files/admin  affiliation remove --force  org1
```
  * 添加组织关系命令,比如：
```
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation add io
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation add io.wheel
fabric-ca-client  -H `pwd`/fabric-ca-files/admin  affiliation add io.wheel.org1
```
* 创建msp的文件存放目录
```
mkdir -p ./fabric-ca-files/wheel.io/msp
```
* 形成Msp证书链
```
fabric-ca-client getcacert -M `pwd`/fabric-ca-files/wheel.io/msp
```
* 修改org1的管理员配置文件
_修改fabric-ca-files/admin/fabric-ca-client-config.yaml其中的id部分修改为：_
```
id:
  name: Admin@org1.wheel.io
  type: client
  affiliation: io.wheel.org1
  maxenrollments: 0
  attributes:
    - name: hf.Registrar.Roles
      value: client,orderer,peer,user
    - name: hf.Registrar.DelegateRoles
      value: client,orderer,peer,user
    - name: hf.Registrar.Attributes
      value: "*"
    - name: hf.GenCRL
      value: true
    - name: hf.Revoker
      value: true
    - name: hf.AffiliationMgr
      value: true
    - name: hf.IntermediateCA
      value: true
    - name: role
      value: admin
      ecert: true
```
* 注册org的管理员证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/admin --id.secret=password
```
* 登记org的管理员证书
```
fabric-ca-client enroll -u http://Admin@org1.wheel.io:password@localhost:7054  -H `pwd`/fabric-ca-files/peers/org1.wheel.io/admin
```
* 修改peer0.org1的配置文件
_修改fabric-ca-files/peers/org1.wheel.io/admin/fabric-ca-client-config.yaml_
```
id:
  name: peer0.org1.wheel.io
  type: peer
  affiliation: io.wheel.org1
  maxenrollments: 0
  attributes:
    - name: role
      value: peer
      ecert: true
```
* 注册org1.peer0证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/peers/org1.wheel.io/admin --id.secret=password
```
* 创建org1.peer0证书目录
```
mkdir ./fabric-ca-files/peers/org1.wheel.io/peer0
```
* 颁发org1.peer0的证书
```
fabric-ca-client enroll -u http://peer0.org1.wheel.io:password@localhost:7054 -H `pwd`/fabric-ca-files/peers/org1.wheel.io/peer0
```
* 修改peer1.org1的配置文件
_修改fabric-ca-files/peers/org1.wheel.io/admin/fabric-ca-client-config.yaml:_
```
id:
  name: peer1.org1.wheel.io
  type: peer
  affiliation: io.wheel.org1
  maxenrollments: 0
  attributes:
    - name: role
      value: peer
      ecert: true
```
* 注册org1.peer1的证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/peers/org1.wheel.io/admin --id.secret=password
```
* 创建org1.peer1的证书目录
```
mkdir ./fabric-ca-files/peers/org1.wheel.io/peer1
```
* 登记org1.peer1的证书
```
fabric-ca-client enroll -u http://peer1.org1.wheel.io:password@localhost:7054 -H `pwd`/fabric-ca-files/peers/org1.wheel.io/peer1
```

##### org2的证书颁发

* 创建org2的证书文件目录
```
mkdir -p fabric-ca-files/peers/org2.wheel.io/msp
```
* 获取org2的msp证书链
```
fabric-ca-client getcacert -M `pwd`/fabric-ca-files/peers/org2.wheel.io/msp
```
* 创建org2的管理员
_修改fabric-ca-files/admin/fabric-ca-client-config.yaml其中的id部分修改为：_
```
id:
  name: Admin@org2.wheel.io
  type: client
  affiliation: io.wheel.org2
  maxenrollments: 0
  attributes:
    - name: hf.Registrar.Roles
      value: client,orderer,peer,user
    - name: hf.Registrar.DelegateRoles
      value: client,orderer,peer,user
    - name: hf.Registrar.Attributes
      value: "*"
    - name: hf.GenCRL
      value: true
    - name: hf.Revoker
      value: true
    - name: hf.AffiliationMgr
      value: true
    - name: hf.IntermediateCA
      value: true
    - name: role
      value: admin
      ecert: true
```
* 注册org2的管理员证书
```
fabric-ca-client register -H `pwd`/fabric-ca-files/admin --id.secret=password
```
* 登记org2的管理员证书
```
fabric-ca-client enroll -u http://Admin@org2.wheel.io:password@localhost:7054  -H `pwd`/fabric-ca-files/peers/org2.wheel.io/admin
```
* 修改peer0.org2的证书配置文件
_改fabric-ca-files/peers/org2.wheel.io/admin/fabric-ca-client-config.yaml:_
```
id:
  name: peer0.org2.wheel.io
  type: peer
  affiliation: io.wheel.org2
  maxenrollments: 0
  attributes:
    - name: role
      value: peer
      ecert: true
```
* 注册peer0.org2的证书配置文件
```
fabric-ca-client register -H `pwd`/fabric-ca-files/peers/org2.wheel.io/admin --id.secret=password
```
* 创建peer0.org2的证书存放目录
```
mkdir ./fabric-ca-files/peers/org2.wheel.io/peer0
```
* 登记peer0.org2的证书
```
fabric-ca-client enroll -u http://peer0.org2.wheel.io:password@localhost:7054 -H `pwd`/fabric-ca-files/peers/org2.wheel.io/peer0
```

* 修改peer1.org2的证书配置文件
_修改fabric-ca-files/peers/org2.wheel.io/admin/fabric-ca-client-config.yaml:_
```
id:
  name: peer1.org2.wheel.io
  type: peer
  affiliation: io.wheel.org2
  maxenrollments: 0
  attributes:
    - name: role
      value: peer
      ecert: true
```
* 注册peer1.org2的证书配置文件
```
fabric-ca-client register -H `pwd`/fabric-ca-files/peers/org2.wheel.io/admin --id.secret=password
```
* 创建peer0.org2的证书存放目录
```
mkdir ./fabric-ca-files/peers/org2.wheel.io/peer1
```
* 登记peer0.org2的证书
```
fabric-ca-client enroll -u http://peer1.org2.wheel.io:password@localhost:7054 -H `pwd`/fabric-ca-files/peers/org2.wheel.io/peer1
```

*替换相应的证书文件，启动网络*
