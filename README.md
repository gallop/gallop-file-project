



minio中文文档：http://docs.minio.org.cn/docs/

> 注意： 此demo 用到了webix ui 组件，需要购买授权才能用于商用，这里仅是学习之用!!



### MinIO docker 安装

MinIO自定义Access和Secret密钥

要覆盖MinIO的自动生成的密钥，您可以将Access和Secret密钥设为环境变量。 MinIO允许常规字符串作为Access和Secret密钥。

 -e TZ=Asia/Shanghai \

```sh
docker run -p 9000:9000 -p 9001:9001 -idt --name myminio --privileged=true \
  -e "MINIO_ROOT_USER=minioTest" \
  -e "MINIO_ROOT_PASSWORD=12345678" \
  -v /etc/localtime:/etc/localtime:ro \
  -v /home/minio/data:/data \
  -v /home/minio/config:/root/.minio \
  minio/minio:RELEASE.2021-07-08T01-15-01Z server /data \
  --console-address ":9001" --address ":9000"
```

> -v /etc/localtime:/etc/localtime:ro \  # 用户同步宿主机和容器的时区，保持宿主机和同期时间一致



启动日志：

````
API: http://172.17.0.2:9000  http://127.0.0.1:9000 
RootUser: minioTest 
RootPass: 12345678 

Console: http://172.17.0.2:9001 http://127.0.0.1:9001 
RootUser: minioTest 
RootPass: 12345678 

Command-line: https://docs.min.io/docs/minio-client-quickstart-guide
   $ mc alias set myminio http://172.17.0.2:9000 minioTest 12345678

````



启动后 访问 http://192.168.0.118:9000/ 就可进入minio 后台的管理界面



同步宿主机和容器的时区：

方式1、在宿主机执行命令如下：

```
 docker cp -L /usr/share/zoneinfo/Asia/Shanghai  【容器名】:/etc/localtime
 例如：
 [root@localhost ~]# docker cp -L /usr/share/zoneinfo/Asia/Shanghai  myminio:/etc/localtime
```

方式2、使用docker run运行容器时，添加如下参数

 

```
-v /etc/localtime:/etc/localtime:ro
```



查看 linux 系统版本：

/etc/centos-release

cat /etc/redhat-release



````
docker run -p 9000:9000 -p 9001:9001 -idt --name myminio --privileged=true \
  -e "MINIO_ROOT_USER=minioTest" \
  -e "MINIO_ROOT_PASSWORD=12345678" \
  -v /etc/localtime:/etc/localtime:ro \
  -v /home/minio/data:/data \
  -v /home/minio/config:/root/.minio \
  minio/minio:RELEASE.2021-07-08T01-15-01Z server /data \
  --console-address ":9001" --address ":9000"
````



centos 时钟同步：

先查看ntp是否已经安装：rpm -q ntp

如果没有安装则使用命令：yum -y install ntp 安装ntp



安装完成后设置ntp开机启动并启动ntp，如下：

systemctl enable ntpd

systemctl start ntpd



编辑配置文件（vim /etc/ntp.conf），注释默认ntp服务地址，使用国内地址

````
server 0.cn.pool.ntp.org
server 1.cn.pool.ntp.org
server 2.cn.pool.ntp.org
server 3.cn.pool.ntp.org

restrict 0.cn.pool.ntp.org nomodify notrap noquery
restrict 1.cn.pool.ntp.org nomodify notrap noquery
restrict 2.cn.pool.ntp.org nomodify notrap noquery
restrict 3.cn.pool.ntp.org nomodify notrap noquery

server 127.0.0.1 # local clock
fudge 127.0.0.1 stratum 10
````

修改完成后保存退出，并重启ntp（systemctl restart ntpd）。



