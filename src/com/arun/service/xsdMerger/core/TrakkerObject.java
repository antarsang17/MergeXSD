/**
 * 
 */
package com.freddiemac.service.xsdMerger.core;

import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;

/**
 * @author c38847
 *
 */
public class TrakkerObject {

	private String schema1Name;
	private String schema2Name;
	private MERGE_TYPE mergeType;
	private MERGE_ITEM mergeItem;
	private String mergeItemDesc;
	private MERGE_IN_REF mergeInRef;
	private String desc;
	
	public enum MERGE_IN_REF{
		SCHEMA1,SCHEMA2,BOTH;
	}

	/**
	 * @param schema1Name
	 * @param schema2Name
	 * @param mergeType
	 * @param mergeItem
	 * @param mergeItemDesc
	 */
	public TrakkerObject(MERGE_TYPE mergeType, MERGE_ITEM mergeItem, String mergeItemDesc, MERGE_IN_REF mergeInRef,String desc) {
		super();
		this.mergeType = mergeType;
		this.mergeItem = mergeItem;
		this.mergeItemDesc = mergeItemDesc;
		this.mergeInRef = mergeInRef;
		this.desc = desc;
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

	/**
	 * @return the mergeType
	 */
	public MERGE_TYPE getMergeType() {
		return mergeType;
	}

	/**
	 * @param mergeType the mergeType to set
	 */
	public void setMergeType(MERGE_TYPE mergeType) {
		this.mergeType = mergeType;
	}

	/**
	 * @return the mergeItem
	 */
	public MERGE_ITEM getMergeItem() {
		return mergeItem;
	}

	/**
	 * @param mergeItem the mergeItem to set
	 */
	public void setMergeItem(MERGE_ITEM mergeItem) {
		this.mergeItem = mergeItem;
	}

	/**
	 * @return the mergeItemDesc
	 */
	public String getMergeItemDesc() {
		return mergeItemDesc;
	}

	/**
	 * @param mergeItemDesc the mergeItemDesc to set
	 */
	public void setMergeItemDesc(String mergeItemDesc) {
		this.mergeItemDesc = mergeItemDesc;
	}

	/**
	 * @return the mergeInRef
	 */
	public MERGE_IN_REF getMergeInRef() {
		return mergeInRef;
	}

	/**
	 * @param mergeInRef the mergeInRef to set
	 */
	public void setMergeInRef(MERGE_IN_REF mergeInRef) {
		this.mergeInRef = mergeInRef;
	}
	
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mergeType + "\t"+ mergeItem + "\t" + mergeItemDesc+ "\t" + mergeInRef+ "\t" + desc;
	}

	
}
