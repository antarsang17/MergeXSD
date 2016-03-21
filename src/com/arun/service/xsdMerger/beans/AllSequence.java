/**
 * 
 */
package com.freddiemac.service.xsdMerger.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.freddiemac.service.xsdMerger.core.MergeUtils;
import com.freddiemac.service.xsdMerger.core.MergeUtils.SCHEMAS;
import com.freddiemac.service.xsdMerger.core.TRAKKER;
import com.freddiemac.service.xsdMerger.core.TrakkerObject;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM_COMPLEX;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM_SIMPLE;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject.MERGE_IN_REF;

/**
 * @author c38847
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AllSequence implements Mergable<AllSequence>{

    @XmlElement(name="element",namespace="http://www.w3.org/2001/XMLSchema")
    private List<Element> elements;
    
    @XmlTransient
    private String parentName;
    
    @XmlTransient
    private SCHEMAS schemaID;

	/**
	 * @return the elements
	 */
	public List<Element> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(this instanceof AllSequence) || !(obj instanceof AllSequence))
			return false;
		AllSequence other = (AllSequence) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}
	
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Override
	public List<AllSequence> merge(AllSequence object,TRAKKER trakker) {
		// TODO Auto-generated method stub
		
		Set<Element> superSetKeys = new HashSet<Element>(elements);
		superSetKeys.addAll(object.elements);
		
	    final AllSequence sequence = new AllSequence();
		sequence.setElements(new ArrayList<Element>(MergeUtils.handleDuplicateElememts(elements,object.elements,trakker,getParentName())));
		Collections.sort(sequence.getElements());
		
		MergeUtils.trackMerge(elements, object.elements, superSetKeys, trakker, MERGE_ITEM.COMPLEX_TYPE, MERGE_ITEM_COMPLEX.ALL_SEQUENCE.toString(),getParentName());
	    
		return new ArrayList<AllSequence>(){{add(sequence);}};
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
