package org.jboss.as.quickstarts.datagrid.prova.reducers;


import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;

public class ReducerSum implements Reducer<String, RawData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8031855321028697782L;

	@Override
	public RawData reduce(String reducedKey, Iterator<RawData> iter) {
		int sum = 0;
		while(iter.hasNext()){
			sum += Integer.parseInt(iter.next().getThirdValue(), 2);
		}
		RawData rd = new RawData();
		rd.setFileName("all");
		rd.setFirstValue("boh");
		rd.setSecondValue("");
		rd.setThirdValue(""+sum);
		rd.setHalfKey(reducedKey);
		return rd;
	}

//	@Override
//	public Integer reduce(String reducedKey, Iterator<Integer> iter) {
//		Integer sum = 0;
//		while(iter.hasNext()){
//			sum += iter.next();
//		}
//		return sum;
//	}

}
