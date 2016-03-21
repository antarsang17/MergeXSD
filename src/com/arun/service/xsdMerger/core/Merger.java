package com.freddiemac.service.xsdMerger.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

import com.freddiemac.service.xsdMerger.beans.AnyElement;
import com.freddiemac.service.xsdMerger.beans.ComplexType;
import com.freddiemac.service.xsdMerger.beans.Element;
import com.freddiemac.service.xsdMerger.beans.Enumeration;
import com.freddiemac.service.xsdMerger.beans.Schema;
import com.freddiemac.service.xsdMerger.beans.SimpleType;
import com.freddiemac.service.xsdMerger.core.MergeUtils.MERGE_PROPS;
import com.freddiemac.service.xsdMerger.core.MergeUtils.SCHEMAS;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_ITEM;
import com.freddiemac.service.xsdMerger.core.TRAKKER.MERGE_TYPE;
import com.freddiemac.service.xsdMerger.core.TrakkerObject.MERGE_IN_REF;

public class Merger {
	
	public static void main(String[] args) {
		
		try {
			File inputLocation = new File(MergeUtils.getProps(MERGE_PROPS.INPUT_LOCATION));
			
			boolean mergeFileFound=false;
			Schema merged = null;
			boolean firstReport=true;
			
			for(File inputFile:inputLocation.listFiles()){
				if(inputFile.isDirectory() || !getFileExtn(inputFile.getName()).equals("xsd"))continue;
				if(!mergeFileFound){
					merged = getSchema(inputFile,SCHEMAS.SCHEMA1);
					mergeFileFound = true;
				}
				else{
					TRAKKER trakker = new TRAKKER(merged.getFileName(),inputFile.getName());
					merged = merge(merged, getSchema(inputFile,SCHEMAS.SCHEMA2),trakker);
					appendReport(trakker.toString(),MERGE_PROPS.REPORT_FILE_NAME,firstReport);
					firstReport = false;
				}
			}
		
			printSchema(merged);
			
			System.out.println("Following Files Merged "+ Arrays.asList(inputLocation.list()));
			System.out.println("Find merged file @ "+MergeUtils.getProps(MERGE_PROPS.OUTPUT_LOCATION)+MergeUtils.getProps(MERGE_PROPS.MERGED_FILE_NAME));
			System.out.println("Find merge report @ "+MergeUtils.getProps(MERGE_PROPS.OUTPUT_LOCATION)+MergeUtils.getProps(MERGE_PROPS.REPORT_FILE_NAME));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	    
	}
	
	private static String getFileExtn(String fileName) {
		if(fileName == null || fileName.trim().equals(""))return "";
		
		String[] strs = fileName.split("\\.");
		return strs[strs.length-1];
	}
	private static String getFileName(String fileName) {
		if(fileName == null || fileName.trim().equals(""))return "";
		
		String[] strs = fileName.split("\\.");
		return fileName.replace(strs[strs.length-1],"");
	}
	
	private static Schema getSchema(File file,SCHEMAS scheamaID) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(Schema.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    
	    Schema schema = (Schema) jaxbUnmarshaller.unmarshal(file);
	    schema.setFileName(file.getName());
	    schema.setSchemaID(scheamaID);
	    
	    schema.organize();
	    
	    return schema;
	}
	
	private static Schema merge(Schema schemaMerged,Schema schema2,TRAKKER trakker) {
		
		MergeUtils.addSimpleTypesMap(schemaMerged.getSimpleMaps(), schema2.getSimpleMaps());
		
	    Set<String> superSetComplexKeys = new TreeSet<String>();
	    Set<String> superSetSimpleKeys = new TreeSet<String>();
	    
	    int simpleS1size=schemaMerged.getSimpleTypes().size();
	    int simpleS2size=schema2.getSimpleTypes().size();
	    int complex1size=schemaMerged.getComplexTypes().size();
	    int complex2size=schema2.getComplexTypes().size();
	    
	    for(int i=0;;i++){
	    	
	    	if(i<complex1size)
	    		superSetComplexKeys.add(schemaMerged.getComplexTypes().get(i).getName());
	    	if(i<complex2size)
	    		superSetComplexKeys.add(schema2.getComplexTypes().get(i).getName());
	    	if(i<simpleS1size)
	    		superSetSimpleKeys.add(schemaMerged.getSimpleTypes().get(i).getName());
	    	if(i<simpleS2size)
	    		superSetSimpleKeys.add(schema2.getSimpleTypes().get(i).getName());
	    	
	    	if(i>complex1size && i>complex2size && i>simpleS1size && i>simpleS2size)
	    		break;
	    }
	    
	    List<ComplexType> ComplexTypes = new ArrayList<ComplexType>();
	    for(String key:superSetComplexKeys){
	    	if(!schema2.getComplexMaps().containsKey(key)){
	    		ComplexTypes.add(schemaMerged.getComplexMaps().get(key));
	    		trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.COMPLEX_TYPE, "\t", MERGE_IN_REF.SCHEMA1,key));	
	    	}else if(!schemaMerged.getComplexMaps().containsKey(key)){
	    		ComplexTypes.add(schema2.getComplexMaps().get(key));
	    		trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.COMPLEX_TYPE, "\t", MERGE_IN_REF.SCHEMA2,key));
	    	}else{
	    		ComplexTypes.addAll(schemaMerged.getComplexMaps().get(key).merge(schema2.getComplexMaps().get(key),trakker));
	    	}
	    }
	    
	    List<SimpleType> simpletypes = new ArrayList<SimpleType>();
	    for(String key:superSetSimpleKeys){
	    	if(!schema2.getSimpleMaps().containsKey(key)){
	    		simpletypes.add(schemaMerged.getSimpleMaps().get(key));
	    		trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.SIMPLE_TYPE, "\t", MERGE_IN_REF.SCHEMA1,key));
	    	}else if(!schemaMerged.getSimpleMaps().containsKey(key)){
	    		simpletypes.add(schema2.getSimpleMaps().get(key));
	    		trakker.add(new TrakkerObject(MERGE_TYPE.ADD, MERGE_ITEM.SIMPLE_TYPE, "\t", MERGE_IN_REF.SCHEMA2,key));
	    	}else{
	    		simpletypes.addAll(schemaMerged.getSimpleMaps().get(key).merge(schema2.getSimpleMaps().get(key),trakker));
	    	}
	    }
	    
	    schemaMerged.setComplexTypes(ComplexTypes);
	    schemaMerged.setSimpleTypes(simpletypes);
	    schemaMerged.setFileName(getFileName(schemaMerged.getFileName())+"|"+schema2.getFileName());
	    schemaMerged.organize();
	    
	    return schemaMerged;
	}
	
	private static void printSchema(Schema merged){
		
		StringBuffer outputSchema = new StringBuffer();
		
		for(SimpleType type:merged.getSimpleTypes()){
			outputSchema.append("\t<xs:simpleType name=\""+type.getName()+"\"> \n");
			outputSchema.append("\t\t <xs:restriction base=\""+type.getRestriction().getBase()+"\" > \n");
			for(Enumeration enumval:type.getRestriction().getEnumerations()){
				outputSchema.append("\t \t \t <xs:enumeration value=\""+enumval.getValue()+"\"/> \n");
			}
			for(AnyElement any:type.getRestriction().getOtrRestrictions()){
				outputSchema.append("\t \t \t <"+any.getName()+" value=\""+any.getValue()+"\"/> \n");
			}
			outputSchema.append("\t\t </xs:restriction> \n");
			outputSchema.append("\t</xs:simpleType> \n\n");
		}
		
		for(ComplexType type:merged.getComplexTypes()){
			outputSchema.append("\t <xs:complexType name=\""+type.getName()+"\"> \n");
			outputSchema.append("\t\t <xs:all> \n");
			for(Element elem:type.getAllSequence().getElements()){
				outputSchema.append(" \t\t \t <xs:element"
						+ " maxOccurs=\""+elem.getMaxOccurs()+"\""
						+ " minOccurs=\""+elem.getMinOccurs()+"\""
						+ " name=\""+elem.getName()+"\""
						+ " type=\""+elem.getType()+"\""
						+ " /> \n");
			}
			outputSchema.append("\t\t </xs:all> \n");
			outputSchema.append("\t</xs:complexType> \n \n");
		}
		
		writeFile(outputSchema.toString(),"$MERGED");
	}
	
	private static void writeFile(String replace,String placeHolder){
		String s;
		try {
			s = FileUtils.readFileToString(new File(MergeUtils.getProps(MERGE_PROPS.OUTPUT_LOCATION)+MergeUtils.getProps(MERGE_PROPS.TEMPLATE_FILE_NAME)));
			s=s.replace(placeHolder, replace);
			FileUtils.write(new File(MergeUtils.getProps(MERGE_PROPS.OUTPUT_LOCATION)+MergeUtils.getProps(MERGE_PROPS.MERGED_FILE_NAME)), s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void appendReport(String append,MERGE_PROPS reportFile,boolean firstReport){
		String s;
		try {
			File f = new File(MergeUtils.getProps(MERGE_PROPS.OUTPUT_LOCATION)+MergeUtils.getProps(reportFile));
			if(firstReport)f.delete();
			if(!f.exists())f.createNewFile();
			s = FileUtils.readFileToString(f);
			s=s+"\n"+append.replaceAll("\t", ",");
			FileUtils.write(f, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

