package org.jboss.as.quickstarts.datagrid.prova.mappers;


import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferImpl;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.map.hash.THashMap;

public class MapperOnly implements Mapper<String, TCustomHashMap<byte[], byte[]>, String, RawData>{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7747940711267131933L;

//	private String code;
	private byte[] byteBuffer;

	public String getCode() {
		return new String(byteBuffer, StandardCharsets.UTF_8);
	}

	public void setCode(String code) {
		this.byteBuffer = code.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void map(String key, TCustomHashMap<byte[], byte[]> value, Collector<String, RawData> collector) {
		
		byte[] rdByte = value.get(byteBuffer);
		if(rdByte != null){
			String rdString = new String(rdByte, StandardCharsets.UTF_8);
			String[] rdArrayString = rdString.split(" ");
			RawData rd = new RawData();
			rd.setFileName(rdArrayString[4]);
			rd.setHalfKey(rdArrayString[1]);
			rd.setFirstValue(rdArrayString[0]);
			rd.setSecondValue(rdArrayString[2]);
			rd.setThirdValue(rdArrayString[3]);
			collector.emit(this.getCode(), rd);
		}
		
	}


//	@Override
//	public void map(String key, ConcurrentMap<ByteBuffer, byte[]> value, Collector<String, RawData> collector) {
//
//		byte[] b = code.getBytes(StandardCharsets.UTF_8);
//		
//		byte[] rdByte = value.get(b);
//		if(rdByte != null){
//			String rdString = new String(rdByte, StandardCharsets.UTF_8);
//			String[] rdArrayString = rdString.split(" ");
//			RawData rd = new RawData();
//			rd.setFileName(rdArrayString[4]);
//			rd.setHalfKey(rdArrayString[1]);
//			rd.setFirstValue(rdArrayString[0]);
//			rd.setSecondValue(rdArrayString[2]);
//			rd.setThirdValue(rdArrayString[3]);
//			collector.emit(code, rd);
//		}
//	}
	
	



	//	@Override
	//	public void map(String key, ConcurrentMap<String, RawData> value,
	//			Collector<String, ConcurrentMap<String, RawData>> collector) {
	//		
	//		String[] keySplitter = key.split(" ");
	//		String filename = keySplitter[0];
	//		Set<String> files = new HashSet<String>();
	//		files.add(filename);
	//		
	//		ConcurrentHashMap<String, RawData> fileMap = new ConcurrentHashMap<String, RawData>();
	//		fileMap.putAll(value);
	//		collector.emit(filename, fileMap);
	//	}




}
