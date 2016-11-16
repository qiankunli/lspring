package org.lqk.lspring;

import org.junit.Test;
import org.lqk.lspring.bean.BeanA;
import org.lqk.lspring.bean.BeanB;
import org.lqk.lspring.factory.FacadeBeanFactory;

public class TestFactoryBean {
	@Test
	public void testFactoryBean() throws Exception {
		System.out
				.println("test=================================================>");
		FacadeBeanFactory factory = new FacadeBeanFactory();
		BeanB beanB = (BeanB) factory.getBean("beanB");
		System.out.println(beanB.getTitle());
		BeanA beanA = (BeanA) factory.getBean("beanA");
		System.out.println(beanA.getTitle());
		System.out.println(beanA.getBeanB().getTitle());
		
		BeanB beanBF = (BeanB) factory.getBean("beanBF");
		System.out.println(beanBF.getClass().getName());
		
	}

}
