package org.lqk.lspring.factory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.lqk.lspring.util.ReflectUtil;
import org.lqk.lspring.util.StringUtil;
import org.lqk.lspring.xml.Advice;
import org.lqk.lspring.xml.Aspect;
import org.lqk.lspring.xml.Bean;
import org.lqk.lspring.xml.Beans;
import org.lqk.lspring.xml.Before;
import org.lqk.lspring.xml.Property;
import org.lqk.lspring.xml.Scan;

public class XmlBeanFactory extends AbstractBeanFactory{

	private AbstractBeanFactory parentBeanFactory = null;
	public XmlBeanFactory(AbstractBeanFactory parentBeanFactory){
		this.parentBeanFactory = parentBeanFactory;
		this.beanId2Bean = parentBeanFactory.getBeanId2Bean();
		this.beanId2Clazz = parentBeanFactory.getBeanId2Clazz();
		this.beanId2Class = parentBeanFactory.getBeanId2Class();
	}

	public void load() throws Exception {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("beans", Beans.class);

		// 如果配置文件中有多个bean，add一次即可
		digester.addObjectCreate("beans/bean", Bean.class);

		// 设置bean的属性<bean name="",id="">中的id和name。默认属性名和类中的属性名一样，不同的要特殊配置
		digester.addSetProperties("beans/bean", "class", "className");
		digester.addSetProperties("beans/bean", "init-method", "initMethodName");
		digester.addSetProperties("beans/bean", "destroy-method", "destroyMethodName");
		digester.addSetProperties("beans/bean");

		digester.addObjectCreate("beans/bean/property", Property.class);
		digester.addSetProperties("beans/bean/property");

		digester.addObjectCreate("beans/scan", Scan.class);
		digester.addSetProperties("beans/scan", "package", "packageName");

		digester.addObjectCreate("beans/aspect", Aspect.class);
		digester.addSetProperties("beans/aspect");

		digester.addObjectCreate("beans/aspect/before", Before.class);
		digester.addSetProperties("beans/aspect/before");

		// 设置对象间的关系
		digester.addSetNext("beans/bean/property", "addProperty");
		digester.addSetNext("beans/aspect/before", "addAdvice");
		digester.addSetNext("beans/bean", "addBean");
		digester.addSetNext("beans/scan", "setScan");
		digester.addSetNext("beans/aspect", "addAspect");
		

		InputStream in = ClassLoader.getSystemResourceAsStream("beans.xml");
		this.beans = (Beans) digester.parse(in);
		List<Bean> beanList = beans.getBeans();
		for (Bean bean : beanList) {
			System.out.println("bean =================================================>");
			System.out.println("    id ==> " + bean.getId());
			List<Property> props = bean.getProps();
			for (Property prop : props) {
				System.out.println("    property =================================================>");
				System.out.println("        name ==> " + prop.getName());
				System.out.println("        ref ==> " + prop.getRef());
				System.out.println("        value ==> " + prop.getValue());
			}
			beanId2Bean.put(bean.getId(), bean);

			Class clazz = Class.forName(bean.getClassName());
			beanId2Clazz.put(bean.getId(), clazz);
		}

		Scan scan = beans.getScan();
		if (null != scan) {
			System.out.println("scan =================================================>");
			System.out.println("    package name ==> " + scan.getPackageName());
		}
		
		List<Aspect> aspects = beans.getAspects();
		for(Aspect aspect : aspects){
			System.out.println("aspect =================================================>");
			System.out.println("    aspect ref ==> " + aspect.getRef());
			for(Advice advice : aspect.getAdvices()){
				System.out.println("        advice method ==> " + advice.getMethod());
			}
		}
		parentBeanFactory.setBeans(beans);
	}
	
	public Object getBean(String beanId) throws Exception {

		Bean bean = beanId2Bean.get(beanId);
		if (null == bean) {
			return null;
		}
		Class clazz = beanId2Clazz.get(beanId);

		Object obj = beanId2Class.get(beanId);
		if (obj == null) {
			obj = clazz.newInstance();
		}

		List<Property> props = bean.getProps();
		for (Property prop : props) {
			String propertyName = prop.getName();
//			Field field = clazz.getDeclaredField(propertyName);
			Field field = ReflectUtil.getDeclaredField(obj, propertyName);
			String methodName = StringUtil.setMethodName(propertyName);
//			Method m = clazz.getMethod(methodName, field.getType());
			Method m = ReflectUtil.getDeclaredMethod(obj, methodName, field.getType());
			if (null != prop.getValue()) {
				// 其实这里应该尝试进行 prop.getValue() 到 field.getType()的转换
				m.invoke(obj, prop.getValue());
			}
			if (null != prop.getRef()) {
				Object childObj = parentBeanFactory.getBean(prop.getRef());
				m.invoke(obj, childObj);
			}
		}
		if (obj != null) {
			beanId2Class.put(beanId, obj);
		}
		return obj;

	}
}
