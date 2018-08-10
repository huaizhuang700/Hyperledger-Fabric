# fabric cli command


```
 > 进入docker控制台
docker exec -it cli bash

> 指定ORDERER的证书文件
ORDERER_CA=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/wheel.io/orderers/orderer0.wheel.io/msp/tlscacerts/tlsca.wheel.io-cert.pem

> 生成channel的配置区块
peer channel create -o orderer0.wheel.io:7050 -c mychannel -f ./channel-artifacts/channel.tx --tls true --cafile $ORDERER_CA

> 锚节点的更新
peer channel update -o orderer0.wheel.io:7050 -c mychannel -f ./channel-artifacts/Org1MSPanchors.tx --tls true --cafile $ORDERER_CA   更新锚节点

> 指定cli的连接对象
CORE_PEER_LOCALMSPID="Org2MSP"
CORE_PEER_TLS_ROOTCERT_FILE=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.wheel.io/peers/peer0.org2.wheel.io/tls/ca.crt
CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org2.wheel.io/users/Admin@org2.wheel.io/msp
CORE_PEER_ADDRESS=peer0.org2.wheel.io:7051

> 加入channel
peer channel join -b mychannel.block

> 获取配置文件
peer channel fetch oldest mychannel.block -c mychannel -o orderer0.wheel.io:7050 --tls --cafile $ORDERER_CA

> 安装链码
peer chaincode install -n myc -v 1.0 -p github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02

> 实例化链码
peer chaincode instantiate -o orderer0.wheel.io:7050 --tls true --cafile $ORDERER_CA -C mychannel -n smartchaincode -v 1.0 -c '{"Args":["init","a","100","b","200"]}' -P "AND('Org1MSP.member','Org2MSP.member')"

> 更新链码
peer chaincode upgrade -o orderer0.wheel.io:7050 --tls true --cafile $ORDERER_CA -C mychannel -n smartchaincode -v 9.3 -c '{"Args":["init"]}' -P "AND('Org1MSP.member','Org2MSP.member')"

> 操作链码
peer chaincode invoke -o orderer0.wheel.io:7050  --tls true --cafile $ORDERER_CA -C mychannel -n myccc -c '{"Args":["invoke","a","b","10"]}'
```
