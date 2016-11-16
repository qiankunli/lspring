package org.lqk.lspring.factory;

public interface BeanFactory {
	public Object getBean(String beanId) throws Exception;
	public void load() throws Exception;
}
