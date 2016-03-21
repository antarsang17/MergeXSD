/**
 * 
 */
package com.freddiemac.service.xsdMerger.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;

import com.freddiemac.service.xsdMerger.beans.AnyElement;
import com.freddiemac.service.xsdMerger.beans.Element;
import com.freddiemac.service.xsdMerger.beans.SimpleType;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM_COMPLEX;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject.MERGE_IN_REF;

/**
 * @author c38847
 *
 */
public class MergeUtils {
	
	private static Map<SCHEMAS,Map<String,SimpleType>> simpleTypesMap = new HashMap<SCHEMAS,Map<String, SimpleType>>();
	private static Map<String,List<String>> typHierarchyMap = new HashMap<String, List<String>>();
	private static Properties props;
	
	public enum SCHEMAS{
		SCHEMA1,SCHEMA2;
	}
	
	public enum MERGE_PROPS{
		HOME_LOCATION,INPUT_LOCATION,OUTPUT_LOCATION,TEMPLATE_FILE_NAME,MERGED_FILE_NAME,TYPE_FILE_NAME,REPORT_FILE_NAME;
	}
	
	public static String getProps(MERGE_PROPS propName) {
		return props.getProperty(propName.name());
	}
	
	static{
		BufferedReader br = null;
		try {
			
			props = new Properties();
			props.load(new FileInputStream(new File("").getAbsolutePath()+"\\App.properties"));
			if((props.get(MERGE_PROPS.HOME_LOCATION)+"".trim()).equals("")){
				props.setProperty(MERGE_PROPS.HOME_LOCATION.name(), (new File("").getAbsolutePath()+"\\"));
			}

			br = new BufferedReader(new FileReader(props.getProperty(MERGE_PROPS.HOME_LOCATION.name())+props.getProperty(MERGE_PROPS.TYPE_FILE_NAME.name())));
			String line = null;
			String[] strs = null;
			while ((line = br.readLine()) != null) 
			{
				strs = line.split("=");
				typHierarchyMap.put(strs[0], Arrays.asList(strs[1].split(",")));
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(null!=br)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static void trackMerge(Collection a, Collection b, Collection ab,TRAKKER trakker,
			MERGE_ITEM mergeItem,String mergeItemDesc,String desc){
		
		MERGE_IN_REF mergeRef = null;
		MERGE_TYPE	mergeType = MERGE_TYPE.ADD;
		if(ab==null)return;
		if(a==null && b==null)return;
		if(a==null) mergeRef = MERGE_IN_REF.SCHEMA2;
		if(b==null) mergeRef = MERGE_IN_REF.SCHEMA1;
		
		int aDif = ab.size()-(a==null?0:a.size());
		int bDif = ab.size()-(b==null?0:b.size());
		
		if(aDif==0 && bDif==0)return;
		
		if(mergeRef==null){
			if(aDif>0 && bDif>0){
				mergeRef = MERGE_IN_REF.BOTH;
			}else if(aDif>0){
				mergeRef = MERGE_IN_REF.SCHEMA2;
			}else{
				mergeRef = MERGE_IN_REF.SCHEMA1;
			}
		}
		
		trakker.add(new TrakkerObject(MERGE_TYPE.ADD, mergeItem, mergeItemDesc, 
    			mergeRef,
    			desc+"/Added-"+ 
    			(mergeRef.equals(MERGE_IN_REF.SCHEMA1)?
    					aDif:(mergeRef.equals(MERGE_IN_REF.SCHEMA2)?bDif:(aDif+bDif)))+"\n"));
	}
	
	public static Collection<Element> handleDuplicateElememts(List<Element> elements1,List<Element> elements2,TRAKKER trakker,String parentname){
		Map<String,Element> elememtsMap = new HashMap<String, Element>();
		for(Element elem:elements1){
			elememtsMap.put(elem.getName(), elem);
		}
		
		for(Element elem:elements2){
			if(elememtsMap.containsKey(elem.getName())){
				if(!elememtsMap.get(elem.getName()).equals(elem))
					elememtsMap.get(elem.getName()).setType(getTypeByHierarchy(elememtsMap.get(elem.getName()).getType(), elem.getType(),trakker,parentname));
			}else
				elememtsMap.put(elem.getName(), elem);
		}
		return elememtsMap.values();
	}
	
	private static String getTypeByHierarchy(String type1,String type2,TRAKKER trakker,String parentname){
		SimpleType simpleType1 = simpleTypesMap.get(SCHEMAS.SCHEMA1).get(type1);
		SimpleType simpleType2 = simpleTypesMap.get(SCHEMAS.SCHEMA2).get(type2);
		String dataType1 = simpleType1.getRestriction().getBase();
		String dataType2 = simpleType2.getRestriction().getBase();
		String returnType = null;
		
		if(!dataType1.equals(dataType2)){
			String type= getTypeByHierarchyRecursive(dataType1, dataType2,false);
			if(null == type){
				trakker.addRisk(new TrakkerObject(null, null, null, null, "Simple Type of Name-"+simpleType1.getName()+" has discrepancy, Type Hierarchy Not handled. Update Type.properties"
						+ "\n\t "+trakker.getSchema1Name()+"\t"+ simpleType1
						+ "\n\t "+trakker.getSchema2Name()+"\t"+ simpleType2));
				return type1;
			}
			
			returnType = type.equals(dataType1)?type1:type2;
			
			trakker.add(new TrakkerObject(MERGE_TYPE.MERGE, MERGE_ITEM.COMPLEX_TYPE, MERGE_ITEM_COMPLEX.ELEMENTS.name(), 
					returnType.equals(type1)?MERGE_IN_REF.SCHEMA1:MERGE_IN_REF.SCHEMA2,
	    			"Compared type for "+parentname+" - "+ type1+" / "+type2+" - resulting - "+returnType));
			
			return returnType;
		}
		else{
			
			List<AnyElement> res1 = simpleType1.getRestriction().getOtrRestrictions();
			List<AnyElement> res2 = simpleType2.getRestriction().getOtrRestrictions();
			
			if(res1.size()!=res2.size()){
				trakker.addRisk(new TrakkerObject(null, null, null, null, "Simple Type of Name-"+simpleType1.getName()+" has discrepancy, which cannot be handled by tool. Please fix manually"
						+ "\n\t "+trakker.getSchema1Name()+"\t"+ simpleType1
						+ "\n\t "+trakker.getSchema2Name()+"\t"+ simpleType2));
				return type1;
			}
			
			List<AnyElement> merged = handleDuplicateAnyElememts(res1, res2);
			
			if(CollectionUtils.isEqualCollection(merged, res1))
				returnType = type1;
			else if(CollectionUtils.isEqualCollection(merged, res2))
				returnType = type2;
			else{
				trakker.addRisk(new TrakkerObject(null, null, null, null, "Simple Type of Name-"+simpleType1.getName()+" has discrepancy, which cannot be handled by tool. Please fix manually"
						+ "\n\t "+trakker.getSchema1Name()+"\t"+ simpleType1
						+ "\n\t "+trakker.getSchema2Name()+"\t"+ simpleType2));
				return type1;
			}
			
			trakker.add(new TrakkerObject(MERGE_TYPE.MERGE, MERGE_ITEM.COMPLEX_TYPE, MERGE_ITEM_COMPLEX.ELEMENTS.name(), 
					returnType.equals(type1)?MERGE_IN_REF.SCHEMA1:MERGE_IN_REF.SCHEMA2,
	    			"Compared type for "+parentname+" - "+ type1+" / "+type2+" - resulting - "+returnType));
			
			return returnType;
			
		}
		
	}
	
	
	
	public static String getTypeByHierarchyRecursive(String dataType1,String dataType2,boolean last){
		String foundKey = null;
		for(Entry<String, List<String>> entry:typHierarchyMap.entrySet()){
			if(entry.getValue().contains(dataType1)){
				foundKey=entry.getKey();break;
			}
		}
		if(foundKey!=null){
			if(foundKey.equals(dataType2))
				return foundKey;
			else
				return getTypeByHierarchyRecursive(foundKey, dataType2,false);
		}else if(!last){
			return getTypeByHierarchyRecursive(dataType2, dataType1,true);
		}
		return null;
	}
	
	/**
	 * @param simpleTypesMap the simpleTypesMap to set
	 */
	public static void addSimpleTypesMap(Map<String,SimpleType> simpleTypesMap1,Map<String,SimpleType> simpleTypesMap2) {
		MergeUtils.simpleTypesMap.put(SCHEMAS.SCHEMA1, simpleTypesMap1);
		MergeUtils.simpleTypesMap.put(SCHEMAS.SCHEMA2, simpleTypesMap2);
	}
	
	public static List<AnyElement> handleDuplicateAnyElememts(List<AnyElement> elements1,List<AnyElement> elements2){
		Map<String,AnyElement> elememtsMap = new HashMap<String, AnyElement>();
		for(AnyElement elem:elements1){
			elememtsMap.put(elem.getName(), elem);
		}
		
		for(AnyElement elem:elements2){
			if(elememtsMap.containsKey(elem.getName())){
				if(!elememtsMap.get(elem.getName()).equals(elem))
					elememtsMap.get(elem.getName()).setValue(
							Integer.parseInt(elememtsMap.get(elem.getName()).getValue())>Integer.parseInt(elem.getValue())
								?elememtsMap.get(elem.getName()).getValue():elem.getValue());;
			}else
				elememtsMap.put(elem.getName(), elem);
		}
		return new ArrayList<AnyElement>(elememtsMap.values());
	}
	
	
}
