package org.lqk.lspring.processor;

import java.lang.reflect.Method;

import org.lqk.lspring.factory.AbstractBeanFactory;
import org.lqk.lspring.framework.FactoryBean;
import org.lqk.lspring.util.StringUtil;
import org.lqk.lspring.xml.Bean;

public class FactoryBeanProcessor extends AbstractBeanProcessor{

	public FactoryBeanProcessor(AbstractBeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	public void process(String beanId) throws Exception {
		
		Bean bean = beanId2Bean.get(beanId);
		Class clazz = beanId2Clazz.get(beanId);
		Object obj = beanId2Class.get(beanId);
		
		Class[] intfs = clazz.getInterfaces();
		for (Class intf : intfs) {
			if(FactoryBean.class.getSimpleName().equals(intf.getSimpleName())){
				// 替换beanId对应的对象
				Method m = clazz.getMethod("getObject", null);
				Object targetObj = m.invoke(obj, null);
				beanId2Class.put(bean.getId(), targetObj);
				// 替换beanId中的clazz
				Method m2 = clazz.getMethod("getObjectType", null);
				Object targetClazz = m2.invoke(obj, null);
				beanId2Clazz.put(bean.getId(), (Class)targetClazz);
				// 暂时先不处理Bean对象
				beanId2Bean.remove(bean.getId());
				
				// 对于工厂bean，InitializingBean方法执行的时候，beanId还不是factoryBeanId
				// 这样不影响工厂bean本身的DisposableBean接口的处理（InitializingBean接口已处理过）
				String factoryBeanId = StringUtil.lowerCaseFirstChar(clazz.getSimpleName());
				beanId2Clazz.put(factoryBeanId, clazz);
				beanId2Class.put(factoryBeanId, obj);
				beanId2Bean.put(factoryBeanId, bean);
				break;
			}
		}
	}


	
	
}
