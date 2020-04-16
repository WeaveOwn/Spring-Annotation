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
		System.out.println("Man摧毁前执行");
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("Man初始化");
		
	}
}
