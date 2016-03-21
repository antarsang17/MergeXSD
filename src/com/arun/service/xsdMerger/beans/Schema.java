/**
 * 
 */
package com.freddiemac.service.xsdMerger.beans;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.freddiemac.service.xsdMerger.core.MergeUtils.SCHEMAS;

/**
 * @author c38847
 *
 */
@XmlRootElement(name="schema" ,namespace="http://www.w3.org/2001/XMLSchema")
@XmlAccessorType(XmlAccessType.FIELD)
public class Schema{

	@XmlElement(name="simpleType",namespace="http://www.w3.org/2001/XMLSchema")
	private List<SimpleType> simpleTypes = new LinkedList<SimpleType>();
	
	@XmlTransient
	private String fileName;
	
	@XmlTransient
	private SCHEMAS schemaID;
	
	@XmlTransient
	private Map<String,SimpleType> simpleMaps = new LinkedHashMap<String, SimpleType>();
	
	@XmlElement(name="complexType",namespace="http://www.w3.org/2001/XMLSchema")
	private List<ComplexType> complexTypes = new LinkedList<ComplexType>();;

	@XmlTransient
	private Map<String,ComplexType> complexMaps = new LinkedHashMap<String, ComplexType>();
	
	/**
	 * @return the simpleTypes
	 */
	public List<SimpleType> getSimpleTypes() {
		if(null==simpleTypes)simpleTypes=new LinkedList<SimpleType>();
		return simpleTypes;
	}


	/**
	 * @param simpleTypes the simpleTypes to set
	 */
	public void setSimpleTypes(List<SimpleType> simpleTypes) {
		this.simpleTypes = simpleTypes;
	}


	/**
	 * @return the complexTypes
	 */
	public List<ComplexType> getComplexTypes() {
		if(null==simpleTypes)simpleTypes=new LinkedList<SimpleType>();
		return complexTypes;
	}


	/**
	 * @param complexTypes the complexTypes to set
	 */
	public void setComplexTypes(List<ComplexType> complexTypes) {
		this.complexTypes = complexTypes;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((complexMaps == null) ? 0 : complexMaps.hashCode());
		result = prime * result
				+ ((complexTypes == null) ? 0 : complexTypes.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((simpleMaps == null) ? 0 : simpleMaps.hashCode());
		result = prime * result
				+ ((simpleTypes == null) ? 0 : simpleTypes.hashCode());
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
		Schema other = (Schema) obj;
		if (complexMaps == null) {
			if (other.complexMaps != null)
				return false;
		} else if (!complexMaps.equals(other.complexMaps))
			return false;
		if (complexTypes == null) {
			if (other.complexTypes != null)
				return false;
		} else if (!complexTypes.equals(other.complexTypes))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (simpleMaps == null) {
			if (other.simpleMaps != null)
				return false;
		} else if (!simpleMaps.equals(other.simpleMaps))
			return false;
		if (simpleTypes == null) {
			if (other.simpleTypes != null)
				return false;
		} else if (!simpleTypes.equals(other.simpleTypes))
			return false;
		return true;
	}
	
	public void organize(){
		simpleMaps = new HashMap<String, SimpleType>();
		for(SimpleType stype:getSimpleTypes()){
			simpleMaps.put(stype.getName(), stype);
		}
		complexMaps = new HashMap<String, ComplexType>();
		for(ComplexType ctype:getComplexTypes()){
			complexMaps.put(ctype.getName(), ctype);
		}
	}


	/**
	 * @return the complexMaps
	 */
	public Map<String, ComplexType> getComplexMaps() {
		return complexMaps;
	}


	/**
	 * @param complexMaps the complexMaps to set
	 */
	public void setComplexMaps(Map<String, ComplexType> complexMaps) {
		this.complexMaps = complexMaps;
	}


	/**
	 * @return the simpleMaps
	 */
	public Map<String, SimpleType> getSimpleMaps() {
		return simpleMaps;
	}


	/**
	 * @param simpleMaps the simpleMaps to set
	 */
	public void setSimpleMaps(Map<String, SimpleType> simpleMaps) {
		this.simpleMaps = simpleMaps;
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}


	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
