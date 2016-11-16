package org.lqk.lspring.processor;

import org.lqk.lspring.factory.AbstractBeanFactory;
import org.lqk.lspring.framework.BeanOperator;

public abstract class AbstractBeanProcessor extends BeanOperator implements BeanProcessor {
	protected AbstractBeanFactory beanFactory;
	public AbstractBeanProcessor(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		this.beanId2Bean = beanFactory.getBeanId2Bean();
		this.beanId2Class = beanFactory.getBeanId2Class();
		this.beanId2Clazz = beanFactory.getBeanId2Clazz();
		this.beans = beanFactory.getBeans();
	}

	public void process(String beanId) throws Exception {
	}

	public void process() throws Exception {
	}
}
