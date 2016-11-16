package org.lqk.lspring.bean;

public class BeanB extends BeanAB implements InterfaceB{
	public BeanB(){
		super();
	}
	
	public void run(){
		System.out.println("BeanB.run()");
	}
	
}
