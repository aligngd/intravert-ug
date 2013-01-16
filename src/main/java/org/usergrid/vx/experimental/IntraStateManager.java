package org.usergrid.vx.experimental;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zznate
 */
public class IntraStateManager {
  // TODO
  // add actual cachloader Load for IntraState
  // add actual cacheloader load for DynamicOps
  private final LoadingCache<Long, IntraState> intraStates;
  private final LoadingCache<String, DynamicOpHolder> dynamicOps;
  private final int maxSize;
  private final int expireAfter;
  private final AtomicLong idCounter = new AtomicLong();

  private IntraStateManager(int maxSize, int expireAfter) {
    this.maxSize = maxSize;
    this.expireAfter = expireAfter;
    intraStates =  CacheBuilder.newBuilder()
            .maximumSize(this.maxSize)
            .expireAfterWrite(this.expireAfter, TimeUnit.SECONDS)
            .build(
                    new CacheLoader<Long, IntraState>() {
                      public IntraState load(Long id) throws RuntimeException {
                        return null;
                      }
                    });

    dynamicOps =  CacheBuilder.newBuilder()
            .maximumSize(this.maxSize)
            .build(
                    new CacheLoader<String, DynamicOpHolder>() {
                      public DynamicOpHolder load(String id) throws RuntimeException {
                        return null;
                      }
                    });
  }

  public static IntraStateManager manager(int maxCacheSize, int expireAfterSeconds) {
    return new IntraStateManager(maxCacheSize, expireAfterSeconds);
  }

  /**
   * Create a new (non-persisted) IntraState
   * @return
   */
  public IntraState create() {
    return new IntraState(this);
  }

  /**
   * Persist the provided IntraState
   * @param s
   * @return
   */
  public long saveState(IntraState s){
 	  long val = idCounter.getAndIncrement();
 	  intraStates.put(val, s);
 	  return val;
 	}

  public IntraState getState(long id){
    try {
      return this.intraStates.get(id);
    } catch (ExecutionException ex) {
      throw new RuntimeException("Could not return cached IntraState", ex);
    }
 	}

  DynamicOpHolder holderFor(Filter filter, String name, String spec) {
    return new DynamicOpHolder(filter, name, spec);
  }

  DynamicOpHolder holderFor(Processor processor, String name, String spec) {
    return new DynamicOpHolder(processor, name, spec);
  }

  DynamicOpHolder holderFor(MultiProcessor multiProcessor, String name, String spec) {
    return new DynamicOpHolder(multiProcessor, name, spec);
  }

  Filter getFilter(String name) {
    try {
      DynamicOpHolder holder =  dynamicOps.get(DynamicOpHolder.calculateKey(DynamicOpType.FILTER, name));
      if ( holder != null ) {
        return (Filter)holder.getDynamicOp();
      }
      return null;
    } catch(ExecutionException ee) {
      throw new RuntimeException("Could not retrieve op from cache", ee);
    }
  }

  enum DynamicOpType {
    FILTER,
    PROCESSOR,
    MULTIPROCESSOR;
  }

  static class DynamicOpHolder<T extends DynamicOp> {

    private final T op;
    private final String name;
    private final String spec;
    private final String cacheKey;

    private DynamicOpHolder(T op, String name, String spec) {
      this.op = op;
      this.name = name;
      this.spec = spec;
      DynamicOpType opType;
      if ( op instanceof Filter ) {
        opType = DynamicOpType.FILTER;
      } else if ( op instanceof Processor) {
        opType = DynamicOpType.PROCESSOR;
      } else if (op instanceof MultiProcessor ) {
        opType = DynamicOpType.MULTIPROCESSOR;
      } else {
        throw new RuntimeException("Op type cannot be matched to an implementation: " + op.getClass().getName());
      }
      this.cacheKey = calculateKey(opType, name);
    }

    T getDynamicOp() {
      return op;
    }

    static String calculateKey(DynamicOpType opType, String name) {
      return new StringBuilder()
              .append(opType.toString()).append(":")
              .append(name).append(":")
              .toString();
    }

    String getCacheKey() {
      return cacheKey;
    }

  }
}
