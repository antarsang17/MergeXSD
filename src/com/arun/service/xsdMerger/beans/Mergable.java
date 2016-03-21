/**
 * 
 */
package com.freddiemac.service.xsdMerger.beans;

import java.util.List;

import com.freddiemac.service.xsdMerger.core.TRAKKER;


/**
 * @author c38847
 *
 */
public interface Mergable<T>{

	public List<T> merge(T object,TRAKKER trakker);
}
