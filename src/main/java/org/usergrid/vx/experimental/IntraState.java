package org.usergrid.vx.experimental;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.cassandra.db.ConsistencyLevel;
import org.usergrid.vx.experimental.scan.ScanContext;
import org.usergrid.vx.experimental.scan.ScanFilter;

/* class that holds properties for the request lifecycle */
public class IntraState {
  //TODO this should be an epiring cache
  private static Map<Integer,IntraState> savedState = new HashMap<Integer,IntraState>();
  private static AtomicInteger id=new AtomicInteger(0);
	String currentKeyspace="";
	String currentColumnFamily="";
	boolean autoTimestamp= true;
	long nanotime = System.nanoTime();
	ConsistencyLevel consistency= ConsistencyLevel.ONE;
	//TODO this is cookie cutter
	Map<IntraMetaData,String> meta = new HashMap<IntraMetaData,String>();
	//TODO separate per/request state from application/session state
	static Map<String,Processor> processors = new HashMap<String,Processor>();
	static Map<String,Filter> filters = new HashMap<String,Filter>(); 
	static Map<String,MultiProcessor> multiProcessors = new HashMap<String,MultiProcessor>();
	static Map<String,ServiceProcessor> serviceProcessors = new HashMap<String,ServiceProcessor>();
	static Map<String,ScanFilter> scanFilters = new HashMap<String,ScanFilter>();
	Filter currentFilter;
	static Map<Integer,ScanContext> openedScanners = new HashMap<Integer,ScanContext>();
	
	private static AtomicInteger scannerId=new AtomicInteger(0);
	
	public int saveState(IntraState s){
	  int i = id.getAndIncrement();
	  savedState.put(i,s);
	  return i;
	}
	
	public IntraState getState(int i){
	  return this.savedState.get(i);
	}
	
	public int openScanner(ScanContext context){
		int id = scannerId.getAndIncrement();
		openedScanners.put(id, context);
		return id;
	}
}
