package org.lqk.lspring.xml;

import java.util.ArrayList;
import java.util.List;

public class Bean {
	private String className;
	private String id;
	private String initMethodName;
	private String destroyMethodName;
	private List<Property> props = new ArrayList<Property>();
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Property> getProps() {
		return props;
	}
	public void setProps(List<Property> props) {
		this.props = props;
	}
	public void addProperty(Property property){
		this.props.add(property);
	}
	public String getInitMethodName() {
		return initMethodName;
	}
	public void setInitMethodName(String initMethodName) {
		this.initMethodName = initMethodName;
	}
	public String getDestroyMethodName() {
		return destroyMethodName;
	}
	public void setDestroyMethodName(String destroyMethodName) {
		this.destroyMethodName = destroyMethodName;
	}
	
}	
