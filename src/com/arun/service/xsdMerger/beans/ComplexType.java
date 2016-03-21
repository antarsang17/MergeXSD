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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.freddiemac.service.xsdMerger.core.TRAKKER;
import com.freddiemac.service.xsdMerger.core.MergeUtils.SCHEMAS;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM_COMPLEX;
import com.freddiemac.service.xsdMerger.core.TrakkerObject;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject.MERGE_IN_REF;

/**
 * @author c38847
 *
 */

@XmlRootElement(name="complexType",namespace="http://www.w3.org/2001/XMLSchema")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "complexType", propOrder = {"name","allSequence"}) 
public class ComplexType implements Mergable<ComplexType>{
	
	@XmlAttribute
	private String name;
	
	
	@XmlElements({
		@XmlElement(name = "all",namespace="http://www.w3.org/2001/XMLSchema"),
        @XmlElement(name = "sequence",namespace="http://www.w3.org/2001/XMLSchema")
    })
	private AllSequence allSequence;
	
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
	 * @return the allSequence
	 */
	public AllSequence getAllSequence() {
		return allSequence;
	}

	/**
	 * @param allSequence the allSequence to set
	 */
	public void setAllSequence(AllSequence allSequence) {
		this.allSequence = allSequence;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((allSequence == null) ? 0 : allSequence.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexType other = (ComplexType) obj;
		if (allSequence == null) {
			if (other.allSequence != null)
				return false;
		} else if (!allSequence.equals(other.allSequence))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public List<ComplexType> merge(ComplexType object,TRAKKER trakker) {
		// TODO Auto-generated method stub
		List<ComplexType> merged = new ArrayList<ComplexType>();
		if(this.name.equals(object.name)){
			ComplexType type=new ComplexType();
			type.setName(name);
			
			allSequence.setParentName(this.name);
			object.allSequence.setParentName(object.name);
			
			this.allSequence.setSchemaID(SCHEMAS.SCHEMA1);
			object.allSequence.setSchemaID(SCHEMAS.SCHEMA2);
			
			type.setAllSequence(this.allSequence.merge(object.allSequence,trakker).get(0));
			merged.add(type);
			
		}else{
			merged.add(this);
			merged.add(object);
			trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.COMPLEX_TYPE, "\t", MERGE_IN_REF.BOTH,this.name+","+object.name));
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
