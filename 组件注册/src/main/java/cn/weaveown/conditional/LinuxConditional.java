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
		BeanDefinitionRegistry registry = context.getRegistry();
		boolean containsBeanDefinition = registry.containsBeanDefinition("person02");
		String property = environment.getProperty("os.name");
		if(property.contains("Linux")) {
			return true;
		}
		return false;
	}
}
