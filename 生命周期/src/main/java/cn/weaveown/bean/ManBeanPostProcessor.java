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
