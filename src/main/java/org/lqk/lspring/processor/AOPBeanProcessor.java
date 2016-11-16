package org.lqk.lspring.processor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.lqk.lspring.factory.AbstractBeanFactory;
import org.lqk.lspring.util.ReflectUtil;
import org.lqk.lspring.xml.Advice;
import org.lqk.lspring.xml.Aspect;
import org.lqk.lspring.xml.Before;

public class AOPBeanProcessor extends AbstractBeanProcessor {
	// 要切入的方法与切入类和方法的映射
	private Map<String, Map<String, List<Advice>>> pointcut2Aspect = new HashMap<String, Map<String, List<Advice>>>();

	public AOPBeanProcessor(AbstractBeanFactory beanFactory) {
		super(beanFactory);
		parseAspect();

	}

	private void parseAspect() {
		List<Aspect> aspects = this.beans.getAspects();
		for (Aspect aspect : aspects) {
			List<Advice> advices = aspect.getAdvices();
			for (Advice advice : advices) {
				String pointcut = advice.getPointcut();
				String ref = aspect.getRef();
				Map<String, List<Advice>> aspect2Advice = pointcut2Aspect.get(pointcut);
				if (MapUtils.isEmpty(aspect2Advice)) {
					aspect2Advice = new HashMap<String, List<Advice>>();
					pointcut2Aspect.put(pointcut, aspect2Advice);
				}
				List<Advice> advcs = aspect2Advice.get(ref);
				if (CollectionUtils.isEmpty(advcs)) {
					advcs = new ArrayList<Advice>();
					aspect2Advice.put(ref, advcs);
				}
				advcs.add(advice);
			}
		}

		display();
	}

	private void display() {
		for (String pointcut : pointcut2Aspect.keySet()) {
			System.out.println("pointcut =================================================>");
			System.out.println("    pointcut ==> " + pointcut);
			Map<String, List<Advice>> aspect2Advice = pointcut2Aspect.get(pointcut);
			for (Object ref : aspect2Advice.keySet()) {
				System.out.println("        ref ==> " + ref);
				List<Advice> advices = aspect2Advice.get(ref);
				for (Advice advice : advices) {
					System.out.println("            type ==> " + advice.getClass().getSimpleName());
					System.out.println("            method ==> " + advice.getMethod());
				}
			}
		}
	}

	@Override
	public void process(String beanId) throws Exception {
		Class clazz = beanId2Clazz.get(beanId);
		for (String pointcut : pointcut2Aspect.keySet()) {
			if (pointcut.startsWith(clazz.getName())) {
				Map<String, List<Advice>> aspect2Advice = pointcut2Aspect.get(pointcut);
				Object obj = beanId2Class.get(beanId); // 元对象
				ProxyHandler proxyHandler = new ProxyHandler(obj, aspect2Advice);
				Object proxyObj = Proxy.newProxyInstance(clazz.getClassLoader(),
						clazz.getInterfaces(), proxyHandler);
				beanId2Class.put(beanId, proxyObj);
				break;
			}
		}
	}

	public class ProxyHandler implements InvocationHandler {
		private Object proxied;	// 元对象
		private Map<String, List<Advice>> aspect2Advice;

		public ProxyHandler(Object proxied, Map<String, List<Advice>> aspect2Advice) {
			this.proxied = proxied;
			this.aspect2Advice = aspect2Advice;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// 处理前置方法
			for (String ref : aspect2Advice.keySet()) {
				List<Advice> advices = aspect2Advice.get(ref);
				Object adviceObj = beanFactory.getBean(ref);
				for (Advice advice : advices) {
					if (advice instanceof Before) {
						ReflectUtil.invokeMethod(adviceObj, advice.getMethod(), null, null);
					}
				}
			}

			// 转调具体目标对象的方法
			return method.invoke(proxied, args);

			// 在转调具体目标对象之后，可以执行一些功能处理
		}
	}

}
