package org.jboss.as.quickstarts.datagrid.prova.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;


import org.drools.KnowledgeBase;


import org.drools.runtime.StatefulKnowledgeSession;
import org.infinispan.Cache;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.as.quickstarts.datagrid.prova.mappers.MapperList;
import org.jboss.as.quickstarts.datagrid.prova.mappers.MapperOnly;
import org.jboss.as.quickstarts.datagrid.prova.model.RawData;
import org.jboss.as.quickstarts.datagrid.prova.model.custom.ByteArrayStrategy;
import org.jboss.as.quickstarts.datagrid.prova.reducers.ReducerOnly;
import org.jboss.as.quickstarts.datagrid.prova.reducers.ReducerSum;
import org.jboss.as.quickstarts.datagrid.prova.repo.RawDataRepository;
import org.springframework.context.ApplicationContext;

import gnu.trove.map.hash.TCustomHashMap;



@Named
@RequestScoped
public class BusinessRuleControllerTest {

	@Inject
	private Logger log;

	@Inject
	DefaultCacheManager m;

	@Inject
	ApplicationContext ctx;

	@Inject
	KnowledgeBase know;

	private String key;
	private String value;
	private String message;
	private String absPath;
	private String mapValue;
	private String reduceValue;
	private static int RECORDS_BUFFER = 1000000;
	private Long id;
	private static Map<String, RawData> results1 = new HashMap<String, RawData>();
	private static Map<String, List<RawData>> results2 = new HashMap<String, List<RawData>>();


	public void putSomethingPath() throws IOException {
		
		long start = System.currentTimeMillis();
		String filename = "file1";
		Charset charset = Charset.forName("UTF-8");
		Path filePath = Paths.get(absPath); 
		BufferedReader br = Files.newBufferedReader(filePath, charset);
		Cache<String, TCustomHashMap<byte[], byte[]>> c = m.getCache();
		
		try {
			String sCurrentLine;
			int counter = 0;
			int workerCount = 1;
			String recordLineValue;
			TCustomHashMap<byte[], byte[]> temporaryMap = new TCustomHashMap<byte[], byte[]>(new ByteArrayStrategy());
			byte[] key;
			byte[] value;
			
			while ((sCurrentLine = br.readLine()) != null) {
				recordLineValue = (sCurrentLine.split(" "))[1];
				key = (recordLineValue).getBytes(StandardCharsets.UTF_8);
				value = (sCurrentLine + " " +filename).getBytes(StandardCharsets.UTF_8);
				
				temporaryMap.put(key, value);
				counter++;
				
				if (counter >= RECORDS_BUFFER) {
					MemoryManagerHandler mmh = new MemoryManagerHandler(c, temporaryMap, filename);
					thread(mmh, "MemoryWorker" + workerCount, false);
					workerCount++;
					temporaryMap = new TCustomHashMap<byte[], byte[]>(new ByteArrayStrategy());
					counter = 0;
				}
			
			}
			if (temporaryMap.size() != 0) {
				MemoryManagerHandler mmh = new MemoryManagerHandler(c, temporaryMap, filename);
				thread(mmh, "MemoryWorker" + workerCount, false);
				System.out.println(temporaryMap.size());
				temporaryMap = new TCustomHashMap<byte[], byte[]>(new ByteArrayStrategy());
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("CACHE INSERT END IN : " + (end - start) / 1000f + " seconds");
	}

	public void finish() {

		StatefulKnowledgeSession ksession = know.newStatefulKnowledgeSession();

		Cache<String, TCustomHashMap<byte[], byte[]>> c = m.getCache();
		RawDataRepository rawDataRep = ctx.getBean(RawDataRepository.class);

		long elabStart = System.currentTimeMillis();
		System.out.println("MAP/REDUCE START ");
	
		ksession.insert(c);
//		ksession.insert(results1);
//		ksession.insert(results2);
//		ksession.insert(mapValue);
//		ksession.insert(reduceValue);
		
		ksession.fireAllRules();
//		MapperOnly mapperOnly = new MapperOnly();
//		mapperOnly.setCode(reduceValue);
//		MapReduceTask<String, THashMap<ByteBuffer, byte[]>, String, RawData> task = new MapReduceTask<String, ConcurrentMap<ByteBuffer, byte[]>, String, RawData>(c);
//		results1 = task.mappedWith(mapperOnly).reducedWith(new ReducerSum()).execute();
//
//		MapperList mapperList = new MapperList();
//		mapperList.setCode(mapValue);
//		MapReduceTask<String, ConcurrentMap<ByteBuffer, byte[]>, String, List<RawData>> task1 = new MapReduceTask<String, ConcurrentMap<ByteBuffer, byte[]>, String, List<RawData>>(c);
//		results2 = task1.mappedWith(mapperList).reducedWith(new ReducerOnly()).execute();

//		results2 = mapOnCode(c,mapValue);
//		results1 = reduceOnCode(c, reduceValue);
		List<RawData> rdList = results2.get(mapValue);
		for(RawData rd : rdList){
			System.out.println(rd);
		}

		RawData rd = results1.get(reduceValue);

		rawDataRep.save(rdList);
		rawDataRep.save(rd);
		long elabEnd = System.currentTimeMillis();
		System.out.println("MAP/REDUCE END : " + (elabEnd - elabStart) / 1000f + " seconds");
		System.out.println(rd);
		message += "Processed Value : " + rd;
	}
	
	public static void mapOnCode(Cache<String, TCustomHashMap<byte[], byte[]>> c, String code){
		MapperList mapperList = new MapperList();
		mapperList.setCode(code);
		MapReduceTask<String, TCustomHashMap<byte[], byte[]>, String, List<RawData>> task1 = new MapReduceTask<String, TCustomHashMap<byte[], byte[]>, String, List<RawData>>(c);
		results2 = task1.mappedWith(mapperList).reducedWith(new ReducerOnly()).execute();
	}
	
	public static void reduceOnCode(Cache<String, TCustomHashMap<byte[], byte[]>> c, String code){
		MapperOnly mapperOnly = new MapperOnly();
		mapperOnly.setCode(code);
		MapReduceTask<String, TCustomHashMap<byte[], byte[]>, String, RawData> task = new MapReduceTask<String, TCustomHashMap<byte[], byte[]>, String, RawData>(c);
		results1 = task.mappedWith(mapperOnly).reducedWith(new ReducerSum()).execute();
	}


	public static class MemoryManagerHandler implements Runnable {

		private Cache<String, TCustomHashMap<byte[], byte[]>> localCache;
		private TCustomHashMap<byte[], byte[]> temporaryMap;
		private String keyMap="";
		private static long keyMapCode = 0;

		public MemoryManagerHandler(Cache<String, TCustomHashMap<byte[], byte[]>> c, TCustomHashMap<byte[], byte[]> temporaryMap, String filename) {
			super();
			this.localCache = c;
			this.temporaryMap = temporaryMap;
			this.keyMap = filename;
		}

		public void run() {
			String incrementale = ""+keyMapCode++;
			localCache.put(keyMap+" "+incrementale,temporaryMap);
			System.out.println(keyMap+" " +incrementale +" Temporary Map Size:  " + temporaryMap.size());
			
		}

		public Cache<String, TCustomHashMap<byte[], byte[]>> getLocalCache() {
			return localCache;
		}

		public void setLocalCache(Cache<String, TCustomHashMap<byte[], byte[]>> localCache) {
			this.localCache = localCache;
		}

		public TCustomHashMap<byte[], byte[]> getTemporaryMap() {
			return temporaryMap;
		}

		public void setTemporaryMap(TCustomHashMap<byte[], byte[]> temporaryMap) {
			this.temporaryMap = temporaryMap;
		}

	}

	public Thread thread(Runnable runnable, String threadName, boolean daemon) {
		Thread memoryLoaderHandler = new Thread(runnable);
		memoryLoaderHandler.setDaemon(daemon);
		memoryLoaderHandler.setName(threadName);
		log.info("Start thread " + threadName + " [ " + System.identityHashCode(runnable) + " ]");
		memoryLoaderHandler.start();
		return memoryLoaderHandler;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAbsPath() {
		return absPath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	// Called by the get.xhtml - get button
	public void getOne() {

		Cache<Long, String> c = m.getCache();

		String rdCache = c.get(id);
		RawData rdDb = new RawData();
		if (rdCache == null) {
			message = "Nothing in the Cache \n";
			message = "Nothing in the DB \n";
		} else {
			String[] recordLineValue;
			recordLineValue = rdCache.split(" - ");
			RawData rd = new RawData();
			rd.setHalfKey(recordLineValue[0]);
			rd.setFirstValue(recordLineValue[1]);
			rd.setSecondValue(recordLineValue[2]);
			rd.setFileName(recordLineValue[3]);
			message = "Cache: " + rd.toString();
		}

	}

	public void getByKey() {

		RawDataRepository rawDataRep = ctx.getBean(RawDataRepository.class);
		Cache<Long, String> c = m.getCache();

		Set<RawData> cacheSet = new HashSet<RawData>();
		Set<RawData> dbSet = new HashSet<RawData>();
		Set<Long> idSet = c.keySet();
		String[] recordLineValue;
		for (Long id : idSet) {
			String rdString = c.get(id);
			recordLineValue = rdString.split(" - ");
			if (recordLineValue[0].equals(key)) {
				RawData rd = new RawData();
				rd.setHalfKey(recordLineValue[0]);
				rd.setFirstValue(recordLineValue[1]);
				rd.setSecondValue(recordLineValue[2]);
				rd.setFileName(recordLineValue[3]);
				cacheSet.add(rd);
			}
		}

		if (cacheSet.isEmpty()) {
			message = "Nothing in the Cache \n";
		} else {
			message = "Cache: " + cacheSet.toString();
		}

		dbSet = rawDataRep.findRawDataByHalfKey(key);

		message += "\n \n \n";
		if (dbSet.isEmpty()) {
			message += "Nothing in the DB \n";
		} else {
			message += "DB: " + dbSet.toString();
		}
		Set<RawData> allSet = new HashSet<RawData>();

		allSet.addAll(cacheSet);
		allSet.addAll(dbSet);
		Iterator<RawData> iter = allSet.iterator();
		while (iter.hasNext()) {
			RawData rdObject = iter.next();
			String rdString = rdObject.getHalfKey() + " - " + rdObject.getFirstValue() + " - " + rdObject.getSecondValue() + " - " + rdObject.getFileName();

			c.put(iter.next().getId(), rdString);
		}
		message += "\n \n \n";

		message += "Insieme dei risultati: \n" + allSet;

	}

	public void getAll() {
		Cache<String, ConcurrentMap<byte[], byte[]>> c = m.getCache();

		message = "" + c.keySet().size();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMapValue() {
		return mapValue;
	}

	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
	}

	public String getReduceValue() {
		return reduceValue;
	}

	public void setReduceValue(String reduceValue) {
		this.reduceValue = reduceValue;
	}


}
