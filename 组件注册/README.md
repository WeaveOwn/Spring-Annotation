- [@Configuration](#1-configuration)
- [@ComponentScan](#2-componentscan)
  - [FilterType](#filtertype)
  - [excludeFilters](#excludefilters)
  - [includeFIlters](#includefilters)
  - [使用自定义TypeFilter](#使用自定义typefilter)
- [@Bean](#3-bean)
  - [Scope](#scope)
  - [Lazy](#lazy)
- [@Contional](#contional)
- [@Import](#import)
- [通过实现FactoryBean注册组件](#通过实现factorybean注册组件)

本文github位置:<https://github.com/WillVi/Spring-Annotation/>

## 1. @Configuration

​	`@Configuration` //配置注解类似 applicationcontext.xml 只是将xml配置改为 注解方式进行

## 2. @ComponentScan

进行包扫描会根据注解进行注册组件,value="包名"

```java
@ComponentScan(value="cn.weaveown")
```

 ### FilterType  

- ANNOTATION 通过注解类型 列如 @Controller为Controller.class @Service 为 Service.class
- ASSIGNABLE_TYPE, 一组具体类 例如PersonController.class
- ASPECTJ, 一组表达式,使用Aspectj表达式命中类
- REGEX 一组表达式,使用正则命中类
- CUSTOM 自定义的TypeFilter.

###  excludeFilters 

​	 excludeFIlters = Filter[] 根据规则排除组件

```java
@ComponentScan(value="cn.weaveown",excludeFilters= {
  	    //根据注解排除注解类型为@Controller
		@Filter(type=FilterType.ANNOTATION,value= {Controller.class}),
		@Filter(type=FilterType.ASSIGNABLE_TYPE,value= {IncludeConfig.class,MainConfig.class}),
})
```

### includeFilters

​	includeFIlters = Filter[]  根据规则只包含哪些组件（**ps：useDefaultFilters设置为false**）

```java
@ComponentScan(value="cn.weaveown",includeFilters= {
        //根据注解类型扫描注解类型为@Controller的类
		@Filter(type=FilterType.ANNOTATION,value= {Controller.class})
},useDefaultFilters=false)
```

### 使用自定义TypeFilter

​	当过滤有特殊要求时，可以实现TypeFilter来进行自定的过滤规则

自定义TypeFilter:

```java
public class CustomTypeFilter implements TypeFilter {
	/**
	 * metadataReader the metadata reader for the target class 读取当前扫描类的信息
	 * metadataReaderFactory a factory for obtaining metadata readers
	 * for other classes (such as superclasses and interfaces) 获取其他类的信息
	 */
	public boolean match(MetadataReader reader, MetadataReaderFactory factory) throws IOException {
		//获取当前扫描类信息
		ClassMetadata classMetadata = reader.getClassMetadata();
		//获取当前注解信息
		AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
		//获取当前类资源(类路径)
		Resource resource = reader.getResource();
		String className = classMetadata.getClassName();
		System.out.println("----->"+className);
		if(className.contains("PersonService")) {
			return true;
		}
		return false;
	}
}
```

使用：

```java
//自定义过滤组件
@ComponentScan(value="cn.weaveown",includeFilters= {
		@Filter(type=FilterType.CUSTOM,value= {CustomTypeFilter.class})
},useDefaultFilters=false)
//或者
//自定义过滤组件
@ComponentScan(value="cn.weaveown",excludeFilters= {
		@Filter(type=FilterType.CUSTOM,value= {CustomTypeFilter.class})})
```

## 3. @Bean

​	注册bean与spring 的xml配置异曲同工之妙只是将xml配置转换为注解

```xml
  <bean id="person" class="cn.weaveown.bean.Person"  scope="prototype" >
		<property name="age" value="23"></property>
		<property name="name" value="willvi"></property>
	</bean>
```

### @Scope

​	在 Spring IoC 容器是指其创建的 Bean 对象相对于其他 Bean 对象的请求可见范围。

 -  singleton单例模式  全局有且仅有一个实例

- prototype原型模式 每次获取Bean的时候会有一个新的实例

- request 每一次HTTP请求都会产生一个新的bean，同时该bean仅在当前HTTP request内有效

- session  session作用域表示该针对每一次HTTP请求都会产生一个新的bean，同时该bean仅在当前HTTP session内有效

- global session global session作用域类似于标准的HTTP Session作用域，不过它仅仅在基于portlet的web应用中才有意义。

  **以上5个一般只用第一个和第二个**

原型模式使用：

```java
	@Bean
	@Scope("prototype")
	public Person person() {
		return new Person("willvi",23);
	}
```

验证：

```java
	    ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		Person person = (Person) ioc.getBean("personScope");
		Person person1 = (Person) ioc.getBean("personScope");
		//返回true说明为单例
		System.out.println(person==person1);
```

### @Lazy

​	懒加载。当Scope为单例模式时，当容器被初始化时就会被实例化。

​	当有@Lazy时，在容器初始化时不会被实例化，在获取实例时才会被初始化

单例模式懒加载使用

```JAVA
@Bean
	@Scope
	@Lazy //去掉和加上看输出结果
	public Person person() {
        System.out.println("bean初始化");
		return new Person("willvi",23);
	}
```

验证：

```java
    ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
	System.out.println("容器初始化完成");
	Person person = (Person) ioc.getBean("personScope");
```



## 4. Conditional

​	根据自定义条件注册组件。需要实现`Condition`接口

接口实现例子：

```java
package cn.weaveown.conditional;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LinuxConditional implements Condition{
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		//获取环境
		Environment environment = context.getEnvironment();
        //获取组件注册类
		BeanDefinitionRegistry registry = context.getRegistry();
       //判断容器是否包含注册“person02”注册类
		boolean containsBeanDefinition = registry.containsBeanDefinition("person02");
       //获取当前操作系统并判断是否为Linux是的话 返回true；
      //返回true表示满足条件，ioc容器会注册该bean。反之则不会
		String property = environment.getProperty("os.name");
		if(property.contains("Linux")) {
			return true;
		}
		return false;
	}
}
```

运用：

```java
    //根据自定义条件进行注测
    //给定自定义的class
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
----------------------------------------------------------
    //也可以直接写在类前面
    //表示类中统一满足该条件才会被注册
	@Configuration
    @Conditional({WindowConditional.class})
  public mainConfig(){
  ....
	}
```

## 5. @Import

 1.  直接通过`@Import({Dog.class,Pig.class})`

 2. 实现ImportSelector接口 ’`@Import({Dog.class,ImportSelector.class})`

   ```java
   package cn.weaveown.selector;

   import org.springframework.context.annotation.ImportSelector;
   import org.springframework.core.type.AnnotationMetadata;

   /**
    * ImportSelector @Import注解其中之一 选择器例子
    * @author willvi
    *
    */
   public class AnnoImportSelector implements ImportSelector{

   	/**
   	 * AnnotationMetadata获取当前类的注解信息
   	 */
   	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
   		//需要返回全类名
   		String [] beans = {"cn.weaveown.bean.Pig"};
   		return beans;
   	}

   }
   ```

 3. 实现ImportBeanDefinitionRegistrar实现手工注册组件 `@Import({Dog.class,AnnoImportBeanDefinitionRegistrar.class})`

   ```java
   package cn.weaveown.selector;

   import org.springframework.beans.factory.support.BeanDefinitionRegistry;
   import org.springframework.beans.factory.support.RootBeanDefinition;
   import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
   import org.springframework.core.type.AnnotationMetadata;

   import cn.weaveown.bean.Person;

   /**
    * 
    * ImportBeanDefinitionRegistrar接口实现
    * @author willvi
    *
    */
   public class AnnoImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar{

   	/**
   	 * AnnotationMetadata获取当前类的注解信息
   	 * BeanDefinitionRegistry 注册类 可以手工注册组件
   	 */
   	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
   		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Person.class);
   		//参数为：bean id名 bean定义信息
   		registry.registerBeanDefinition("person", rootBeanDefinition);
   		
   	}

   }
   ```

   使用：

   ```java
   package cn.weaveown.config;

   import org.springframework.context.ApplicationContext;
   import org.springframework.context.annotation.AnnotationConfigApplicationContext;
   import org.springframework.context.annotation.Configuration;
   import org.springframework.context.annotation.Import;

   import cn.weaveown.bean.Dog;
   import cn.weaveown.selector.AnnoImportBeanDefinitionRegistrar;
   import cn.weaveown.selector.AnnoImportSelector;

   /**
    * @Import注解的运用
    * @author willvi
    *
    */
   @Configuration
   //快速导入到容器内 输入的组件名称为全类名
   //通过实现ImportSelector接口
   //通过实现ImportBeanDefinitionRegistrar手工注册
   @Import({Dog.class,AnnoImportSelector.class,AnnoImportBeanDefinitionRegistrar.class})

   public class AnnoImportConfig {

   	public static void main(String[] args) {
   		ApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnoImportConfig.class);
   		String[] beanNames = annotationConfigApplicationContext.getBeanDefinitionNames();
   		for (String bean : beanNames) {
   			System.out.println(bean);
   		}
   	}
   }

   ```

## 6. 通过实现FactoryBean注册组件

​	通过实现FactoryBean接口（工厂bean）通常用于整合第三方框架

接口实现：

```java
package cn.weaveown.bean;

import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBean implements FactoryBean<Pig>{

	public Pig getObject() throws Exception {
		return new Pig();
	}

	public Class<?> getObjectType() {
		return Pig.class;
	}

	//是否单例
	public boolean isSingleton() {
		return true;
	}

}
```

使用：

```java
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

```

## 小结

​	给容器注册组件的几种方式：

1. 包扫描配合注解（`@Controller\@Service\@Repository\@Component）[自己写的类]
2. @Bean 导入第三方包的组件
3. @Import
   - 直接通过 @Import({Dog.class,Pig.class})
   - 通过实现ImportSelector接口返回全类名数组 @Import({实现的类})
   - 通过实现ImportBeanDefinitionRegistrar接口手工注册组件 @Import({实现的类})
4. 通过实现FactoryBean接口（工厂bean）通常用于整合第三方框架
   - 默认获取到的是FactoryBean的getObject()返回的对象
   - 要获取当前工厂类对象需 添加 `&`符号