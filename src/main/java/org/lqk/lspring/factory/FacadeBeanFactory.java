package org.lqk.lspring.factory;

import java.util.Arrays;
import java.util.List;

import org.lqk.lspring.bean.BeanB;
import org.lqk.lspring.bean.InterfaceB;
import org.lqk.lspring.factory.FacadeBeanFactory;
import org.lqk.lspring.processor.AOPBeanProcessor;
import org.lqk.lspring.processor.AbstractBeanProcessor;
import org.lqk.lspring.processor.DisposableBeanProcessor;
import org.lqk.lspring.processor.FactoryBeanProcessor;
import org.lqk.lspring.processor.InitializingBeanProcessor;

public class FacadeBeanFactory extends AbstractBeanFactory {

	private Thread shutdownHook;
	private XmlBeanFactory xmlBeanFactory;
	private AnnotationBeanFactory annotationBeanFactory;
	private List<AbstractBeanProcessor> beanProcessors;

	public FacadeBeanFactory() {
		// true表示初始化三个map
		super(true);
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void load() throws Exception {
		// 这个初始化的顺序是不能乱的
		xmlBeanFactory = new XmlBeanFactory(this);
		xmlBeanFactory.load();
		annotationBeanFactory = new AnnotationBeanFactory(this);
		annotationBeanFactory.load();
		beanProcessors = Arrays.asList(new InitializingBeanProcessor(this), 
				new FactoryBeanProcessor(this),new AOPBeanProcessor(this));
		registerShutdownHook();
	}

	public void registerShutdownHook() {
		if (this.shutdownHook == null) {
			// No shutdown hook registered yet.
			this.shutdownHook = new Thread() {
				@Override
				public void run() {
					destroyBeans();
				}
			};
			Runtime.getRuntime().addShutdownHook(this.shutdownHook);
		}
	}

	protected void destroyBeans() {
		try {
			new DisposableBeanProcessor(this).process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getBean(String beanId) throws Exception {
		Object obj = beanId2Class.get(beanId);
		if (obj != null) {
			return obj;
		}
		obj = xmlBeanFactory.getBean(beanId);
		obj = annotationBeanFactory.getBean(beanId);

		for (AbstractBeanProcessor beanProcessor : beanProcessors) {
			beanProcessor.process(beanId);
		}

		return beanId2Class.get(beanId);
	}

	public static void main(String[] args) throws Exception {
		System.out.println("test=================================================>");
		FacadeBeanFactory factory = new FacadeBeanFactory();
		InterfaceB beanB = (InterfaceB) factory.getBean("beanB");
		beanB.run();
		// dispose bean时 obj 为null
		
		
//		System.out.println(beanB.getTitle());
//		BeanA beanA = (BeanA) factory.getBean("beanA");
//		System.out.println(beanA.getTitle());
//		System.out.println(beanA.getBeanB().getTitle());

	}

}
