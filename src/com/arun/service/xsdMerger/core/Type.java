package com.freddiemac.service.xsdMerger.core;

import java.util.List;

public class Type {

	private String typeName;
	private List<Type> types;
	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * @return the types
	 */
	public List<Type> getTypes() {
		return types;
	}
	/**
	 * @param types the types to set
	 */
	public void setTypes(List<Type> types) {
		this.types = types;
	}
}
