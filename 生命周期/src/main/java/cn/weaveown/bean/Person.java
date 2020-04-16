package cn.weaveown.bean;

public class Person  {

	public Person() {
		System.out.println("Person创建");
	}
	public void init() {
		System.out.println("Person初始化");
	}
	
	public void destroy() {
		System.out.println("Person摧毁前执行");
	}
}
