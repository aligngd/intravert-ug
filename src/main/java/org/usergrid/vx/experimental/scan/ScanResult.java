package org.usergrid.vx.experimental.scan;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.usergrid.vx.experimental.IntraClient;
import org.usergrid.vx.experimental.IntraOp;
import org.usergrid.vx.experimental.IntraReq;
import org.usergrid.vx.experimental.IntraRes;
import org.usergrid.vx.experimental.Operations;


public class ScanResult implements Iterable<Map>{

	IntraClient c;
	int scanId;
	List<Map> currentRows;
	enum STATE { NEW, RUNNING, DONE}
	STATE thisState;
	int currentIndex = 0;
	
	public ScanResult(IntraClient c, int scanId){
		this.c = c;
		this.scanId = scanId;
		thisState = STATE.NEW;
	}
	
	@Override 
	public Iterator<Map> iterator() {
		 return new Iterator<Map>() {
			 
			private void getFromServer(boolean skipFirst){
				IntraOp i = Operations.nextscan(scanId, skipFirst);
				IntraReq req = new IntraReq();
				req.add(i);
				IntraRes res = null;
				try {
					res = c.sendBlocking(req);
				} catch (Exception e) {
					throw new RuntimeException (e);
				}
				currentRows = (List<Map>) res.getOpsRes().get(0);
				if (currentRows.size() == 0) {
					thisState = STATE.DONE;
				} else {
					thisState = STATE.RUNNING;
				}
			}
			@Override
			public boolean hasNext() {
				if (thisState == STATE.NEW) {
					getFromServer(false);
				}
				if (thisState == STATE.RUNNING) {
					if (currentIndex < currentRows.size()) {
						return true;
					} else if (currentIndex == currentRows.size()) {
						getFromServer(false);
						if (currentRows.size() > 0) {
							return true;
						} else {
							thisState = STATE.DONE;
							return false;
						}
					} else {
						throw new RuntimeException(
								"remove this should not be reachable");
					}
				} else { // (thisState == STATE.DONE )
					return false;
				}

			}

			@Override
			public Map next() {
				if (this.hasNext()){
					return currentRows.get(currentIndex++);
				} else if (thisState == STATE.DONE){
					return null;
				}
				return null; 
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
			 
		 };
	}

	public int getScannerId(){
		return this.scanId;
	}
	
	public STATE getState(){
		return this.thisState;
	}
}
