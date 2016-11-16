package org.lqk.lspring.processor;

public interface BeanProcessor {
	void process(String beanId) throws Exception;
	void process() throws Exception;
}
