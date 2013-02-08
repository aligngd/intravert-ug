/* 
 *   Copyright 2013 Nate McCall and Edward Capriolo
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
*/
package org.usergrid.vx.experimental;

import org.vertx.java.core.Vertx;
/* You asked for it. You are basically a first class IntraOp, and can
 * do anything. With great power comes great responsibility. */
public interface ServiceProcessor {
	public void process(IntraReq req, IntraRes res, IntraState state,
			int i, Vertx vertx, IntraService is);
}
