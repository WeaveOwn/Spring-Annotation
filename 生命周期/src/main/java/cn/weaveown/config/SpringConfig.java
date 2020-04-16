package cn.weaveown.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.weaveown.bean.Person;

@Configuration
@ComponentScan(value= "cn.weaveown.bean")
public class SpringConfig {

	@Bean(initMethod="init",destroyMethod="destroy")
	public Person person() {
		return new Person();
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
		applicationContext.close();
	}
}
