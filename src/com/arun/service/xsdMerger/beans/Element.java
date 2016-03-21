/**
 * 
 */
package com.freddiemac.service.xsdMerger.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author c38847
 *
 */
@XmlRootElement(name="element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Element implements Comparable<Element>{

	@XmlAttribute
	private String maxOccurs;
	@XmlAttribute
	private String minOccurs;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String type;
	/**
	 * @return the maxOccurs
	 */
	public String getMaxOccurs() {
		return maxOccurs;
	}
	/**
	 * @param maxOccurs the maxOccurs to set
	 */
	public void setMaxOccurs(String maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	/**
	 * @return the minOccurs
	 */
	public String getMinOccurs() {
		return minOccurs;
	}
	/**
	 * @param minOccurs the minOccurs to set
	 */
	public void setMinOccurs(String minOccurs) {
		this.minOccurs = minOccurs;
	}
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((maxOccurs == null) ? 0 : maxOccurs.hashCode());
		result = prime * result
				+ ((minOccurs == null) ? 0 : minOccurs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Element other = (Element) obj;
		if (maxOccurs == null) {
			if (other.maxOccurs != null)
				return false;
		} else if (!maxOccurs.equals(other.maxOccurs))
			return false;
		if (minOccurs == null) {
			if (other.minOccurs != null)
				return false;
		} else if (!minOccurs.equals(other.minOccurs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public int compareTo(Element o) {
		// TODO Auto-generated method stub
		int compare = this.name.compareTo(o.name);
		
		if(compare == 0){
			compare = this.minOccurs.compareTo(o.minOccurs);
			if(compare == 0){
				compare = this.maxOccurs.compareTo(o.maxOccurs);
				if(compare != 0){
					return this.type.compareTo(o.type);
				}
			}
		}
		return compare;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return maxOccurs + "\t" + minOccurs + "\t" + name + "\t" + type;
	}
}
