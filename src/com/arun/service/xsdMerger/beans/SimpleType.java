/**
 * 
 */
package com.freddiemac.service.xsdMerger.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.freddiemac.service.xsdMerger.core.TRAKKER;
import com.freddiemac.service.xsdMerger.core.TrakkerObject;
import com.freddiemac.service.xsdMerger.core.MergeUtils.SCHEMAS;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject.MERGE_IN_REF;

/**
 * @author c38847
 *
 */

@XmlRootElement(name="simpleType",namespace="http://www.w3.org/2001/XMLSchema")
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleType implements Mergable<SimpleType>{
	
	@XmlAttribute
	private String name;
	
	@XmlElement(namespace="http://www.w3.org/2001/XMLSchema")
	private Restriction restriction;
	
	@XmlTransient
	private SCHEMAS schemaID;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the restriction
	 */
	public Restriction getRestriction() {
		return restriction;
	}

	/**
	 * @param restriction the restriction to set
	 */
	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((restriction == null) ? 0 : restriction.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleType other = (SimpleType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (restriction == null) {
			if (other.restriction != null)
				return false;
		} else if (!restriction.equals(other.restriction))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleType [name=" + name + ", restriction=" + restriction
				+ "]";
	}

	@Override
	public List<SimpleType> merge(SimpleType object,TRAKKER trakker) {
		// TODO Auto-generated method stub
		List<SimpleType> merged = new ArrayList<SimpleType>();
		if(this.name.equals(object.name)){
			SimpleType type=new SimpleType();
			type.setName(name);
			restriction.setParentName(name);
			object.restriction.setParentName(object.name);
			type.setRestriction(this.restriction.merge(object.restriction,trakker).get(0));
			merged.add(type);
		}else{
			merged.add(this);
			merged.add(object);
			trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.SIMPLE_TYPE, "\t", MERGE_IN_REF.BOTH,this.name+","+object.name));
		}
		
		return merged;
	}

	/**
	 * @return the schemaID
	 */
	public SCHEMAS getSchemaID() {
		return schemaID;
	}

	/**
	 * @param schemaID the schemaID to set
	 */
	public void setSchemaID(SCHEMAS schemaID) {
		this.schemaID = schemaID;
	}
	
	
}
