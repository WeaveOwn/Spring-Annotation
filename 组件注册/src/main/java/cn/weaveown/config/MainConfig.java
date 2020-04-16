package cn.weaveown.config;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import cn.weaveown.bean.Person;
import cn.weaveown.conditional.LinuxConditional;
import cn.weaveown.conditional.WindowConditional;

/**
 * @Bean @Scope @Lazy注解运用
 * @author willvi
 *
 */
@Configuration //配置注解类似 applicationcontext.xml
public class MainConfig {
	@Bean
	public Person person() {
		return new Person("willvi",23);
	}
	/*@scope 
	 * singlon 默认单例模式 在容器创建时初始化 除非使用@Lazy 懒加载
	 * prototype 非单列模式 在获取时初始化
	 * */
	
	@Scope("prototype")
	@Bean
	public Person personScope() {
		return new Person("Scope",23);
	}
	//根据自定义条件进行注册
	@Conditional({WindowConditional.class})
	@Bean
	public Person person01() {
		return new Person("windows",11);
	}
	@Conditional({LinuxConditional.class})
	@Bean
	public Person person02() {
		return new Person("linux",11);
	}
	public static void main(String[] args) {
		ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		//是否单例
		Person person = (Person) ioc.getBean("personScope");
		Person person1 = (Person) ioc.getBean("personScope");
		System.out.println(person==person1);
		
		//根据条件注册
		Environment environment = ioc.getEnvironment();
		String property = environment.getProperty("os.name");
		System.out.println(property);
		String[] beanDefinitionNames = ioc.getBeanDefinitionNames();
		for (String bean : beanDefinitionNames) {
			System.out.println(bean);
		}
		Map<String, Person> beansOfType = ioc.getBeansOfType(Person.class);
		System.out.println(beansOfType);
	}
}
