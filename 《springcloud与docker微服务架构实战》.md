# 《springcloud与docker微服务架构实战》
> 使用parent统一管理版本
>> 就要确保dependency在所配maven仓库中有对应版本
>>> 比如1.5.9的parent没有对应的1.5.9的actuator，就会报红色波浪线
>>>> 但是只要下载到了替代版本，大部分波浪线并不影响运行
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
cloud | boot
------|------
Angel | 1.2.x
Brixton | 1.3.x
Camden | 1.4.x 1.5.x
Dalston | 1.5.x
Edgware | 1.5.x
Finchley | 2.0.x
> eg：Camden SR3 = 主版本Camden的第三次bug修复  -Spring boot 1.4.x、1.5测试
>> 每个子组件都有自己的版本号 1.0.3.Release

> 版本不匹配，报错：
>> Exception in thread "main" java.lang.IllegalArgumentException: Cannot find class [org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration]
>>> ERROR org.springframework.boot.SpringApplication - Application run failed
java.lang.NoSuchMethodError: org.springframework.boot.builder.SpringApplicationBuilder.<init>([Ljava/lang/Object;)V
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
### 6.1 [Feign简介](https://github.com/OpenFeign/feign)
* Netflix公司开发的声明式、模板化的HTTP客户端（灵感：Retrofit、JAXRS-2.0、WebSocket）
* SpringCloud加强了Feign，赋予其SpringMVC注解（契约默认SpringMVCContract），集成Ribbon和Eureka....真的香...
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
#### 7.2.3 断路器状态监控&深入理解
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
> 项目classpath有Hystrix，则Feign所有方法都自动被包裹，感谢SpringCloud。如果不需要，参照7.2.5.3
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
##### 7.2.5.2 通过 Fallback Factory 检查回退原因
```
改写[6.2消费者feign]
编写回退工厂类，继承自 回退工厂 接口（其实也是一层外包装）
@FeignClient 里面加上 fallbackFactory 属性,指定 回退工厂 类

何处打印[cause]仍旧存在疑问......

验证：
[4.4-eureka] + [4.5-user] + [7.2.5.2]
访问 http://localhost:8010/user/1
停止[4.5-user]
访问 http://localhost:8010/user/1
[7.2.5.2]应该打印回退原因

拓展：
可以对[cause]进行 instanceOf 判断，进一步业务区分
```
##### 7.2.5.3 Feign 拒绝 Hystrix 的自动包裹
*  单独禁用
```
1、定义配置类
@Configuration
public class FeignDisableHystrixConfiguration {
    @Bean
    @Scope("prototype") // 多实例
    public Feign.Builder feignBuilder(){
        return Feign.builder();
    }
}
2、@FeignClient 里面加上 configuration 属性,指定该 配置 类
```
* 全局禁用
```
<application.yml>
feign.hystrix.enable = false
```
### 7.3 Hystrix 的监控
* Hystrix & HystrixObservableCommand 在执行时，会生成 执行结果 & 运行指标
> 如：请求数、成功数...
* 通过 Hystrix 的模块：hystrix-metrics-event-stream 向外披露
> 格式：text/event-stream
* 项目添加 actuator 就可使用/hystrix.stream端点获得Hystrix的监控信息了
> 如：7.2.2 已经支持
```
验证：
[4.4-eureka] + [4.5-user] + [7.2.2]
访问 http://localhost:8010/hystrix.stream 空白/转圈监听
访问 http://localhost:8010/user/1
访问 http://localhost:8010/hystrix.stream 有了
```
* Feign 项目里 的 hystrix 监控
```
改造[7.2.5.1]
1、需要显式引入 spring-cloud-starter-hystrix 依赖
2、启动类上添加 @EnableCircuitBreaker

验证：
[4.4-eureka] + [4.5-user] + [7.3]
访问 http://localhost:8010/hystrix.stream 空白/转圈监听
访问 http://localhost:8010/user/1
访问 http://localhost:8010/hystrix.stream 有了，但是数据可读性太差，请看下文
```
### 7.4 使用 Hystrix Dashboard 可视化监控数据
```
创建[7.4]（本章使用1.5.9-parent，否则启动报缺失class...）
1、引入hystrix-dashboard依赖

验证：
启动[7.4]
访问 http://localhost:8030/hystrix 就看到面板了
启动[4.4-eureka] + [4.5-user] + [7.3]
在面板URL输入 http://localhost:8010/hystrix.stream
在面板Title输入 随意
点击 {monitor stream}
访问 http://localhost:8010/user/1 监控面板就有显示了!!
```
> 其实咱也可以尝试把 Hystrix Dashboard 注册到 Eureka 上，更方便的管理
### 7.5 使用 Turbine 聚合监控数据
#### 7.5.1 Turbine 简介
> 就是把多个微服务的 /hystrix.stream 聚集成一个 /turbine.stream
#### 7.5.2 Turbine 动手
```
创建[7.5.2]
1、引入 spring-cloud-starter-netflix-turbine 依赖
2、启动类 @EnableTurbine
3、application.yml 指定要聚合的微服务名称：[4.5消费者]、[7.3消费者]（改下端口、名称防止撞车）

验证：
启动[4.4-eureka] + [4.5-user] + [7.2.2] + [7.3] + [[7.5.2-turbine]] + [7.4-dashboard]
访问 http://localhost:8010/user/1 让[7.2.2]产生监视数据
访问 http://localhost:8020/user/1 让[7.3]产生监视数据
打开 http://localhost:8030/hystrix.stream 看板首页(路径省略.stream也可以)
在面板URL输入 http://localhost:8031/turbine.stream 看板指向[[7.5.2]]
在面板Title输入 随意
点击 {monitor stream}
 监控面板就有显示了!!
```
#### 7.5.3 使用消息中间件 收集数据
> 微服务 与 turbine 不在同一网段/网络不通时
>> 微服务 将消息发给 中间件
>>> 中间件 再供给 turbine 消费
##### 7.5.3.1 安装 RabbitMQ
* 1、安装依赖：[Erlang/OTP 19.2](http://www.erlang.org/downloads)
* 2、安装RabbitMQ服务：[RabbitMQ Server 3.6.6](https://www.rabbitmq.com/releases/rabbitmq-server/)(http://www.rabbitmq.com/install-windows.html)
* 3、开启可视管理：安装路径/sbin>>rabbitmq-plugins enable rabbitmq_management，访问http://localhost:15672，guest/guest
##### 7.5.3.2 微服务 -- RabbitMQ
```
改造[7.2.2-movie]（本章使用1.4.3-parent）
1、引入 spring-cloud-[netflix-hystrix]-stream 依赖
2、引入 spring-cloud-starter-stream-rabbit 依赖
3、application.yml 指明消息中间件地址
```
##### 7.5.3.3 RabbitMQ -- turbine
```
改造[7.5.2-turbine]
0、去除原 turbine 依赖，修改启动类 @EnableTurbine --> @EnableTurbineStream
1、引入 spring-cloud-[starter-turbine]-stream 依赖
2、引入 spring-cloud-starter-stream-rabbit 依赖
3、application.yml 配置服务端口8031，指明消息中间件地址，去除原来的turbine配置
```
Turbine改造完成...
```
验证：
启动[4.4-eureka] + [4.5-user] + [7.5.3.2] + [7.5.3.3]
访问 http://localhost:8010/user/1 可正常获得结果
访问 http://localhost:8031 可发现turbine能持续不断的显示监控数据
```

## 八、使用 Zuul 构建微服务网关
### 8.1 为什么要涉及网关
* 客户端一个业务下来，要访问多个微服务，客户端也累
* 跨域问题
* 认证复杂：每个微服务都需要认证
* 微服务迭代：分久必合合久必分，让客户端抓狂
* 某些微服务可能使用防火墙等
> 方案：【客户端】----1【网关】n----【微服务】
* 易于监控
* 易于认证
* 减少交互次数
### 8.2 [Zuul](https://github.com/Netflix/zuul) 简介
Zuul 又是 Netflix 的开源项目，核心是“过滤器”
[学习地址](https://github.com/Netflix/zuul/wiki/How-We-Use-Zuul-At-Netflix)
* 身份认证 & 安全
* 审查 & 监控：边缘位置追踪有意义的数据，精确的生产视图
* 动态路由：定向后端集群
* 压力测试：可以通过控制指定集群的流量来实现
* 负载分配：同上
* 静态响应处理：在边缘位置直接建立部分响应，从而避免转发到内部集群
* 多区域弹性：跨越AWS region进行请求路由，旨在实现ELB(弹性负载均衡)，让系统边缘更贴近系统使用者
> Cloud 整合 Zuul 默认的使用 http 客户端：Apache http client
>> 使用 RESTClient：ribbon.restclient.enable = true
>>> 使用 okhttp3.OkHttpClient：ribbon.okhttp.enable = true
### 8.3 编写 Zuul 网关
```
创建[8.3]
添加依赖：spring-cloud-starter-netflix-zuul
添加依赖：spring-cloud-starter-netflix-eureka-client
启动类 @EnableZuulProxy
配置 application.yml 8040

验证：
启动[4.4-eureka] + [4.5-user] + [5.2-movie] + [8.3]
访问 http://localhost:8040/microservice-consumer-movie/user/1 访问消费者（可能一时间找不到注册的服务，等下再试试）
访问 http://localhost:8040/microservice-provider-user/1       访问生产者
http://localhost:8761/

验证 负载均衡：
关掉[4.5-user]，启动多个[5.2-user]多实例
多次访问 http://localhost:8040/microservice-provider-user/1  会发现Zuul内置Ribbon负载均衡

验证 容错：
启动[4.4-eureka] + [4.5-user] + [5.2-movie] + [8.3] + [7.4-hystrix-dashboard]
访问 http://localhost:8040/microservice-consumer-movie/user/1 访问消费者
访问 http://localhost:8030/hystrix 的URL输入 http://localhost:8040/hystrix.stream 说明支持了hystrix
```

### 8.4 Zuul的路由端点
> 8.3 spring-cloud-starter-netflix-zuul（@EnableZuulProxy） 其实还集成了 actuator （/routes端点）
>> GET 就是查询 POST 就是手动强刷
```
验证：
启动[4.4-eureka] + [4.5-user] + [5.2-movie] + [8.3]
访问 http://localhost:8040/routes 查看 路径-微服务 映射
```
### 8.5 路由配置详解
> 只想让 Zuul 代理部分微服务，URL 精确控制
* 1、指定路径
```
zuul:
  routes:
    microservice-provider-user: /user/**
```
* 2、忽略某些微服务
```
zuul:
  ignored-services: microservice-provider-user,microservice-consumer-movie
```
* 3、忽略所有，只路由指定微服务
```
zuul:
  ignored-services: '*'   # 使用'*'可忽略所有微服务
  routes:
    microservice-provider-user: /user/**
```
* 4、同时指定微服务的serviceId和对应路径
```
zuul:
  routes:
    user-route:                   # 该配置方式中，user-route只是给路由一个名称，可以任意起名。
      service-id: microservice-provider-user
      path: /user/**              # service-id对应的路径
```
* 5、同时指定path和url
```
zuul:
  routes:
    user-route:                   # 该配置方式中，user-route只是给路由一个名称，可以任意起名。
      url: http://localhost:8000/ # 指定的url
      path: /user/**              # url对应的路径。
```
> 将/user/**直接映射到 localhost:8000/**下
>> 代价：不会通过HystrixCommand执行，换言之不支持Hystrix容错，同时不支持Ribbon负载均衡。看6
* 6、同时指定path和URL，并且不破坏Zuul的Hystrix、Ribbon特性。
```
zuul:
  routes:
    user-route:
      path: /user/**
      service-id: microservice-provider-user
ribbon:
  eureka:
    enabled: false    # 禁用掉ribbon的eureka使用。详见：http://cloud.spring.io/spring-cloud-static/Camden.SR3/#_example_disable_eureka_use_in_ribbon
microservice-provider-user:
  ribbon:
    listOfServers: localhost:8000,localhost:8001
```
* 7、正则表达式--路由匹配规则
```
@Bean
public PatternServiceRouteMapper serviceRouteMapper() {
    // （微服务正则，路由正则）
    return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)", "${version}/${name}");
}
```
> 此配置可实现将诸如：microservice-provider-user-v1这个微服务映射到/v1/microservice-provider-user/**这个路径
* 8.1、路由前缀
```
zuul:
  prefix: /api
  strip-prefix: false
  routes:
    microservice-provider-user: /user/**
logging:
  level:
    com.netflix: DEBUG
```
> 访问Zuul的/api/microservice-provider-user/1路径，请求将会被转发到microservice-provider-user的/api/1，可查看日志打印，有助于理解。
* 8.2、路由前缀
```
zuul:
  routes:
    microservice-provider-user: 
      path: /user/**
      strip-prefix: false
logging:
  level:
    com.netflix: DEBUG
```
> 访问Zuul的/user/1路径，请求将会被转发到microservice-provider-user的/user/1，可查看日志打印，有助于理解。
>> 可以查阅：https://github.com/spring-cloud/spring-cloud-netflix/issues/1365
>>> 可以查阅：https://github.com/spring-cloud/spring-cloud-netflix/issues/1514
* 9、忽略某些路径（更细粒度）
```
zuul:
  ignoredPatterns: /**/admin/**   # 忽略所有包括/admin/的路径
  routes:
    microservice-provider-user: /user/**
```
* 10、本地转发（略）
```
zuul:
  routes:
    route-name:
      path: /path-a/**
      url: forward:/path-b
```
> 如此可将 microservice-provider-user 服务映射到 /user/**，但会忽略此为服务中包含/admin/的路径
### 8.6 Zuul的安全 & Header
#### 8.6.1 设置敏感 Header
* 系统中服务共享 header
* 防止header外泄，需要设置敏感header
```
// 为单个微服务指定（优先级更高）zuul.routes.*.sensitive-headers:
zuul:
  routes:
    microservice-provider-user:
      path: /users/**
      sensitive-headers: Cookie,Set-Cookie,Authorization
      url: https://downstream
// 全局设置
zuul:
  sensitive-headers: Cookie,Set-Cookie,Authorization # 其实这仨是默认的
```
#### 8.6.2 忽略 Header
* 丢弃一些 Header，阻止其传播到其他微服务中
* classpath中若存在有Spring Security，ignore-headers默认不为空，便会阻止如下header的传播。
> Pragma、Cache-Control、X-Frame-Options、X-Content-Type-Options、X-XSS-Protection、Expires
>> 可通过zuul.ignore-headers:false来使其传播
>>> 可以查阅：https://github.com/spring-cloud/spring-cloud-netflix/issues/1487
>>>> 单元测试...略
```
zuul:
  ignore-headers: Header1,, Header2
```
### 8.7 使用 Zuul 上传文件
* 小于 1M 可直接上传
* 大于 10M  需要为上传路径添加 /zuul/ 前缀，或者使用zuul.servlet-path自定义前缀。如使用ribbon，则需要改大超时 阈值
```
# 上传大文件得将超时时间设置长一些，否则会报超时异常。以下几行超时设置来自http://cloud.spring.io/spring-cloud-static/Camden.SR3/#_uploading_files_through_zuul
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds: 60000
ribbon:
  ConnectTimeout: 3000
  ReadTimeout: 60000
```
> 一般都自带 [下载curl.zip](https://curl.haxx.se/download.html),解压添加Path/curl.exe放在System32下也可以
>> PowerShell 无法识别 -参数，建议参照大象》CMD.exe --- PowerShell，使用原生 CMD.exe测试
```
创建[8.7-upload]
普通，注册到Eureka
application.yml配置http文件传输大小
编写HTML页面（支持访问 localhost:8050）未用到...

验证：
启动[4.4-eureka] + [8.7-upload] + [8.3-zuul]
直接上传至8.7微服务：curl -F "file=@small.file" localhost:8050/upload  -- OK
上传小文件至zuul网关：curl -v -H "Transfer-Encoding=chunked" -F "file=@small.file" localhost:8040/microservice-file-upload/upload  -- OK
上传大文件至zuul网关：curl -v -H "Transfer-Encoding=chunked" -F "file=@large.file" localhost:8040/microservice-file-upload/upload  -- 不OK,500提示size超限
上传zuul（添加前缀）：curl -v -H "Transfer-Encoding=chunked" -F "file=@large.file" localhost:8040/zuul/microservice-file-upload/upload  -- 若提示zuul.hystrix超时，zuul需要添加前面超时配置即可（尝试依旧超时，不知是否文件太大）
```
### 8.8 Zuul 过滤器
> 过滤器才是zuul的核心
#### 8.8.1 过滤器类型 & 请求生命周期
类型|生命周期|用途
---|-------|---
PRE|【请求】-x-【路由】-【微服务】|请求被路由之前调用，身份验证、集群中选择请求的微服务、记录调试信息
ROUTING|【请求】-【路由】-x-【微服务】|将请求路由至微服务，用于构建发送给微服务的请求，使用Apache HTTPClient/Netflix Ribbon
POST|【请求】-【路由】-【微服务】-x|在路由到达微服务之后执行，为Response添加标准header，收集统计指标，传递Response给客户
ERROR|-|其他阶段发生错误时执行
自定义|-|如：拦截请求在zuul中直接处理并返回

一个请求的生命周期：
![8.8.1.png](imgs/8.8.1.png)

#### 8.8.2 编写 Zuul 过滤器
```
复制[8.3-zuul]
编写自定义过滤器 继承 ZuulFilter
修改启动类：生效自定义过滤器

验证：
启动[4..4-eureka] + [4.5-user] + [8.8.2]
访问：http://localhost:8040/microservice-provider-user/1 [8.8.2]自定义的过滤器打印send GET request to http://localhost:8040/microservice-provider-user/1
```
#### 8.8.3 禁用 zuul 过滤器
> Cloud 默认为 zuul 编写了几个过滤器：DebugFilter、FormBodyWrapperFilter、PreDecorationFilter...
>> 位于： spring-cloud-netflix-core.jar包下的(org.springframework.cloud.netflix.zuul.filters)
>>> 如何禁用：zuul.<ClassName>.<filterType>.disable=true
>>>> 例如禁用上文过滤器：zuul.PreRequestLogFilter.pre.disable=true
### 8.9 zuul 的 容错 & 回退
```
验证：
启动[4..4-eureka]8671 + [4.5-user]8000 + [8.3-zuul]8040 + [7.4-hystrix-dashboard]8030
访问：http://localhost:8040/microservice-provider-user/1 正常获得结果
访问：http://localhost:8040/hystrix.stream 正常获得hystrix监控数据
访问：http://localhost:8030/hystrix 输入地址http://localhost:8040/hystrix.stream
关闭 [4.5-user]8000
访问：http://localhost:8040/microservice-provider-user/1 不优雅！
```
> 网关 zuul 监控的粒度是 微服务，而不是API，如果关闭4.5，并不能优雅的显示 default 用户
```
复制[8.3-zuul]8040---[8.9]
编写zuul回退类 实现 ZuulFallBackProvider

重复验证：
启动[4..4-eureka]8671 + [4.5-user]8000 + [8.9-zuul-fallback]8040 + [7.4-hystrix-dashboard]8030
访问：http://localhost:8040/microservice-provider-user/1 正常获得结果
访问：http://localhost:8040/hystrix.stream 正常获得hystrix监控数据
访问：http://localhost:8030/hystrix 输入地址http://localhost:8040/hystrix.stream
关闭 [4.5-user]8000
访问：http://localhost:8040/microservice-provider-user/1 变得优雅！
```
### 8.10 Zuul 的 高可用
> 作为网关，必须高可用，避免单点故障

* 情况1：Zuul Client 注册到了 Eureka Server上
> 很简单，多启动几个 Zuul Client 即可
* 情况2：Zuul Client 没注册到 Eureka Server上
> 比如 zuul 是一个 APP
>> 只能在 zuul 和 请求方 之间再设立一个负载均衡器，如：Nginx、HAProxy、F5 等

### 8.11 使用 Sidercar（整合了zuul） 整合 非JVM微服务
> 4.9 非JVM 也可以操作 Eureka 的 REST 端点
>> Sidecar灵感来自于 Netflix Prana
#### 8.11.1 编写 Node.js 服务
```
创建一个简单的 [8.11.1-node-service] (只要是 非JVM工程都可)
使用命令行启动：node node-service.js
测试：http://localhost:8060/health.json
测试：http://localhost:8060/
```
#### 8.11.2 编写 Sidecar
```
创建[8.11.2-Sidecar]
依赖：spring-cloud-netflix-sidecar
启动类：@EnableSidecar
配置：拉手 [8.11.1]

验证：
启动 [8.11.1-node-service] + [4.4-Eureka] + [4.5-user]8000 + [8.11.2-Sidecar]8070
访问 http://localhost:8070/microservice-provider-user/1   返回张三
既然 Sidecar 可以访问 user 微服务，那么 Sidecar 拉手的非JVM，一样可以访问Eureka上的微服务。。。
启动 [8.11.2.1-Sidecar-client-ribbon]8071
访问 http://localhost:8071/test   就被转接到了 非JVM 服务上
```
#### 8.11.3 Sidecar 端点
* / 返回一个测试页面，展示常用端点
* /hosts/{serviceId} 指定为服务在Eureka上的实例列表
* /ping 返回 "OK"
* /{serviceId} 因为整合了zuul，所以可以直接转发请求到指定微服务
#### 8.11.4 Sidecar 与 Node.js 分离部署
方法一
```
eureka:
  instance:
    hostname: 非JVM微服务的hostname
```
方法二（Netflix1.3.0之后）
```
sidecar:
  hostname: 非JVM微服务的hostname
  ip-address: 非JVM微服务的IP地址
```
> 可以查阅：https://github.com/spring-cloud/spring-cloud-netflix/issues/981
#### 8.11.5 Sidecar 原理
* 访问 http://localhost:8761/eureka/apps/microservice-sidecar-node-service
> 可以看到 sidecar帮node-service将8060注册到了 eureka，还有homePage
* @EnableSidecar 整合了 @EnableZuulProxy
> 可以访问 http://localhost:8070/routes（可能需要配置 management.security.enabled : false）
* 被拉手的node-service挂了，http://localhost:8070/health就会显示down
### 8.12 使用 Zuul 聚合微服务
> 客户只发送一次请求，zuul 多次请求自己手上的微服务，然后一次性返回
```
复制[8.3-zuul]----------------[8.12-zuul-aggregation]
启动类：添加RestTemplate
创建 User 实体类
创建 聚合服务 AggregationService
创建 控制层 AggregationController（涉及 RxJava-异步链式库-观察者模式的应用）

验证：
启动 [4.4-Eureka] + [4.5-user]8000 + [4.5-movie]8010 + [8.12-zuul-aggregation]8040
（微服务聚合）访问 http://localhost:8040/aggregate/1    依次调用了movie、user
（Hystrix容错）停止movie、user，再访问 http://localhost:8040/aggregate/1 实现了容错回退
```

## 9 Spring Cloud Config 统一管理配置
### 9.1 为什么
微服务架构中，常有如下需求：
* 统一管理配置文件：适应繁多的微服务
* 适应不同环境：开发环境、测试环境、生产环境
* 运行期间动态调整：根据实际load调整一些阈值、连接池，且不影响微服务运行
* 修改后自动生效
### 9.2 [Spring Cloud Config](https://github.com/spring-cloud/spring-cloud-config)
【Config Server】【Config Client】都实现了Spring环境 与 Property Source 的抽象映射（就是为Spring打造的）
* Config Server：可横向扩展、集中式的配置服务器，默认Git存储配置内容
* Config Client：用于操作Server上的配置
![9.2.png](imgs/9.2.png)
> 每个微服务启动时会请求Server获取配置，并缓存起来哦
### 9.3 编写 Config Server
作者在gitee上写了几个配置文件，此处我写在【gitee-config-repo】文件夹下，并上传至GitHub。
为测试版本控制，在master下建立 config-label-v2.0 分支，在该分支下，将配置文件中的1.0改为2.0
```
创建[9.3-config-server]
1-依赖：spring-cloud-config-server
2-启动类：@EnableConfigServer
3-配置：Git仓库/账号/密码（在国内还是建议用码云）

准备4种配置文件：
microservice-foo    （取这种名字的配置文件，就认为微服务名称是 microservice-foo，即覆盖掉上文3-中配置的微服务名称）
microservice-foo-dev
microservice-foo-production
microservice-foo-test
在
master/                     下；
master/config-label-v2.0/   分之下修改成2.0；

验证：
启动 [9.3-config-server]8080
http://localhost:8080/microservice-foo/dev                              -- 可以获取 master下 foo、foo-dev 配置文件名|路径标识 列表
http://localhost:8080/microservice-foo/dev/config-label-v2.0            -- 可以获取 master/config-label-v2.0下 foo、foo-dev 配置文件名列表
http://localhost:8080/microservice-foo-dev.properties                   -- 直接获取 master下 指定配置文件的内容：profile: dev-1.0
http://localhost:8080/config-label-v2.0/microservice-foo-dev.properties -- 直接获取 master/config-label-v2.0下 指定配置文件的内容：profile: dev-2.0
```
### 9.4 编写 Config Client
```
创建[9.4-config-client]
1-依赖：spring-cloud-starter-config
2-配置文件 bootstrap（主程序的“引导上下文”动作会加载名为bootstrap.*的文件内容，优先级更高，必须配置于此，否则就默认 config-server 是 localhost:8888。题外话：禁用引导过程：spring.cloud.bootstrap.enable=false）
指定 config-server

验证：
启动 [9.3-config-server]8080 + [9.4-config-client]8081
http://localhost:8081/profile                             -- 可以获取到相应配置文件（即使现在关掉9.3也没关系，因为配置文件被缓存起来了）
```
### 9.5 Config Server 的Git仓库配置详解
spring.cloud.config.server.git.uri
* 占位符支持：{application} {profile} {label}
> https://github.com/wsws0521/IDEA-Vincent-microservice.git
>> https://github.com/wsws0521/{application}    -- 即可支持一个应用/profile对应一个Git仓库
* 模式匹配：{application}/{profile}
> spring.cloud.config.server.git.repos.simple: https://         --simple仓库地址，只匹配所有配置文件中名为simple的应用程序
>> spring.cloud.config.server.git.repos.special.pattern: special*/dev*, *special*/dev*
>> spring.cloud.config.server.git.repos.special.uri: https://   --special仓库地址
>>> spring.cloud.config.server.git.repos.local.pattern: local*
>>> spring.cloud.config.server.git.repos.local.uri: file:/home/ --local仓库地址，匹配所有配置文件中以local开头的应用程序
* 搜索目录：（同样支持占位符）
> spring.cloud.config.server.git.search-paths: foo,bar*     --搜索foo子目录以及bar开头的子目录
* 启动时就去访问Git加载配置（默认都是第一次被client请求才去clone git仓库）
> spring.cloud.config.server.git.uri:
> spring.cloud.config.server.git.repos.team-a.pattern: microservice-*
> spring.cloud.config.server.git.repos.team-a.clone-on-start: true
> spring.cloud.config.server.git.repos.team-a.uri:
>> team-a 可以替换为 *，也可以直接全局 git.clone-on-start: true
>>> 启动就去校验 Git 仓库，蛮好的

### 9.6 Config Server 的健康状况指示器
自带：http://localhost:8080/health -- {"status":"UP"} -- 可用来校验配的 EnvironmentRepository 可否访问

> 请求 EnvironmentRepository 时默认使用的：{application}=app, {profile}=default, {label}=master
>> 当然也可以配置
>>> spring.cloud.config.server.health.repositories.a-foo.label: config-label-v2.0
>>> spring.cloud.config.server.health.repositories.a-foo.name: microservice-foo
>>> spring.cloud.config.server.health.repositories.a-foo.profiles: dev

禁用：spring.cloud.config.server.health.enable: false
```
复制[9.3]----[9.6-config-server-health]-----只是加了health配置而已
```
### 9.7 配置内容的加解密（例如DB用户名密码，不想明文显示）
#### 9.7.1 安装 JCE （Java 密码学 扩展）
[下载Java8JCE](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
Config Server 的加解密依赖于此，安装 Java8JCE 就是替换 JDK/jre/lib/security/下面的两个jar
#### 9.7.2 Config Server 的加解密端点
```
curl $CONFIG_SERVER_URL/encrypt -d 想要加密的明文
curl $CONFIG_SERVER_URL/decrypt -d 想要解密的密文
```
#### 9.7.3 对称加密
```
复制[9.3]----[9.7.2-config-server-encryption]
必须在 bootstrap 里设置对称秘钥（主程序的“引导上下文”动作会加载名为bootstrap.*的文件内容，优先级更高。题外话：禁用引导过程：spring.cloud.bootstrap.enable=false）

验证：
启动 [9.7.2-config-server-encryption]8080
访问：curl localhost:8080/encrypt -d vincentPWD    //即可得到密文
访问：curl localhost:8080/decrypt -d 密文           //即可得到明文
```
#### 9.7.4 存储加密的内容
```
在gitee-config-repo/添加配置文件encryption.yml，将密文加入
push到Git远端！！！

验证：
启动[9.7.2]8080
访问：localhost:8080/encryption-default.yml    // 从Github拿下来自动解密，看到明文 spring.cloud.coonfig.server.encrypt.enabled = false 直接返回密文本身
```
#### 9.7.5 非对称加密
```
复制[9.3]----[9.7.5-config-server-encryption-rsa]
生成一个keyStore：
keytool -genkeypair -alias mytestkey -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US" -keypass 123456 -keystore server.jks -storepass 123456
生成的server.jks放到classpath下面
application.yml下面指定keyStore

验证：
启动 [9.7.5-config-server-encryption]8080
访问：curl localhost:8080/encrypt -d vincentPWD    //即可得到【更复杂的RSA】密文
访问：curl localhost:8080/decrypt -d 密文           //即可得到明文
```
### 9.8 使用/refresh端点手动刷新配置
```
复制[9.4]----[9.8-config-client-refresh]
添加actuator依赖（内含refresh端点）
Controller添加 @RefreshScope
application.yml的security置为false，否则没有refresh节点权限

验证：
启动 [9.3-config-server]8080 + [9.8-config-client-refresh]8081
访问：localhost:8081/profile
// 返回https://github.com/wsws0521/IDEA-Vincent-microservice/blob/master/gitee-config-repo/microservice-foo-dev.properties内容
// 修改内容，再次访问，返回未见修改
发送[POST]：curl -X POST http://localhost:8081/refresh     // 实现刷新，刷新有变化才有返回内容，否则返回[]
再次访问：localhost:8081/profile     // 返回刷新后的内容
```
### 9.9 使用 Spring Cloud Bus 自动刷新配置
> Bus  类似分布式的 Actuator  可以广播消息（类似RabbitMQ、kafka的模式）
>> 所有微服务都和Bus消息总线相连，都有【订阅】
![9.9.png](imgs/9.9.png)
#### 9.9.2 实现自动刷新
```
安装MQ（参考7.5.3.1），开启服务，http://localhost:15672这是管理界面
复制[9.8-config-client-refresh]-----[9.9.2-onfig-client-refresh-bus]
添加依赖：bus-amqp
bootstrap.yml中指定rabbitmq服务地址


验证：
启动 [9.3-config-server]8080 + [9.9.2-config-client-refresh-bus]8081 + [9.9.2-config-client-refresh-bus]8082
访问：localhost:8081/profile
// 返回https://github.com/wsws0521/IDEA-Vincent-microservice/blob/master/gitee-config-repo/microservice-foo-dev.properties内容
// 修改内容，再次访问，返回未见修改
发送[POST]：curl -X POST http://localhost:8081/bus/refresh     // 注意路径中带bus，才会去广播修改
再次访问：localhost:8081/profile     // 返回刷新后的内容
再次访问：localhost:8082/profile     // 返回刷新后的内容
```
> 这其实还是要手动一次，如果想让GitHub主动推送修改，需要再Github/repository/右侧Settings设置添加webhooks指明推送地址

#### 9.9.3 局部刷新
> bus/refresh?destination=微服务ApplicationContextID（其实默认就是 微服务名：端口）
> bus/refresh?destination=微服务名：****     刷新指定的 微服务名
#### 9.9.4 改进：将server添加进bus群
> 人为POST刷新看来是省不掉，那么最好在server上刷新，让微服务成员保持单一职责
![9.9.4.png](imgs/9.9.4.png)
```
复制[9.3-config-server]-----[9.9.4-config-server-refresh-bus]8080
添加依赖：bus-amqp
application.yml中指定rabbitmq服务地址 + 【开启bus追踪，后面可用/trace跟踪查看】 + security：false

验证：
启动 [9.9.4-config-server-refresh-bus]8080 + [9.9.2-config-client-refresh-bus]8081 + [9.9.2-config-client-refresh-bus]8082
访问：localhost:8081/profile
// 返回https://github.com/wsws0521/IDEA-Vincent-microservice/blob/master/gitee-config-repo/microservice-foo-dev.properties内容
// 修改内容，再次访问，返回未见修改
发送[POST]：curl -X POST http://localhost:8080/bus/refresh     // 注意在8080server上刷新
再次访问：localhost:8081/profile     // 返回刷新后的内容
再次访问：localhost:8082/profile     // 返回刷新后的内容
```
#### 9.9.5 跟踪总线事件
> application.yml中，开启bus追踪，后面可用/trace跟踪查看传播细节
### 9.10 将config-server，config-client都注册到Eureka
> config-server
>> 很简单，直接添加application配置
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```
> config-client
>> 修改bootstrap，通过发现方式找到config-server，再将自己注册到Eureka
```
spring:
  application:
    name: microservice-foo    # 对应config server所获取的配置文件的{application}
  cloud:
    config:
      profile: dev
      label: master
      discovery:
        enabled: true                                  # 表示使用服务发现组件中的Config Server，而不自己指定Config Server的uri，默认false
        service-id: microservice-config-server-eureka  # 指定Config Server在服务发现中的serviceId，默认是configserver

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```
### 9.11 为config-server添加用户认证
允许匿名访问总是不安全的
此处简单加个 HTTP BASIC 认证
```
复制[9.3-config-server]-----[9.11-config-server-authenticating]8080
添加依赖：security
application.yml中【开启认证】 + 【用户名密码】

验证：
启动 [9.11-config-server-authenticating]8080
访问：localhost:8080 就会弹出认证框
[9.4-client]的uri: http://localhost:8080/连接串应改成
方案① uri: http://用户名:密码@localhost:8080/        # curl风格
方案② 额外添加 username、password属性(优先级更高)
否则无法启动
```
### 9.12 config-server高可用
#### 9.12.1 Git高可用
① 官方的Github、Bitbucket、git@osc、Coding等人家本身就实现了高可用，不用担心
② 自己搭建的Git仓库，如[搭建GitLab高可用](https://about.gitlab.com/high-availability/)
#### 9.12.2 MQ高可用
[搭建RabbitMQ高可用](https://www.rabbitmq.com/ha.html)
#### 9.12.3 config-server高可用
① 没注册到Eureka
[servers]------[负载均衡器]-------[clients]
② 已注册到Eureka
[servers]------[Eureka]-------[clients]

## 10 微服务跟踪 Sleuth（侦探、警犬）
### 10.1
> Actuator/Hystrix 都属于“监控”
> 现在是要跟踪，为什么要跟踪？
* 网络！脆弱，资源有限
* 跟踪！调优，发现瓶颈
### 10.2 术语
> 大量借用 Google Dapper、Twitter Zipkin 和 Apache HTrace的设计
* span（64位ID）
* trace（64位ID）span组成的树
* annotation（标注）CS客户端发送时间戳，SR服务端接收时间戳，CS-SR是啥一步了然，SS和CR是啥也能猜到
### 10.3 整合Sleuth
```
复制[01-simple-provider-user]-----[10.3-simple-provider-user-trace]8000
复制[02-simple-consumer-movie]-----[10.3-simple-consumer-movie-trace]8010
添加依赖：sleuth
application.yml中【给自己应用起名字】 + 【开启 DispatcherServlet/Sleuth 日志】

验证：
启动 [10.3-simple-provider-user-trace]8000
访问：localhost:8000/1 后台 会打印带ID的日志
启动[10.3-simple-consumer-movie-trace]8010
访问：localhost:8010/user/1 后台 会打印带ID的日志
```
### 10.4 为Sleuth整合 ELK 日志分析工具（elastic）
> [安装ELK](https://www.elastic.co/guide/index.html)
```
略
```
### 10.5 为Sleuth整合 Zipkin 日志分析工具
> [安装Zipkin](http://zipkin.io/)
>> Zipkin是Twitter开源的 分布式跟踪系统，擅长收集时序数据，分析微服务架构延时问题，带 不错的 UI
```
略
```

## 11 常见问题&总结（使用时查阅吧）
### 11.1 Eureka问题
* 注册慢
* 注销慢/不注销
* 如何自定义InstanceID
* Eureka的UNKNOW问题
### 11.2 Hystrix/Feign首次请求失败
### 11.3 Turbine聚合数据不完整
### 11.4 原生配置
### 11.5 Cloud问题思路




****







