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
