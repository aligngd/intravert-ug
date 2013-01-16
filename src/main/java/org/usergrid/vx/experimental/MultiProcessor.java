package org.usergrid.vx.experimental;

import java.util.List;
import java.util.Map;

public interface MultiProcessor extends DynamicOp {
  public List<Map> multiProcess(Map<Integer,Object> results, Map params);
}
