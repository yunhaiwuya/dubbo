dubbo+zookeeper+springmvc+nginx(集群负载均衡)
dubbo本身已经实现了服务负载均衡,是基于服务分布式框架
Http web层用nginx负载均衡,通过异步非阻塞能够有很大的并发量，据说是百万级
Zookeeper下载，安装，配置:
1、打开conf目录copy一份zoo_sample.cfg文件，并重命名为zoo.cfg
2、打开zoo.cfg，入手阶段基本不用做什么修改，唯一需要注意的是端口号，后面的dubbo 管理配置需要和这个保持一致
3、找到bin目录下的 zKserver.cmd 启动zookeeper
下载，配置dubbo的管理项目
jdk1.7、tomcat7环境下  dubbo-admin-2.5.4
1、下载 dubbo admin 项目
2、解压，取出war包。
3、拷贝一个tomcat，改一下端口号（用默认容易和其他的tomcat端口冲突）
4、将dubbo admin 包解压内容放到root目录中，修改classpath 目录下的dubbo.properties， 将这个监测中心注册到zookeeper，即将dubbo.registry.address改成你安装zookeeper的机子IP+zookeeper配置的端口
dubbo.registry.address=zookeeper://127.0.0.1:2181
dubbo.admin.root.password=root
dubbo.admin.guest.password=guest
5、bin目录启动tomcat，输入这个tomat的访问网址 http://localhost:8180/dubbo-admin-2.5.4,然后登陆密码都是就是上面配置文件中的，root  root登录
 
dubber-service 提供者
创建公用接口项目 service，创建两个provider （dubbo-provider1，dubbo-provider2）
创建消费者，这里创建2个Spring mvc web项目来充当消费的角色（dubbo-consumer1，dubbo-consumer2）
其中dubbo.xml
<!-- 生成远程服务代理，可以和本地bean一样使用demoService -->
    <dubbo:reference id="testService" interface="com.test.service.TestService" />
然后我们可以启动了，我们将2个consuemr分别放在两个tomcat中运行，2个tomcat端口要不一致的

到这里我们的dubbo+zookeeper+Spring mvc 搭建完成了，下一步，加入nginx 实现http层面的负载均衡
1、下载nginx，解压，并修改conf目录下的nginx.conf
2、我们新建个upstream 负载均衡，采取默认的nginx负载均衡策略，然后监听80端口，将80端口所有的请求代理转发到负载上面
#负载均衡服务器
	upstream dubbo-test-server{
		server 127.0.0.1:8084;
		server 127.0.0.1:8088;
	}
	server {         # 服务器主机配置
		#监听80端口
		listen  80;
		#定义使用localhost来访问
        server_name  localhost;
        #charset koi8-r;

		#定义本虚拟主机的访问日志
        #access_log  logs/host.access.log  main;

		# 路由配置
		location / {
			proxy_pass http://dubbo-test-server;
		}
命令行启动nginx，然后在浏览器中输入接口网址即可发现会出现四种结果，
consumer01provider01、consumer02provider01、consumer01provider02、consumer02provider02.  
dubbo+zookeeper+spring mvc+nginx负载均衡测试完成

其中，要去掉访问路径中的项目名。可以在tomcat server.xml中 <Host>加上
<Context docBase="dubbo-consumer2" path="" reloadable="true" source="org.eclipse.jst.j2ee.server:dubbo-consumer2"/>
也即去掉path的值，设置为“”，映射为空，就可访问去掉项目的路径了