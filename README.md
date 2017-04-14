### demo构建

> author: afterloe  
> mail: lm6289511@gmail.com  
> License: MIT  

spring boot 官网：http://projects.spring.io/spring-boot/  
本文档中涉及代码托管在: https://github.com/afterloe/spring-boot-demo.git  

按照官网的构建方案，我们推荐使用gradle来构建Spring boot 项目  
首先是是gradle安装，由于gradle安装被墙，所以推荐翻墙来安装，速度较快。完成之后可以立即使用，同时也可以为android项目进行构建。细节的安装就不在这里叙述了。环境采用的是jdk 1.8 ，所以基础的java开发环境请大家自己进行构建和处理，ide采用的是 eclipse j2ee edit版本。

#### 构建项目前的环境准备
> jdk 1.8 http://www.oracle.com/technetwork/java/javase/downloads/index.html   
> gradle  https://gradle.org/  
> eclipse j2ee edit  https://www.eclipse.org/downloads/  
> eclipse j2ee plug gradle  http://marketplace.eclipse.org/content/buildship-gradle-integration  

#### 开始构建项目
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_1.png)  
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_2.png)  
在项目下打开并编辑build.gradle，该文件为项目的构建文件，类似与node.js里的package.json，修改默认的构建文件
```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:1.5.2.RELEASE")
    }
}

apply plugin: 'java' // 构建插件 for java
apply plugin: 'eclipse' // 构建插件 for eclipse
apply plugin: 'org.springframework.boot'

jar {
	baseName = 'spring-boot-demo'
	version= '0.0.1'
}

sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
    mavenCentral() // 采用maven构建jar包
 //   jcenter() // 采用gradle构建jar包
}

dependencies {
	compile("org.springframework.boot:spring-boot-starter-web:1.5.2.RELEASE")
    testCompile 'junit:junit:4.12'
}
```

使用gradle进行项目构建
```bash
afterloe:spring-boot-demo $ ./gradlew eclipse
:eclipseClasspath
:eclipseJdt
:eclipseProject
:eclipse

BUILD SUCCESSFUL

Total time: 4.508 secs
```
在eclipse进行刷新项目  
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_3.png)  

开始编写java代码，spring-boot和node.js一样都是用一个主函数来进行处理，所以我们先些一个main函数用来启动Spring boot项目

```java
/**
 * create by aftelroe 2017-4-14
 */
package com.github.afterloe.spring_boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LanchApplication {

	public static void main(String[] args) {
		SpringApplication.run(LanchApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("检查并输出 spring boot中注册的beans");
			String[] beanNames = ctx.getBeanDefinitionNames();
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
	}
}
```
试运行一下，点击运行java项目  
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_4.png)  

Ok 我们继续构建，Spring-boot启动之后会监听一个端口，一般情况下时8080，为了达到我们的一个目标，我们做一个简单的resful接口。  
编写一个controller
```java
/**
 * create by afterloe
 */
package com.github.afterloe.spring_boot.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

	private static final String template = "%s this is %s access";
	
	@RequestMapping("/")
	public String index(@RequestParam(value = "name", defaultValue="localhost") String name) {
		return String.format(template, new Date().toString(), name);
	}
}
```

再次运行,并查看结果  
```bash
afterloe:spring-boot-demo $ curl 127.0.0.1:8080/
Fri Apr 14 11:08:27 CST 2017 this is localhost access
afterloe:spring-boot-demo $ curl 127.0.0.1:8080?name=afterloe
Fri Apr 14 11:08:29 CST 2017 this is afterloe access
```

如果我们不想spring boot监听8080端口，可以在配置文件处进行修改。创建一个名为application.properties的文件,存放在resources目录下，而resources则和java同一个目录。
```bash
afterloe:spring-boot-demo $ cd src/
afterloe:spring-boot-demo $ ls
main test
afterloe:spring-boot-demo $ cd main/
afterloe:spring-boot-demo $ ls
java
afterloe:spring-boot-demo $ mkdir resources
afterloe:spring-boot-demo $ ls
java      resources
afterloe:spring-boot-demo $ touch resources/application.properties
afterloe:spring-boot-demo $ ls -l resources/
total 0
-rw-r--r--  1 afterloe  staff  0  4 14 11:14 application.properties
```
如图  
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_5.png)  

修改内容如下
```
server.port=80
```
直接采用80端口进行服务，由于linux下小于1024的都需要root权限启动，所以我们在启动的时候加入的sudo  
修改完成之后直接编包运行
```bash
afterloe:spring-boot-demo $ ./gradlew build --daemon --stacktrace
:compileJava
:processResources
:classes
:findMainClass
:jar
:bootRepackage
:assemble
:compileTestJava UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 1.631 secs

afterloe:spring-boot-demo $ sudo java -jar ./build/libs/spring-boot-demo-0.0.1.jar
Password:


  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.2.RELEASE)

2017-04-14 11:25:19.688  INFO 38043 --- [           main] c.g.a.spring_boot.LanchApplication       : Starting LanchApplication on mbp.local with PID 38043 (/project/java/spring-boot-demo/build/libs/spring-boot-demo-0.0.1.jar started by root in /project/java/spring-boot-demo)
org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration
2017-04-14 11:25:22.762  INFO 38043 --- [           main] c.g.a.spring_boot.LanchApplication       : Started LanchApplication in 13.64 seconds (JVM running for 14.212)
```

浏览器访问一下http://127.0.0.1/ 和 http://127.0.0.1?name=afterloe  
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_6.png)  
![image](https://github.com/afterloe/docs/raw/master/spring-boot/images/build_project_7.png)  

如果生产环境下需要频繁修改application.properties则启动的时候可以采用以下几种方式  
* jar包所在目录同级的目录“/config”的子目录下
* jar包所在目录的同级目录下

```bash
afterloe:spring-boot-demo $ ls
application.properties              spring-boot-demo-0.0.1.jar          spring-boot-demo-0.0.1.jar.original
afterloe:spring-boot-demo $ more ./application.properties
server.port=15024
afterloe:spring-boot-demo $ java -jar spring-boot-demo-0.0.1.jar
afterloe:spring-boot-demo $ curl 127.0.0.1:15024/
Fri Apr 14 11:10:27 CST 2017 this is localhost access
```
