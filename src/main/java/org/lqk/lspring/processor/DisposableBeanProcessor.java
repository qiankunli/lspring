package org.lqk.lspring.processor;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.lqk.lspring.factory.AbstractBeanFactory;
import org.lqk.lspring.framework.DisposableBean;
import org.lqk.lspring.xml.Bean;

public class DisposableBeanProcessor extends AbstractBeanProcessor {

	public DisposableBeanProcessor(AbstractBeanFactory beanFactory) {
		super(beanFactory);
	}

	public void doProcess(Object obj, Bean bean, Class clazz) throws Exception {
		// 执行destroy-method
		if (null != bean) {
			String destroyMethodName = bean.getDestroyMethodName();
			if (StringUtils.isNotEmpty(destroyMethodName)) {
				Method m = clazz.getMethod(destroyMethodName, null);
				m.invoke(obj, null);
			}
		}

		// 执行destroy
		if (null != clazz) {
			Class[] intfs = clazz.getInterfaces();
			for (Class intf : intfs) {
				if (DisposableBean.class.getSimpleName().equals(intf.getSimpleName())) {
					Method m = clazz.getMethod("destroy", null);
					m.invoke(obj, null);
					break;
				}
			}
		}
	}

	@Override
	public void process() throws Exception {
		// 获取beanId2Class，只有那些真正实例化的才会被销毁
		for (String key : beanId2Class.keySet()) {
			Object obj = beanId2Class.get(key);
			Bean bean = beanId2Bean.get(key);
			Class clazz = beanId2Clazz.get(key);
			doProcess(obj, bean, clazz);
		}
	}
}
