
# 搭建Nacos环境

官方地址： https://nacos.io/zh-cn/docs/quick-start-spring-boot.html

## 1.将配件文件
`https://github.com/alibaba/nacos/blob/master/config/src/main/resources/META-INF/nacos-db.sql`
导入Mysql环境

## 2.docker创建Nacos环境
```
docker run -d \
-e MODE=standalone \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_MASTER_SERVICE_HOST=数据库ip \
-e MYSQL_MASTER_SERVICE_PORT=数据库端口 \
-e MYSQL_MASTER_SERVICE_USER=用户名 \
-e MYSQL_MASTER_SERVICE_PASSWORD=密码 \
-e MYSQL_MASTER_SERVICE_DB_NAME=对应的数据库名 \
-e MYSQL_SLAVE_SERVICE_HOST=从数据库ip \
-p 8848:8848 \
--name nacos-sa-mysql \
--restart=always \
nacos/nacos-server
```

## 参数说明：
<table>
<thead>
<tr>
<th>参数名</th>
<th>描述</th>
<th>可选项</th>
<th>默认值</th>
</tr>
</thead>
<tbody>
<tr>
<td>MODE</td>
<td>cluster模式/standalone模式</td>
<td>cluster/standalone</td>
<td>cluster</td>
</tr>
<tr>
<td>NACOS_SERVERS</td>
<td>nacos cluster地址</td>
<td>eg. ip1,ip2,ip3</td>
<td></td>
</tr>
<tr>
<td>PREFER_HOST_MODE</td>
<td>是否支持hostname</td>
<td>hostname/ip</td>
<td>ip</td>
</tr>
<tr>
<td>NACOS_SERVER_PORT</td>
<td>nacos服务器端口</td>
<td></td>
<td>8848</td>
</tr>
<tr>
<td>NACOS_SERVER_IP</td>
<td>多网卡下的自定义nacos服务器IP</td>
<td></td>
<td></td>
</tr>
<tr>
<td>SPRING_DATASOURCE_PLATFORM</td>
<td>standalone 支持 mysql</td>
<td>mysql/empty</td>
<td>empty</td>
</tr>
<tr>
<td>MYSQL_MASTER_SERVICE_HOST</td>
<td>mysql 主节点host</td>
<td></td>
<td></td>
</tr>
<tr>
<td>MYSQL_MASTER_SERVICE_PORT</td>
<td>mysql 主节点端口</td>
<td></td>
<td>3306</td>
</tr>
<tr>
<td>MYSQL_MASTER_SERVICE_DB_NAME</td>
<td>mysql 主节点数据库</td>
<td></td>
<td></td>
</tr>
<tr>
<td>MYSQL_MASTER_SERVICE_USER</td>
<td>数据库用户名</td>
<td></td>
<td></td>
</tr>
<tr>
<td>MYSQL_MASTER_SERVICE_PASSWORD</td>
<td>数据库密码</td>
<td></td>
<td></td>
</tr>
<tr>
<td>MYSQL_SLAVE_SERVICE_HOST</td>
<td>mysql从节点host</td>
<td></td>
<td></td>
</tr>
<tr>
<td>MYSQL_SLAVE_SERVICE_PORT</td>
<td>mysql从节点端口</td>
<td></td>
<td>3306</td>
</tr>
<tr>
<td>MYSQL_DATABASE_NUM</td>
<td>数据库数量</td>
<td></td>
<td>2</td>
</tr>
<tr>
<td>JVM_XMS</td>
<td>-Xms</td>
<td></td>
<td>2g</td>
</tr>
<tr>
<td>JVM_XMX</td>
<td>-Xmx</td>
<td></td>
<td>2g</td>
</tr>
<tr>
<td>JVM_XMN</td>
<td>-Xmn</td>
<td></td>
<td>1g</td>
</tr>
<tr>
<td>JVM_MS</td>
<td>-XX:MetaspaceSize</td>
<td></td>
<td>128m</td>
</tr>
<tr>
<td>JVM_MMS</td>
<td>-XX:MaxMetaspaceSize</td>
<td></td>
<td>320m</td>
</tr>
<tr>
<td>NACOS_DEBUG</td>
<td>开启远程调试</td>
<td>y/n</td>
<td>n</td>
</tr>
<tr>
<td>TOMCAT_ACCESSLOG_ENABLED</td>
<td>server.tomcat.accesslog.enabled</td>
<td></td>
<td>false</td>
</tr>
</tbody>
</table>

## 3.配置
访问http://ip:8848
账号：nacos 密码nacos

登陆后 ： 配置列表--> + (新增)
```
Data ID: ZUUL_DATA
Group:   ZUUL_GATEWAY
配置格式: JSON
配置内容： [
              {
                  "enabled":true,
                  "id":"test",
                  "path":"/myTest/**",
                  "retryable":false,
                  "stripPrefix":true,
                  "url":"http://192.168.0.41:8988"
              }
          ]

```
注：Nacos配置中心新建对应dataId,groupId的路由配置json文件



# 主要文件说明
```
NacosDynamicRouteListener @NacosConfigListener --监听远程Nacos 路由配置更新
NacosDynamicRouteLocator implements RefreshableRouteLocator -- 实现路由可刷新定位器
RoutesRefreshedEvent -- zuul 路由更新事件，publish后实现zuul更新路由
ZuulRefreshListener   onApplicationEvent -- 可以监听到更新事件publish信息  
MyZuulFilter  -- 自定义网关路由器，根据自身需求定制化
```
