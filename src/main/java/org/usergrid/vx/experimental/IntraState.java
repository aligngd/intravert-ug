package org.usergrid.vx.experimental;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.cassandra.db.ConsistencyLevel;

/* class that holds properties for the request lifecycle */
public class IntraState {
  //TODO this should be an epiring cache

  private final IntraStateManager intraStateManager;
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
	Filter currentFilter;

  IntraState(IntraStateManager intraStateManager) {
    this.intraStateManager = intraStateManager;
  }

  long saveState() {
    return intraStateManager.saveState(this);
  }

  IntraState getState(long id){
    return intraStateManager.getState(id);
  }

  Filter getFilter(String name) {
    return intraStateManager.getFilter(name);
  }

  // TODO
  // getProcessor
  // getMultiProcessor


  void addProcessor(String name, Processor processor) {

  }

  void addFilter(String name, Filter filter) {

  }

  void addMultiProcessor(String name, MultiProcessor multiProcessor) {

  }



}
