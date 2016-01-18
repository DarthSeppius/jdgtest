package org.jboss.as.quickstarts.datagrid.prova.mappers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferImpl;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.map.hash.THashMap;

public class MapperList implements Mapper<String, TCustomHashMap<byte[], byte[]>, String, List<RawData>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845331840161419010L;

	//	private String code;
	private byte[] byteBuffer;

	public String getCode() {
		return new String(byteBuffer, StandardCharsets.UTF_8);
	}

	public void setCode(String code) {
		this.byteBuffer = code.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void map(String key, TCustomHashMap<byte[], byte[]> value, Collector<String, List<RawData>> collector) {
		ArrayList<RawData> rdList = new ArrayList<RawData>(1);

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
			rdList.add(rd);
			collector.emit(this.getCode(), rdList);
		}
	}

	//	@Override
	//	public void map(String key, ConcurrentMap<ByteBuffer, byte[]> value, Collector<String, List<RawData>> collector) {
	//		ArrayList<RawData> rdList = new ArrayList<RawData>(1);
	//
	//		byte[] b = code.getBytes(StandardCharsets.UTF_8);
	//
	//		byte[] rdByte = null;
	//		
	//		for( Entry<byte[], byte[]> entry : value.entrySet()){
	//			if(entry.getKey().toString().equals(b.toString()))
	//				rdByte = entry.getValue();
	//		}
	//		
	//		if(rdByte != null){
	//			String rdString = new String(rdByte, StandardCharsets.UTF_8);
	//			String[] rdArrayString = rdString.split(" ");
	//			RawData rd = new RawData();
	//			rd.setFileName(rdArrayString[4]);
	//			rd.setHalfKey(rdArrayString[1]);
	//			rd.setFirstValue(rdArrayString[0]);
	//			rd.setSecondValue(rdArrayString[2]);
	//			rd.setThirdValue(rdArrayString[3]);
	//			rdList.add(rd);
	//			collector.emit(code, rdList);
	//		}
	//	}

}
