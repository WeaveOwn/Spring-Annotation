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
