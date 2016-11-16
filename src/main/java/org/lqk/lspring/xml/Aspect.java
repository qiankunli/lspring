package org.lqk.lspring.xml;

import java.util.ArrayList;
import java.util.List;

public class Aspect {
	private String ref;
	private String id;
	private List<Advice> advices = new ArrayList<Advice>();

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public List<Advice> getAdvices() {
		return advices;
	}

	public void setAdvices(List<Advice> advices) {
		this.advices = advices;
	}

	public void addAdvice(Advice advice){
		this.advices.add(advice);
	}
}
