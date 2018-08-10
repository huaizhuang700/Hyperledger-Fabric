# Hyperledger Fabric Root Ca Config

## 运行根Ca服务器
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

## 配置节点生成的证书目录，用于存放节点的管理员和节点证书
```linux
cd /data/
```
