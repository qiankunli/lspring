package org.lqk.lspring.factory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.lqk.lspring.annotation.Component;
import org.lqk.lspring.annotation.Value;
import org.lqk.lspring.util.StringUtil;
import org.lqk.lspring.xml.Scan;

public class AnnotationBeanFactory extends AbstractBeanFactory{
	
	private AbstractBeanFactory parentBeanFactory = null;
	
	public AnnotationBeanFactory(AbstractBeanFactory parentBeanFactory){
		this.parentBeanFactory = parentBeanFactory;
		this.beanId2Bean = parentBeanFactory.getBeanId2Bean();
		this.beanId2Clazz = parentBeanFactory.getBeanId2Clazz();
		this.beanId2Class = parentBeanFactory.getBeanId2Class();
		this.beans = parentBeanFactory.getBeans();
	}

	public void load() throws Exception {
		Scan scan = this.beans.getScan();
		if (null == scan) {
			return;
		}
		String packageName = scan.getPackageName();

		String rootPath = FacadeBeanFactory.class.getResource("/").getPath();
		File file = new File(rootPath + File.separator + packageName.replace(".", File.separator));
		System.out.println(file.getAbsolutePath());
		String[] fileNames = file.list();

		for (String fileName : fileNames) {
			String className = fileName.substring(0, fileName.length() - ".class".length());
			String fullClassName = packageName + "." + className;
			Class clazz = Class.forName(fullClassName);
			Component cop = (Component) clazz.getAnnotation(Component.class);
			if (null != cop) {
				String beanId = cop.value();
				if (StringUtils.isEmpty(beanId)) {
					beanId = StringUtil.lowerCaseFirstChar(className);
				}
				beanId2Clazz.put(beanId, clazz);
			}
		}
	}
	
	public Object getBean(String beanId) throws Exception {
		Class clazz = beanId2Clazz.get(beanId);
		if (null == clazz) {
			return null;
		}
		Object obj = beanId2Class.get(beanId);
		if (obj == null) {
			obj = clazz.newInstance();
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String methodName = StringUtil.setMethodName(field.getName());
			// method的获取，写在这里不保险，因为可能没有
//			Method m = clazz.getMethod(methodName, field.getType());
			Value v = field.getAnnotation(Value.class);
			if (null != v) {
				String propertyValue = v.value();
				Method m = clazz.getMethod(methodName, field.getType());
				m.invoke(obj, propertyValue);
			}
			Resource r = field.getAnnotation(Resource.class);
			if (null != r) {
				String propertyBeanId = r.name();
				if (StringUtils.isEmpty(propertyBeanId)) {
					propertyBeanId = StringUtil.lowerCaseFirstChar(field
							.getType().getSimpleName());
				}
				Object propertyObj = parentBeanFactory.getBean(propertyBeanId);
				Method m = clazz.getMethod(methodName, field.getType());
				m.invoke(obj, propertyObj);
			}

		}
		if (obj != null) {
			beanId2Class.put(beanId, obj);
		}
		return obj;
	}
}
