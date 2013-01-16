package org.usergrid.vx.experimental;

import java.util.Map;

public interface Filter extends DynamicOp {

  public Map filter(Map row);


}
