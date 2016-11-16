package org.lqk.lspring.processor;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.lqk.lspring.factory.AbstractBeanFactory;
import org.lqk.lspring.framework.InitializingBean;
import org.lqk.lspring.xml.Bean;

public class InitializingBeanProcessor extends AbstractBeanProcessor {

	public InitializingBeanProcessor(AbstractBeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	public void process(String beanId) throws Exception {
		Bean bean = beanId2Bean.get(beanId);
		Class clazz = beanId2Clazz.get(beanId);
		Object obj = beanId2Class.get(beanId);

		// 执行init-method
		String initMethodName = bean.getInitMethodName();
		if (StringUtils.isNotEmpty(initMethodName)) {
			Method m = clazz.getMethod(initMethodName, null);
			m.invoke(obj, null);
		}

		// 执行afterPropertiesSet方法
		Class[] intfs = clazz.getInterfaces();
		for (Class intf : intfs) {
			if (InitializingBean.class.getSimpleName().equals(intf.getSimpleName())) {
				Method m = clazz.getMethod("afterPropertiesSet", null);
				m.invoke(obj, null);
				break;
			}
		}
	}

}
