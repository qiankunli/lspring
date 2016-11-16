package org.lqk.lspring.factory;

import org.lqk.lspring.framework.BeanOperator;

public abstract class AbstractBeanFactory extends BeanOperator implements BeanFactory{
	
	public AbstractBeanFactory(){}
	public AbstractBeanFactory(boolean init){
		super(true);
	}
	public abstract Object getBean(String beanId) throws Exception;
	public abstract void load() throws Exception;
}
