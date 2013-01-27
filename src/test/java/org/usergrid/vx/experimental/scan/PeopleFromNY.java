package org.usergrid.vx.experimental.scan;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.utils.ByteBufferUtil;
import org.usergrid.vx.experimental.IntraService;
import org.usergrid.vx.experimental.IntraState;
import org.vertx.java.core.Vertx;

public class PeopleFromNY implements ScanFilter {

	public PeopleFromNY() {
	}

	@Override
	public boolean filter(Map currentRow, ScanContext c) {
		String val = null;
		try {
			val = ByteBufferUtil.string(((ByteBuffer) currentRow.get("value")).duplicate());
		} catch (CharacterCodingException e) {
			
		}
		
		if (val.compareTo("NY")==1){
			return false; //stop if we get past ny
		} else if (val.equals("NY")){
			c.collect(currentRow);
			return true;
		} else {
			return true;
		}
	}

}
