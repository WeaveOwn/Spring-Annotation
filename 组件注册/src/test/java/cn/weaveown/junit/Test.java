package cn.weaveown.junit;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

import cn.weaveown.bean.Person;
import cn.weaveown.config.MainConfig;

public class Test {
	/**
	 * 测试容器类包含的组件
	 */
	@org.junit.Test
	public void test01() {
		ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		String[] beans = ioc.getBeanDefinitionNames();
		for (String string : beans) {
			System.out.println(string);
		}
	}
	
	/**
	 * 验证@scope 单例和非单例情况
	 */
	@org.junit.Test
	public void test02() {
		ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		Person person = (Person) ioc.getBean("personScope");
		Person person1 = (Person) ioc.getBean("personScope");
		System.out.println(person==person1);
	}
	
	/**
	 * 验证自定义条件注册
	 */
	@org.junit.Test
	public void test03() {
		ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		Environment environment = ioc.getEnvironment();
		String property = environment.getProperty("os.name");
		System.out.println(property);
		String[] beanDefinitionNames = ioc.getBeanDefinitionNames();
		for (String string : beanDefinitionNames) {
			System.out.println(string);
		}
		Map<String, Person> beansOfType = ioc.getBeansOfType(Person.class);
		System.out.println(beansOfType);
	}
}
