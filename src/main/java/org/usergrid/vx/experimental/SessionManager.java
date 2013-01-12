package org.usergrid.vx.experimental;

import java.util.HashMap;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class SessionManager extends Verticle implements Handler<Message<JsonObject>>{

  private HashMap<Integer,HashMap> session = new HashMap<Integer,HashMap>();
  int i=0;
  
  @Override
  public void handle(Message<JsonObject> arg0) {
    System.out.println("Got req");
    arg0.body.getString("type").equals("create");
    session.put(i++, new HashMap());
    JsonObject result = new JsonObject();
    result.putString("id", i+"");
    arg0.reply(result);
  }

  @Override
  public void start() throws Exception {
    vertx.eventBus().registerHandler("intravert.session", this);
    System.out.println("registierd");
  }

}
