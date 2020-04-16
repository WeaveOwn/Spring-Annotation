package cn.weaveown.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

import cn.weaveown.bean.Person;

/**
 * @ComponentScan之 excludeFilters条件 注解运用
 * @author willvi
 *
 */
@Configuration //配置注解类似 applicationcontext.xml
//excludeFilters = Filter[] 根据规则排除哪些组件
/*@Filter type 包含 
 * ANNOTATION 通过注解类型 列如 @Controller为Controller.class @Service 为 Service.class
 * ASSIGNABLE_TYPE, 一组具体类 例如PersonController.class
 * ASPECTJ, 一组表达式,使用Aspectj表达式命中类
 * REGEX 一组表达式,使用正则命中类
 * CUSTOM 自定义的TypeFilter.
 * */ 
@ComponentScan(value= "cn.weaveown",excludeFilters= {
		@Filter(type=FilterType.ANNOTATION,value= {Controller.class}),
		@Filter(type=FilterType.ASSIGNABLE_TYPE,value= {IncludeConfig.class,MainConfig.class}),
})

public class ExcludeConfig {
	@Bean
	public Person person() {
		return new Person("willvi",23);
	}
	
	public static void main(String[] args) {
		ApplicationContext ioc = new AnnotationConfigApplicationContext(ExcludeConfig.class);
		String[] beans = ioc.getBeanDefinitionNames();
		for (String string : beans) {
			System.out.println(string);
		}
	}
}

