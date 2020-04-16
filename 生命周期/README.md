# Spring注解开发-全面解析常用注解使用方法之生命周期

## bean生命周期

​	创建------>初始化------->销毁。容器统一管理bean的生命周期，我们也可以自定义初始化和销毁方法；容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法。

## 流程：

1. 构造（对象创建）

- 单实例：在容器启动的时候创建对象
- 多实例：在每次获取的时候创建对象


2. BeanPostProcessor.postProcessBeforeInitialization

   ​	ps:遍历得到容器中所有的BeanPostProcessor；挨个执行beforeInitialization，一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization

3. 初始化： 对象创建完成，并赋值好，调用初始化方法。。。

4. BeanPostProcessor.postProcessAfterInitialization

5. 销毁：

   - 单实例：容器关闭的时候
   - 多实例：容器不会管理这个bean；容器不会调用销毁方法；   

## BeanPostProcessor原理

 以下下方法依次执行：

   `populateBean(beanName, mbd, instanceWrapper);`给bean进行属性赋值

​    initializeBean

   {

  	`applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);`初始化前执行

  	`invokeInitMethods(beanName, wrappedBean, mbd);`执行自定义初始化

  	`applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);`初始化后执行

  }

## 使用方法：

1. 指定初始化和销毁方法；通过@Bean指定initMethod和destroyMethod；

   例：`@Bean(initMethod="init",destroyMethod="destroy")`

2. 通过让Bean实现InitializingBean（定义初始化逻辑），DisposableBean（定义销毁逻辑）

   ```java
   package cn.weaveown.bean;

   import org.springframework.beans.factory.DisposableBean;
   import org.springframework.beans.factory.InitializingBean;
   import org.springframework.stereotype.Component;

   @Component
   public class Man implements InitializingBean,DisposableBean{

   	public Man() {
   		System.out.println("Man创建");
   	}
   	
   	
   	public void destroy() {
   		System.out.println("Man摧毁");
   	}

   	public void afterPropertiesSet() throws Exception {
   		System.out.println("Man初始化");
   		
   	}
   }
   ```

3. 可以使用JSR250；

   1. @PostConstruct：在bean创建完成并且属性赋值完成；来执行初始化方法
   2. @PreDestroy：在容器销毁bean之前通知我们进行清理工作

   ```JAVA
   package cn.weaveown.bean;
   import javax.annotation.PostConstruct;
   import javax.annotation.PreDestroy;

   import org.springframework.stereotype.Component;
   @Component
   public class Woman {

   	public Woman() {
   		System.out.println("woman创建");
   	}
   	@PreDestroy
   	public void destroy() {
   		System.out.println("Woman摧毁前执行");
   	}
   	@PostConstruct
   	public void init(){
   		System.out.println("Woman初始化");
   		
   	}
   }
   ```

   ​

4. BeanPostProcessor【interface】：bean的后置处理器；在bean初始化前后进行一些处理工作

   1. postProcessBeforeInitialization:在初始化之前工作
   2. postProcessAfterInitialization:在初始化之后工作

   ```JAVA
   package cn.weaveown.bean;

   import org.springframework.beans.BeansException;
   import org.springframework.beans.factory.config.BeanPostProcessor;
   import org.springframework.stereotype.Component;
   @Component
   public class ManBeanPostProcessor implements BeanPostProcessor{


   	public Object postProcessAfterInitialization(Object arg0, String arg1) throws BeansException {
   		if(arg1.equals("man")) {
   			System.out.println("----------------------BeanPostProcessor初始化后执行");
   		}
   		return arg0;
   	}

   	public Object postProcessBeforeInitialization(Object arg0, String arg1) throws BeansException {
   		if(arg1.equals("man")) {
   			System.out.println("---------------------BeanPostProcessor初始化前");
   		}
   		return arg0;
   	}
   }

   ```

   ##Spring底层对 BeanPostProcessor 的使用

    	1. bean赋值
   	2. 注入其他组件
   	3. @Autowired
   	4. 生命周期注解功能
   	5. @Async,xxx
