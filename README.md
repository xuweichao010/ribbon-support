## RibbonSupport

### 解决的问题

```text
    在开发中 在本地启动微服务程序调试时，其他开发人员调用接口路由到本地程序上，
影响对程序的调试。并且调试时其他开发人员调用接口路由到本地调试服务，导致http请
求一直阻塞，影响其他开发人员对接口问题的判断。这个问题在大型的微服务体系中存、
大的开发团队中在尤为严重的问题， ribbon-support 就是为了解决这个问题而封装的.
    它解决了开发人员本地服务可以接微服务集群中，而不干扰集群的正常运行，并且开
    发人员通过HTTP请求配置又能调用到本地服务问题。
```

### 简介

```text
    这是一个在微服务中使用的组件，他可以帮助开发人员无入侵的接入服务集群，并且
可以通过服务的集群网关调用本地服务的接口不用担心本地服务的DEBUG时影响到其它人
开发人员对接口的调用。
    也可以暴露最新的接口信息给前端人员,方便前端开发者和后端开
发者的接口联调。
```

### 实现流程

![img.png](imgs/img.png)

**路由的优先级:** `指定路由(从请求头中获取到、从seesion中获取到的)`  >  `默认路由(服务器配置的路由)` > `随机路由(系统自己决定)`

### 快速开始

- 添加仓库信息

```xml

<repositories>
    <repository>
        <id>my-public</id>
        <name>my-public</name>
        <url>http://maven.onetozero.cn/repository/maven-public/</url>
    </repository>
</repositories>
```

- 添加maven坐标

```xml

<dependency>
    <groupId>com.xwc.support</groupId>
    <artifactId>ribbon-support</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

- 开启路由

```yaml
ribbon:
  support:
    ip-rule:
      enable: true  #开启优先IP路由配置
      exclude-ip-regex: '^10\.27\.(5|7|8|9|10)\.[0-9]{1,3}$'  # 无法随机路由的网段 只能通过IP路由默认路由来访问
      default-ip: 10.27.12.191  # 默认路由

```