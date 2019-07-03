package com.dubbo.provider;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Provider {

	// start provider
	static{
		PropertyConfigurator.configure("src/main/resources/log4j.properties");
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		//Spring容器的加载，前面就是读取配置文件，
		//然后进行监听器添加，注册ShutdownHook
		context.start();
		//System.in.read()返回一个整型字节数据，该数据表示的
		//是字节因此是Unicode的第一个字节或是字符的ASCII码值。
		//该方法是从一个流中一个一个的读取数据，因此是一个迭代的过程
		System.in.read();
	}
}
