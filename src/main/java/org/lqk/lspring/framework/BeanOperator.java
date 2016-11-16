package org.lqk.lspring.framework;

import java.util.HashMap;
import java.util.Map;

import org.lqk.lspring.xml.Bean;
import org.lqk.lspring.xml.Beans;

public class BeanOperator {
	// xml反序列化得到的对象
	protected Beans beans = null;
	// 实现id到bean对象的映射
	protected Map<String, Bean> beanId2Bean = null;
	// 实现id到对象的映射
	protected Map<String, Object> beanId2Class = null;
	// 实现id到clazz的映射
	protected Map<String, Class> beanId2Clazz = null;

	public BeanOperator(){}
	public BeanOperator(boolean init) {
		if (init) {
			this.beanId2Bean = new HashMap<String, Bean>();
			this.beanId2Class = new HashMap<String, Object>();
			this.beanId2Clazz = new HashMap<String, Class>();
		}
	}

	public Map<String, Bean> getBeanId2Bean() {
		return beanId2Bean;
	}

	public void setBeanId2Bean(Map<String, Bean> beanId2Bean) {
		this.beanId2Bean = beanId2Bean;
	}

	public Map<String, Object> getBeanId2Class() {
		return beanId2Class;
	}

	public void setBeanId2Class(Map<String, Object> beanId2Class) {
		this.beanId2Class = beanId2Class;
	}

	public Map<String, Class> getBeanId2Clazz() {
		return beanId2Clazz;
	}

	public void setBeanId2Clazz(Map<String, Class> beanId2Clazz) {
		this.beanId2Clazz = beanId2Clazz;
	}
	public Beans getBeans() {
		return beans;
	}
	public void setBeans(Beans beans) {
		this.beans = beans;
	}
	
}
