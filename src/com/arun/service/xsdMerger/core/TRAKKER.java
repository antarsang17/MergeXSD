/**
 * 
 */
package com.freddiemac.service.xsdMerger.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author c38847
 *
 */
public class TRAKKER {

	public enum MERGE_TYPE{
		ADD,DELETE,UPDATE,MERGE;
	}
	
	public enum MERGE_ITEM{
		SIMPLE_TYPE,COMPLEX_TYPE;
	}
	
	public enum MERGE_ITEM_SIMPLE{
		RESTRICTION,ENUMERATION,OTHER_ITEMS;
	}
	
	public enum MERGE_ITEM_COMPLEX{
		ALL_SEQUENCE,ELEMENTS;
	}
	
	private String schema1Name;
	private String schema2Name;
	
	private List<TrakkerObject> trakkers = new ArrayList<TrakkerObject>();
	
	private List<TrakkerObject> riskTrakkers = new ArrayList<TrakkerObject>();

	
	/**
	 * @param schema1Name
	 * @param schema2Name
	 */
	public TRAKKER(String schema1Name, String schema2Name) {
		super();
		this.schema1Name = schema1Name;
		this.schema2Name = schema2Name;
	}
	
	public void add(TrakkerObject obj){
		obj.setSchema1Name(schema1Name);
		obj.setSchema2Name(schema2Name);
		trakkers.add(obj);
	}
	
	public Iterator<TrakkerObject> getTrakkers(){
		return trakkers.iterator();
	}
	
	public void addRisk(TrakkerObject obj){
		obj.setSchema1Name(schema1Name);
		obj.setSchema2Name(schema2Name);
		riskTrakkers.add(obj);
	}
	
	public Iterator<TrakkerObject> getRiskTrakkers(){
		return riskTrakkers.iterator();
	}

	/**
	 * @return the schema1Name
	 */
	public String getSchema1Name() {
		return schema1Name;
	}

	/**
	 * @param schema1Name the schema1Name to set
	 */
	public void setSchema1Name(String schema1Name) {
		this.schema1Name = schema1Name;
	}

	/**
	 * @return the schema2Name
	 */
	public String getSchema2Name() {
		return schema2Name;
	}

	/**
	 * @param schema2Name the schema2Name to set
	 */
	public void setSchema2Name(String schema2Name) {
		this.schema2Name = schema2Name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return schema1Name + "  Vs " + schema2Name + "\n" + printTrakkers();
	}

	private String printTrakkers(){
		StringBuffer buffer = new StringBuffer();
		
		for(TrakkerObject obj: trakkers){
			buffer.append("\t"+obj.toString()+"\n");
		}
		if(!riskTrakkers.isEmpty())
			buffer.append("\n\n\tRISKS\n\t=====\n");
		for(TrakkerObject obj: riskTrakkers){
			buffer.append("\t"+obj.getDesc()+"\n");
		}
		buffer.append("\n");
		return buffer.toString();
	}

}
