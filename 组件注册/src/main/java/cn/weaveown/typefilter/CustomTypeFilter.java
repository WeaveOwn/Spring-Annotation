package cn.weaveown.typefilter;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

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
