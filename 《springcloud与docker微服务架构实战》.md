# 《springcloud与docker微服务架构实战》

-------------------
## 源码地址：
> **1-11章代码地址**
>> https://github.com/itmuch/spring-cloud-docker-microservice-book-code
>> http://git.oschina.net/itmuch/spring-cloud-docker-microservice-book-code

> **12-14章配套代码地址**：
>> https://github.com/itmuch/spring-cloud-docker-microservice-book-code-docker
>> http://git.oschina.net/itmuch/spring-cloud-docker-microservice-book-code-docker
## 书目录
* 详见： http://www.itmuch.com/advertisment/my-spring-book/
## 笔记地址
* JAVA>>纯洁的微笑>>17节

## 复制模块：
* F5
* iml文件重命名
* pom3处重命名
* package重命名+包内每个文件都要修改

******************

## 官方文档位置
![00.png](imgs/00.png)
## 一、微服务架构概述
### 1.1 单体应用存在的问题？
* 虽然模块化，但所有业务集于1个war
* 复杂性：模块边界模糊、代码质量参差不齐、互相依赖、牵一发动全身···
* 技术债务：不坏不修，坏了不敢修
* 部署频率低：1个war
* 可靠性差：一个bug就打不出包
* 模块需求差异：CPU、IO内存需求不同
* 技术局部更进困难：框架一致
### 1.2 解决方案
* 微服务
### 1.3 优点
* ...
### 1.4 挑战
* 1、运维成本
* 2、分布式固有的问题：容错、网络、事务
* 3、接口调整成本
* 4、粒度不够，服务间代码重叠：共享库又不能跨语言
### 1.5 原则
* 单一职责：···
* 服务自治：高度解耦、包括部署
* 轻量通信：REST、AMQP、STOMP、MQTT
* 粒度：领域驱动
### 1.6 如何实现？
* 1、技术选型
- [x] 框架：Spring Cloud齐全，或Dubbo、DropWizard、Armada
- [x] 平台：PC Server、阿里云、AWS、**Docker(云原生PaaS)**
* 2、手绘架构图
![01.png](imgs/01.png)
## 二、微服务开发框架 Spring Cloud
### 2.1 Spring Cloud 简介
Spring boot基础上构建，Docker、PaaS上部署，云原生，参照《十二要素应用宣言》
### 2.2 Spring Cloud 特点
* 约定优于配置
* 适应多环境部署
* 隐藏组件复杂性
* 开箱即用
* 轻量级组件丰富： Eureka、Zuul
* 选型中立：支持使用Eureka、Zookeeper、Consul实现服务发现
* 组件间解耦
### 2.3 Spring Cloud 版本（伦敦地铁站ABC排序）N多组件都有自己的发布版本
* Angel    -对应- Spring boot 1.2.x
* Brixton  -对应- Spring boot 1.3.x
> eg：Camden SR3 = 主版本Camden的第三次bug修复  -Spring boot 1.4.x、1.5测试
>> 每个子组件都有自己的版本号 1.0.3.Release
## 三、用Cloud实战微服务
### 3.1 技术基础、版本方案
- [x] JAVA、Groovy、Scala语言均可
- [x] Spring boot
- [x] 构建工具: Maven/Gradle 
> tip：Gradle转Maven的命令：gradle init --type pom
- [x] 框架版本：JDK1.8---Spring Boot1.4.3.Release---Spring Cloud Camden SR4
- [x] IDE版本：Spring Tool Suit 3.8.3---Maven 3.3.9
### 3.2 构建服务提供者&服务消费者
用户微服务（提供）-----电影微服务（消费）-----用户（人）
### 3.3 服务提供者
> [自动初始化工具](http://start.spring.io)
```
parent-----------------spring-boot-starter-parent
dependencies-----------spring-boot-starter-web、JPA持久层接口、h2数据库
dependencyManagement---spring-cloud-dependencies
build------------------spring-boot-maven-plugin
application.yml：[yet another markup language] boot/cloud支持properties+yml 两种格式的配置文件
```
### 3.4 服务消费者
```
1、pom配置同上
2、@SpringBootApplication下添加RestTemplate的Bean
3、业务中注入RestTemplate，向提供者微服务发送rest请求
```
### 3.5 整合Spring Boot Actuator 
> 监控端点、运行状况
```
为生产者消费者添加依赖：
+dependencies-----------spring-boot-starter-actuator
```
> Actuator提供的端点：localhost:8000/health
- [x] 【GET】
* autoconfig------------显示自动配置信息
* beans-----------------显示应用上下文所有的Spring bean
* configprops-----------显示所有@ConfigurationProperties的配置属性列表
* dump------------------显示线程活动的快照
* env-------------------显示应用的环境变量
* health----------------显示应用的健康指标（值由Healthindicator的实现类提供）
* info------------------显示应用信息
* mappings--------------显示所有url路径
* metrics---------------显示应用度量标准信息
* trace-----------------显示跟踪信息，默认最近100个http请求
- [x] 【POST】
* shutdown--------------关闭应用，该功能启用：endpoints.shutdown.enable=true 
### 3.6 以上硬编码的硬伤
- [x] ①消费者代码里写提供者的url，消费者配置文件里写提供者的url，一端变化，另一端也要重新发布
- [x] ②动态伸缩，一个微服多套实例，容灾+负载均衡，自动增删节点时，受限
## 四、微服务的注册于发现
### 4.1 啥是发现？
* 微服启动之时，将网络地址等信息注册到服务发现组件，由其存储
* 消费者去发现组件中查询提供者的信息
* 微服务→发现组件 之间采用通信机制（如心跳），发现组件长时间无法与某微服通信，则注销该实例
IP端口变更，重新注册
> Eureka、Consul、Zookeeper
![02.png](imgs/02.png)
### 4.2 [Eureka](https://github.com/Netflix/Eureka) 简介
* Netflix开源的服务发现组件，位于子项目 Spring Cloud Netflix 中
* 基于Rest的服务
* Server+Client 两部分
### 4.3 Eureka原理
![03.png](imgs/03.png)
* Availability Zone--机房
* Region-------------跨机房的Eureka集群（springcloud默认使用 us-east-1 集群）
> AWS通过这种方式实现了最大的 容错+稳定性
![04.png](imgs/04.png)

> Eureka server：
>> 服务发现，接收微服启动时发来的注册信息（默认30s续约自己的租期），
并存储（默认90s未收到就注销实例）

> Eureka client：
>> 一个Java客户端，用于简化与Eureka server的交互，
一般会缓存注册表，替server拦截重复的心跳请求 + 备份以防server宕机

>一般Server与Client同体，Server间相互复制注册表

### 4.4 编写 [Eureka Service]:http://localhost:8761
```
① dependencies-----------spring-cloud-starter-eureka-server
  dependencyManagement---spring-cloud-dependencies
② @SpringBootApplication @EnableEurekaServer
③ application.yml--------
server.port:8761
eureka.client.registerWithEureka:false  # 不用将自己注册到其他Eureka服务器，自己此处是服务器
eureka.client.fetch-registry: false     # 自己是单点Eureka服务，不用同步其他节点数据
eureka.client.serviceUrl.defaultZone: http://localhost:8761/eureka/
# 设置与EurekaServer的 交互地址，查询、注册服务 多个地址,分隔
```
### 4.5 将微服务注册到 Eureka Server 上
微服务pom.xml
```
① dependencies-----------spring-cloud-starter-eureka
② @SpringBootApplication @EnableDiscoveryClient/@EnableEurekaClient
③ application.yml--------
spring.application.name: ***
eureka.client.service-url.default-zone: http://localhost:8761/eureka/
eureka.instance.prefer-ip-address: true # 注册自己的ip到Eureka,否则是自己的OS-hostname
```
### 4.6 Eureka Server 高可用 / Eureka Server 集群 相互注册、防止宕机
```
① 基于单节点Eureka Server，防止直接运行jar报jar中没有主清单属性，pom添加
build------------------spring-boot-maven-plugin
② 修改hosts文件：127.0.0.1 localhost peer1 peer2
③ application.yml（第一部分共用，二、三部分分别为spring.properties指定一套值）
spring.application.name: ms-discovery-eureka-ha
---
spring.profiles: peer1 # 描述为peer1
server.port: 8761
eureka.instance.hostname: peer1 # 当指定Profiles=peer1，主机名是peer1
eureka.client.serviceUrl.defaultZone: http://peer2:8762/eureka/ # 将自己注册到peer2这个Eureka上面
---
spring.profiles: peer2 # 描述为peer2
server.port: 8762
eureka.instance.hostname: peer2 # 当指定Profiles=peer2，主机名是peer2
eureka.client.serviceUrl.defaultZone: http://peer1:8761/eureka/ # 将自己注册到peer1这个Eureka上面
④ 先打包，再命令行启动，通过spring.profile.active指定使用哪个profile启动。
java -jar 4.6-discovery-eureka-ha-1.0-SNAPSHOT.jar --spring.profiles.active=peer1 (启动可能会报peer2连接被拒，不要紧，等peer2启动就好了)
java -jar 4.6-discovery-eureka-ha-1.0-SNAPSHOT.jar --spring.profiles.active=peer2
也可以 --server.port=8001，反正jar优先取同级config/下的配置文件，其次同级prop，其次包内，命令里指定配置应该优先级最高
⑤ 验证
http://peer1:8761 的register-replicas中已有peer2节点
http://peer2:8762 的register-replicas中已有peer1节点
⑥ 将微服务注册到Eureka Server集群上
eureka.client.serviceUrl.defaultZone: http://peer1:8761/eureka/,http://peer2:8762/eureka/
# 配置一个地址也能达到效果，因为服务间互相复制传播，不过还是建议配全
```
### 4.7 为 Eureka Server 添加用户认证
```
添加如下配置
① pom内添加
dependency-------------spring-boot-starter-security
② application.yml 内添加
security.basic.enable: true
security.user.name: wsws0521 # 默认是user
security.user.password: 0521wsws # 默认是随机数，启动服务时打印
③ 将微服务注册到带认证的server
需要设置defaultZone: http://wsws0521:0521wsws@localhost:8761/eureka/

DiscoveryClientOptionalArgs可实现复杂ClientFilter
```
### 4.8 Eureka 的元数据
```
标准元数据：微服务的 IP、端口、状态页、健康检查
自定义元数据：
① 提供者微服务的application.yml中添加
eureka.instance.metadata-map.my-key: myvalue
② 消费者微服务的Controller中注入 DiscoveryClient
③ 消费者微服务的Controller中定义/user-instance访问接口
返回List<ServiceInstance>=discoveryClient.getInstances("//spring.application.name");

验证：启动 [4.7-Eureka]、[提供者]、[消费者]，说白了通过消费者看到提供者的自定义配置项
访问 http://localhost:8761/eureka/apps 查看返回结果，包含my-key: myvalue
访问 http://localhost:8010/user-instance 查看返回结果，包含my-key: myvalue
```
### 4.9 Eureka Server的 REST 端点
```
其实 Eureka Client就是通过这些REST接口与Server交互的，如：
注册----------POST /eureka/apps/appID
注销----------DELETE /eureka/apps/appID/instanceID
发送心跳------PUT /eureka/apps/appID/instanceID
等等
PS：appID是应用程序的名称，
instanceID是与实例关联的唯一ID，AWS下为微服务实例ID，非AWS下为实例主机名

可用 XML 或者 Json 与其交互，冒充注册
测试XML请求：
① 编写符合xsd要求的参数格式[略]的xml文件
② curl命令 cat ./xml文件.xml | curl -v -X POST -H "Content-tpye:application/xml" -d
    @- http://localhost:8761/eureka/apps/rest-api-test(appID)
③ 注册成功，可查看http://localhost:8761/eureka/apps/rest-api-test(appID)
```
### 4.10 Eureka 的自我保护模式
> EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
>> 例如网络分区故障，导致不再收到心跳，并不是微服务宕机，不应该注销该实例
所以进入了“自我保护模式”，不删注册表，网络恢复后，退出自我保护模式
保守地保留所有微服务，不盲目撤销健康的 微服务，
可使用eureka.server.enable-self-preservation=false来禁用该模式
### 4.11 微服务自己是多网卡环境，选择自己的某个IP，用来注册到eureka
```
① 按网卡名称忽略：
spring.cloud.inetutils.ignored-interfaces:
- docker0    #忽略docker0网卡
- veth.*     #忽略以veth开头的网卡
② 按正则表达式指定：
spring.cloud.inetutils.preferredNetworks:
- 192.168
- 10.0
eureka.instance.prefer-ip-address: true
③ 只是用站点本地址：
spring.cloud.inetutils.useOnlySiteLocalInterfaces: true
eureka.instance.prefer-ip-address: true
④ 手动选定自身的IP地址：
eureka.instance.prefer-ip-address: true
eureka.instance.ip_address: 127.0.0.1
```
### 4.12 Eureka 的健康检查
* ① 只有status为UP的才会被请求，DOWN、OUT_OF_SERVICE、UNKNOWN
* ② 微服务发送心跳，即认为UP健康，但是“网络抖动”、“连不上数据源”无法工作，实际有内伤
* ③ Actuator检测的健康状态发送到Eureka，微服务端application.yml配置
> eureka.client.healthcheck.enable: true
>> 此时Actuator的/pause端点失效，无法暂停应用，可能是bug....
>>> 更细粒度的控制安全检查：可实现com.netflix.appinfo.HealthCheckHandler接口。
## 五、Ribbon-----客户端负载均衡
### 5.1 Ribbon简介
* 一个提供者部署多个实例，消费者将请求分摊
* Netflix公司发布的负载均衡器
* 控制HTTP/TCP客户端的行为
* 基于某种负载均衡算法（轮询、随机..），帮消费者去请求
* Ribbon+Eureka，自动从eureka获取到 服务提供者 地址列表
![05.png](imgs/05.png)
### 5.2 为消费者整合Ribbon
```
movie消费者 添加如下配置
① pom内添加
dependency-------------spring-cloud-starter-ribbon（操蛋的是：eureka依赖包含了此依赖，可省略。5.5就是抛弃Eureka单独使用ribbon）
② RestTemplate Bean(创建实例的地方，而非注入实例的地方)上面添加 @LoadBalanced，使其具有负载均衡能力
③ 将RestTemplate、LoadBalancerClient（用于查询节点信息）注入到Controller
④ 处理GET请求时，将ip:port改为instanceID（虚拟主机名可以通过eureka.instance.virtual-host-name配置）
⑤ 添加/log-instance请求处理，打印实例的相关信息
⑥ 将提供者打包，通过profile区分端口启动多个实例

验证：
启动[4.4-Eureka]，启动[提供者·多实例](可用上文jar指定profile，也可以在IDEA右上角配置Springboot的profile写上spring.profiles名字，就这么简单)，启动[消费者]
访问localhost:8761查看 消费者-提供者 是否是 1-N 的关系
多次访问 http://localhost:8010/user/1，看后台都是哪个提供者实例在处理（均匀分布）
多次访问 http://localhost:8010/log-instance，看后台日志打印（均匀分布）
```
### 5.3 通过[Java代码]自定义 Ribbon 的配置
> 修改均衡规则等

| Bean类型 | Bean名称 | 类名 |
|---------|----------|-----|
|IClientConfig|ribbonClientConfig|DefaultClientConfigImpl|
|IRule|ribbonRule|ZoneAvoidanceRule|
|IPing|ribbonPing|NoOpPing|
|ServerList|ribbonServerList|ConfigurationBasedServerList|
|ServerListFilter|ribbonServerListFilter|ZonePreferenceServerListFilter|
|ILoadBalancer|ribbonILoadBalancer|ZoneAwareILoadBalancer|
```
改造[5.2-movie消费者] 添加如下配置
① 新建@Configuration类返回IRule的Bean
② 新建@Configuration空类，使用@RibbonClient将均衡对象与①绑定

验证：
启动[4.4-Eureka]，启动[5.2提供者·多实例](可用上文jar指定profile，也可以在IDEA右上角配置Springboot的profile写上spring.profiles名字，就这么简单)，启动该[消费者]
多次访问 http://localhost:8010/log-instance，看后台日志打印（随机分布）
```
### 5.4 通过[properties.yml]自定义 Ribbon 的配置
> 修改均衡规则等

| 类名 | 说明 |
|---------|----------|
|NFLoadBalancerClassName|配置ILoadBalancer的实现类|
|NFLoadBalancerRuleClassName|配置IRule的实现类|
|NFLoadBalancerPingClassName|配置IPing的实现类|
|NIWSServerListClassName|配置ServerList的实现类|
|NIWSServerListFilterClassName|配置ServerListFilter的实现类|
```
改造[5.2-movie消费者] 的properties.yml 添加如下配置，便能实现均衡对象与均衡规则的绑定
microservice-provider-user:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule

验证：
启动[4.4-Eureka]，启动[5.2提供者·多实例](可用上文jar指定profile，也可以在IDEA右上角配置Springboot的profile写上spring.profiles名字，就这么简单)，启动该[消费者]
多次访问 http://localhost:8010/log-instance，看后台日志打印（随机分布）
```
### 5.5 脱离Eureka使用[Ribbon]
```
既然Ribbon这么香，肯定有单独使用的需求
改造[5.2-movie消费者]
① 《pom.xml》替换spring-cloud-starter-eureka为spring-cloud-starter-ribbon
② 《启动类》去掉@EnableDiscoveryClient
③ 《properties.yml》去掉eureka配置，添加请求地址列表配置
microservice-provider-user:
  ribbon:
    listOfServers: localhost:8000, localhost:8001

验证：
启动[01提供者·多实例，因为5.2会去连eureka]
>>java -jar 01-simple-provider-user-1.0-SNAPSHOT.jar
>>java -jar 01-simple-provider-user-1.0-SNAPSHOT.jar --server.port=8001
启动该[消费者]
多次访问 http://localhost:8010/log-instance，看后台日志打印（均匀分布）
```
## 六、Feign-----更优雅地调用HTTP API
### 6.1 Feign简介
* Netflix公司开发的声明式、模板化的HTTP客户端（灵感：Retrofit、JAXRS-2.0、WebSocket）
* SpringCloud加强了Feign，赋予其SpringMVC注解（契约默认SpringMVCContract），集成Ribbon和Eureka....真的香...
* https://github.com/OpenFeign/feign
### 6.2 为消费者整合Feign
```
改造[4.5-movie消费者]
① 《pom.xml》引入依赖spring-cloud-starter-feign
②  添加Service接口，以@FeignClient修饰
③  Controller注入该Service
② 《启动类》@EnableFeignClients    -- 否则③注入接口要求有实现类...
 
验证：
启动[4.4-Eureka]，启动[5.2提供者·多实例](可用上文jar指定profile，也可以在IDEA右上角配置Springboot的profile写上spring.profiles名字，就这么简单)，启动该[消费者]
多次访问 http://localhost:8010/user/1，
不但实现了REST API调用，还实现了客户端的负载均衡！！！！
```
### 6.3 自定义Feign配置
https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/1.4.4.RELEASE/single/spring-cloud-netflix.html
```
默认配置类：FeignClientsConfiguration -- 包含feign的[编码器、解码器、契约-默认SpringMVCContract]
@FeignClient(configuration = 自定义的优先级更高.class)

改造[6.2-movie消费者]
① 定义配置类
② Service接口@FeignClient指定配置类，注解便可以使用feign原生
③ 还可以自定义feign的编码器、解码器、日志打印
甚至为feign添加拦截器（一些接口基于http basic的认证才能调用）

验证：
启动[4.4-Eureka]，启动[5.2提供者·多实例](可用上文jar指定profile，也可以在IDEA右上角配置Springboot的profile写上spring.profiles名字，就这么简单)，启动该[消费者]
多次访问 http://localhost:8010/user/1，
不但实现了REST API调用，还实现了客户端的负载均衡！！！！
```
### 6.4 手写feign
> 生产者
```
改造[4.5-user提供者]
添加依赖：spring-boot-starter-security
编写用户名、密码、角色配置

验证：
启动[4.4-Eureka]、[6.4-user-auth]
访问 http://localhost:8000/1
后台打印用户信息
```
> 消费者
```
改造[6.2-movie消费者]
去掉启动类上的@EnableFeignClients
去掉接口上的@FeignClient(name = "microservice-provider-user")
修改controller：
@Import(FeignClientsConfiguration)
初始化两个[UserFeignClient对象](一个是admin，一个是user)

验证：
启动[4.4-Eureka]，启动[6.4提供者]，启动该[6.4消费者]
访问 http://localhost:8010/user-user/1，可以查询结果，且[6.4提供者]日志输出：当前用户是：user，角色是：user-role
访问 http://localhost:8010/user-admin/1,可以查询结果，且[6.4提供者]日志输出：当前用户是：admin，角色是：admin-role
```
### 6.5 Feign.继承
* 消费者的 Feign接口 可以继承（extends） 一个普通的父接口
* 消费者的 Feign接口 可以被 提供者的 Controller 实现 
> 不推荐，微服务间接口共享，紧耦合|方便性之间做出取舍
>> Feign不属于SpringMVC，方法参数映射不能被继承
### 6.6 Feign.压缩
* 对请求/响应进行压缩
```
《properties.yml》
feign.compression.request.enable=true
# feign.compression.request.mime-types=text/xml,application/xml,application/json
# feign.compression.request.min-request-size=2048 # 设置请求的最小阈值
feign.compression.request.response=true
```
### 6.7 Feign.日志
* 可为每个Feign客户端定制
```
改造[6.2-movie消费者]
编写Feign.Logger配置类 FeignLogConfiguration，FULL/BASIC
Feign接口指定此配置类
《properties.yml》中指定日志级别 DEBUG

验证：
启动[4.4-Eureka]、[4.5-user]、[6.7-movie]
访问 http://localhost:8010/user/1
[6.7-movie]后台打印DEBUG日志，可以FULL也可以BASIC
```
### 6.8 Feign.构造多参数请求
> GET
```
<UserFeignClient>
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User findById(User user); // 405，认为是POST
    public User findById(@PathVariable("id") Long id,
                         @PathVariable("username") String username); // OK
    public User findById(@PathVariable Map(String, Object) map); // OK HashMap(String, Object) map = Maps.newHashMap();
```
> POST
```
@RestController
public class UserController {
    @PostMapping("/post")
    public User post(@RequestBody User user){ ... }
}

<UserFeignClient>
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public User findById(@RequestBody User user); // OK
```
## 七、Hystrix 实现微服务容错处理
> 当依赖的服务不可用时，整个系统会不会被拖垮？
>> 雪崩效应：基础服务不可用---级联故障---整个系统瘫痪

### 7.1 容错机制：
* 为网络请求设置超时：防止长时间占用线程/进程
* 断路器模式：服务太忙了，就让后续请求**快速失败**，也能**自我修复**
> 断路打开一段时间之后，会进入‘半开’状态，允许1个请求进去，相应OK则会闭合断路器，否则继续断开
### 7.2 Hystrix 容错机制：
> [Hystrix](https://github.com/Netflix/Hystrix) 是 Netflix开源的，实现了上述2种机制的，工具类库。
* 包裹请求,防止裸调：HystrixCommand/HystrixObserverCommand，每个命令在独立线程中执行（使用命令模式）
* 跳闸机制
* 资源隔离：为每个依赖创建线程池，满，则不用等，直接失败
* 监控：近乎实时，指标、配置变化
* 回退机制：请求失败/拒绝后，可以自定义回退逻辑
* 自我修复：即‘半开’
#### 7.2.2 通用方式整合Hystrix
```
改造[5.2-movie消费者]
① 《pom.xml》引入依赖spring-cloud-starter-hystrix
②  启动类：@EnableCircuitBreaker/@EnableHystrix
③  Controller方法上：@HystrixCommand(fallbackMethod = "自定义回退方法名，服务降级，好过直接挂掉")   // 由javanica库(hystrix的子项目)提供。SpringCloud自动将SpringBean与该注解封装在一个‘连接到Hystrix断路器的代理中’。
 
验证：
启动[4.4-Eureka]，启动[4.5提供者]，启动该[消费者]
访问 http://localhost:8010/user/1，
停止[4.5提供者]
访问 http://localhost:8010/user/1，
返回了-1User
```
> 进入‘回退方法’并不代表断路器打开，想知道断路器状态？看下面
#### 7.2.3 通用方式整合Hystrix
> 因为Eureka依赖actuator
```
http://localhost:8010/health
```
> 看到Hystrix的状态是UP
```
停止[4.5提供者]
快速多次访问 http://localhost:8010/user/1
把断路器 干断！
http://localhost:8010/health    // "CIRCUIT_OPEN"

启动[4.5提供者]
访问 http://localhost:8010/user/1
断路器 自动重连
http://localhost:8010/health    // "UP"
```
#### 7.2.4 Hystrix 线程隔离策略 & 传播上下文
[官网wiki](https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.strategy)
```
@HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
```
> ① Tread 线程隔离（默认|建议，有个除网络超时以外的保护层）
>> @HystrixCommand在单独的线程上执行，并发请求 受‘线程池’约束（服务限流）
>>> 如果发生 scope context(域上下文)找不到，因为本地线程无法传播到HystrixCommand线程，可以考虑换成下一种
>>>> 可以查阅：https://github.com/spring-cloud/spring-cloud-netflix/issues/1336（SpringCloud与Netflix文档对照，容易理解）
```
@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
```
> ② Semaphore 信号量隔离（开销小一点，适用每秒数百次请求）
>> @HystrixCommand在原调用线程上执行，并发请求 受‘信号量个数’约束（服务限流）（令牌桶算法）

拓展：
- [x] [服务熔断、降级、限流(上文讲了)、异步RPC(就是并发调用省时间)](https://blog.csdn.net/fanrenxiang/article/details/78443774)
- [x] [分布式系统延迟和容错框架Hystrix](https://blog.csdn.net/fanrenxiang/article/details/78443799)
#### 7.2.5 Feign 使用 Hystrix
> 项目classpath有Hystrix，则Feign所有方法都自动被包裹，感谢SpringCloud
##### 7.2.5.1 Feign 使用 Hystrix 的回退方法
```
改写[6.2消费者feign]
编写回退类，继承自 FeignClient 接口
@FeignClient 里面加上fallback属性,指定回退类

验证：
[4.4-eureka] + [4.5-user] + [7.2.5.1]
访问 http://localhost:8010/user/1
停止[4.5-user]
访问 http://localhost:8010/user/1
```








****







