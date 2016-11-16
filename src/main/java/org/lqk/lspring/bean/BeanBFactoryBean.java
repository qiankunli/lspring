package org.lqk.lspring.bean;

import org.lqk.lspring.framework.DisposableBean;
import org.lqk.lspring.framework.FactoryBean;
import org.lqk.lspring.framework.InitializingBean;

public class BeanBFactoryBean implements FactoryBean<BeanB>,InitializingBean,DisposableBean {

	private BeanB beanB;

	public BeanB getObject() throws Exception {
		if (beanB == null) {  
	         synchronized (this) {  
	            if (beanB == null) {  
	            	BeanB beanB = new BeanB();  
	            	beanB.setTitle("studentB");
	            	// 为什么要这样做
	                this.beanB = beanB;  
	            }  
	         }  
	      }  
	      return beanB;  
	}

	public Class<BeanB> getObjectType() {
		return BeanB.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void destroy() throws Exception {
		System.out.println("BeanBFactoryBean.destroy()");
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("BeanBFactoryBean.afterPropertiesSet()");
	}
}
