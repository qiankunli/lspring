package org.lqk.lspring.xml;

import java.util.ArrayList;
import java.util.List;

public class Beans {
	private List<Bean> beans = new ArrayList<Bean>();
	// 事实上可能有多个scan
	private Scan scan;
	private List<Aspect> aspects = new ArrayList<Aspect>();

	public Scan getScan() {
		return scan;
	}

	public void setScan(Scan scan) {
		this.scan = scan;
	}

	public List<Bean> getBeans() {
		return beans;
	}

	public void setBeans(List<Bean> beans) {
		this.beans = beans;
	}

	public void addBean(Bean bean) {
		this.beans.add(bean);
	}

	public List<Aspect> getAspects() {
		return aspects;
	}

	public void setAspects(List<Aspect> aspects) {
		this.aspects = aspects;
	}

	public void addAspect(Aspect aspect){
		this.aspects.add(aspect);
	}

}
