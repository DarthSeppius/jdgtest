package org.jboss.as.quickstarts.datagrid.prova.reducers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.infinispan.distexec.mapreduce.Reducer;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;

public class ReducerOnly implements Reducer<String, List<RawData>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 793321666564160876L;

	@Override
	public List<RawData> reduce(String reducedKey, Iterator<List<RawData>> iter) {
		
		ArrayList<RawData> rdList = new ArrayList<RawData>();
		
		while(iter.hasNext()){
			rdList.addAll(iter.next());
		}
		
		return rdList;
	}

	

	

}
