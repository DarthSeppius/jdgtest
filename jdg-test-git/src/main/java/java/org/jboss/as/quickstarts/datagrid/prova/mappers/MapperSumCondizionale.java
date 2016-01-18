//package org.jboss.as.quickstarts.datagrid.prova.mappers;
//
//import java.nio.charset.StandardCharsets;
//
//import org.infinispan.distexec.mapreduce.Collector;
//import org.infinispan.distexec.mapreduce.Mapper;
//import org.jboss.as.quickstarts.datagrid.prova.controller.BusinessRuleController;
//
//public class MapperSumCondizionale implements Mapper<byte[], byte[], String, Integer>{
//
////	@Override
////	public void map(String key, RawData value, Collector<String, Integer> collector) {
////		if(value.getHalfKey().equals("0300")){
////			collector.emit(value.getHalfKey(), Integer.parseInt(value.getThirdValue(), 2));
////		}
////	}
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 173449041420365078L;
//	
//	private String reduceValue;
//
//	@Override
//	public void map(byte[] key, byte[] value, Collector<String, Integer> collector) {
//		String[] recordLineValue = new String (value, StandardCharsets.UTF_8).split(" ");
//		if(recordLineValue[1].equals(reduceValue)){
//			collector.emit(recordLineValue[1], Integer.parseInt(recordLineValue[3], 2));
//		}
//	}
//
//	public String getReduceValue() {
//		return reduceValue;
//	}
//
//	public void setReduceValue(String reduceValue) {
//		this.reduceValue = reduceValue;
//	}
//	
//
//}
