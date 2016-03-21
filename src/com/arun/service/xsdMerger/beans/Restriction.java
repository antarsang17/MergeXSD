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
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.w3c.dom.Element;

import com.freddiemac.service.xsdMerger.core.MergeUtils;
import com.freddiemac.service.xsdMerger.core.TRAKKER;
import com.freddiemac.service.xsdMerger.core.MergeUtils.SCHEMAS;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM_SIMPLE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject.MERGE_IN_REF;

/**
 * @author c38847
 *
 */
@XmlRootElement(name="restriction",namespace="http://www.w3.org/2001/XMLSchema")
@XmlAccessorType(XmlAccessType.FIELD)
public class Restriction implements Mergable<Restriction>{

	@XmlAttribute
	private String base;
	
	@XmlElement(name="enumeration",namespace="http://www.w3.org/2001/XMLSchema")
	private List<Enumeration> enumerations;
	
	@XmlAnyElement(lax=true)
	private List<org.w3c.dom.Element> otherRestrictions;
	
	@XmlTransient
	private List<AnyElement> otrRestrictions;
	
	@XmlTransient
	private String parentName;
	
	/**
	 * @return the base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * @return the enumerations
	 */
	public List<Enumeration> getEnumerations() {
		return enumerations;
	}

	/**
	 * @param enumerations the enumerations to set
	 */
	public void setEnumerations(List<Enumeration> enumerations) {
		this.enumerations = enumerations;
	}
	
	/**
	 * @return the otherRestrictions
	 */
	public List<org.w3c.dom.Element> getOtherRestrictions() {
		return otherRestrictions;
	}

	/**
	 * @param otherRestrictions the otherRestrictions to set
	 */
	public void setOtherRestrictions(List<org.w3c.dom.Element> otherRestrictions) {
		this.otherRestrictions = otherRestrictions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result
				+ ((enumerations == null) ? 0 : enumerations.hashCode());
		result = prime * result
				+ ((getOtrRestrictions() == null) ? 0 : getOtrRestrictions().hashCode());
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
		Restriction other = (Restriction) obj;
		if (base == null) {
			if (other.base != null)
				return false;
		} else if (!base.equals(other.base))
			return false;
		if (enumerations == null) {
			if (other.enumerations != null)
				return false;
		} else if (!enumerations.equals(other.enumerations))
			return false;
		if (getOtrRestrictions() == null) {
			if (other.getOtrRestrictions() != null)
				return false;
		} else if (!getOtrRestrictions().equals(other.getOtrRestrictions()))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Restriction [base=" + base + ", enumerations=" + enumerations+ ", otherRestrictions=" + getOtrRestrictions()
				+ "]";
	}

	/**
	 * @return the otrRestrictions
	 */
	public List<AnyElement> getOtrRestrictions() {
		if(null == otrRestrictions)updateOtrRestrictions();
		return otrRestrictions;
	}
	
	private void updateOtrRestrictions(){
		otrRestrictions = new ArrayList<AnyElement>();
		if(null!=otherRestrictions)
		for(Element element:otherRestrictions){
			getOtrRestrictions().add(new AnyElement(element.getNodeName(), element.getAttribute("value"))); 
		}
	}

	/**
	 * @param otrRestrictions the otrRestrictions to set
	 */
	public void setOtrRestrictions(List<AnyElement> otrRestrictions) {
		this.otrRestrictions = otrRestrictions;
	}

	@Override
	public List<Restriction> merge(Restriction object,TRAKKER trakker) {
		
		List<Restriction> merged = new ArrayList<Restriction>();
		if(this.base.equals(object.base)){
			Restriction restriction=new Restriction();
			restriction.setBase(base);
			
			Set<Enumeration> superSetKeys = new HashSet<Enumeration>();
			if(null!=enumerations)superSetKeys.addAll(enumerations);
		    if(null!=object.enumerations)superSetKeys.addAll(object.enumerations);
		    restriction.setEnumerations(new ArrayList<Enumeration>(superSetKeys));
		    Collections.sort(restriction.getEnumerations());
		    
		    MergeUtils.trackMerge(enumerations, object.enumerations, superSetKeys, trakker, MERGE_ITEM.SIMPLE_TYPE, MERGE_ITEM_SIMPLE.ENUMERATION.toString(),parentName);
		    
		    Set<AnyElement> superSetOther = new HashSet<AnyElement>();
		    if(null!=getOtrRestrictions())superSetOther.addAll(getOtrRestrictions());
		    if(null!=object.getOtrRestrictions())superSetOther.addAll(object.getOtrRestrictions());
		    
		    restriction.setOtrRestrictions(new ArrayList<AnyElement>(MergeUtils.handleDuplicateAnyElememts(getOtrRestrictions(),object.getOtrRestrictions())));
			Collections.sort(restriction.getOtrRestrictions());
			
		    
		    MergeUtils.trackMerge(getOtrRestrictions(), object.getOtrRestrictions(), superSetOther, trakker, MERGE_ITEM.SIMPLE_TYPE, MERGE_ITEM_SIMPLE.OTHER_ITEMS.toString(),parentName);
			
			merged.add(restriction);
		}else{
			merged.add(this);
			merged.add(object);
			trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.SIMPLE_TYPE, MERGE_ITEM_SIMPLE.RESTRICTION.toString(), MERGE_IN_REF.BOTH,this.base+","+object.base));
		}
		
		return merged;
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

}
