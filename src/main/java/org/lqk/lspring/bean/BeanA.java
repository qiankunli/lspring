package org.lqk.lspring.bean;

public class BeanA extends BeanAB{
	
	private BeanB beanB;

	public BeanB getBeanB() {
		return beanB;
	}

	public void setBeanB(BeanB beanB) {
		this.beanB = beanB;
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("BeanA.afterPropertiesSet()");
	}

	public void destroy() throws Exception {
		System.out.println("BeanA.destroy()");
	}

	public void init() {
		System.out.println("BeanA.init()");
	}

	public void close() {
		System.out.println("BeanA.close()");
	}
}
