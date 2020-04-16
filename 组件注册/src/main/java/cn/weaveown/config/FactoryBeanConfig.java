package cn.weaveown.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.weaveown.bean.MyFactoryBean;

/**
 * 通过FactoryBean的实现注册
 * @author willvi
 *
 */
@Configuration
public class FactoryBeanConfig {

	@Bean
	public MyFactoryBean MyFactoryBean() {
		return new MyFactoryBean();
	}
	
	public static void main(String[] args) {
		ApplicationContext  applicationContext= new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
		//获取的是MyFactoryBean中getObject()返回的实例
		System.out.println(applicationContext.getBean("MyFactoryBean"));
		//添加'&' 获取的是MyFactoryBean实例
		System.out.println(applicationContext.getBean("&MyFactoryBean"));
	}
}
