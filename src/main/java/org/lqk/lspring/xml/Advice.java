package org.lqk.lspring.xml;
/**
 * 增强
 * @author bert.li
 *
 */
public class Advice {
	protected String method;
	protected String pointcut;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPointcut() {
		return pointcut;
	}
	public void setPointcut(String pointcut) {
		this.pointcut = pointcut;
	}
}
