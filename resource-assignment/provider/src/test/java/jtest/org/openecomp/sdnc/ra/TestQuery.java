/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 ONAP Intellectual Property. All rights
 * 						reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package jtest.org.openecomp.sdnc.ra;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openecomp.sdnc.ra.ResourceAllocator;
import org.openecomp.sdnc.sli.SvcLogicContext;
import org.openecomp.sdnc.sli.SvcLogicResource.QueryStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestQuery {

	private static final Logger log = LoggerFactory.getLogger(TestQuery.class);

	@Autowired(required = true)
	private ResourceAllocator resourceAllocator;

	@Autowired(required = true)
	private DataSetup dataSetup;

	@Test
	public void test001() throws Exception {
		String t = "001";
		log.info("============== query " + t + " ================================");
		log.info("=== Test reading assigned resources (subinterface-id, vlan-id-inner)");

		String service1 = "ICOREPVC" + t + "-1";

		dataSetup.cleanup();
		dataSetup.setupVpePort("MTSNJA4LCP1", "mtanjrsv126", "ae0", "PROV", "juniper-vpe-image");

		SvcLogicContext ctx = new SvcLogicContext();
		ctx.setAttribute("tmp.resource-allocator.request-type", "New");
		ctx.setAttribute("tmp.resource-allocator.service-model", "L3AVPN-EVC");
		ctx.setAttribute("tmp.resource-allocator.service-instance-id", service1);
		ctx.setAttribute("tmp.resource-allocator.speed", "300");
		ctx.setAttribute("tmp.resource-allocator.speed-unit", "Mbps");
		ctx.setAttribute("tmp.resource-allocator.aic-site-id", "MTSNJA4LCP1");
		ctx.setAttribute("tmp.resource-allocator.vpn-id", "123");
		ctx.setAttribute("tmp.resource-allocator.vrf-required", "false");

		QueryStatus st = resourceAllocator.reserve("NetworkCapacity", null, null, null, ctx);

		Assert.assertTrue(st == QueryStatus.SUCCESS);
		Assert.assertTrue(dataSetup.serviceCorrectInDb(service1, "VPE-Cust", "Pending", 1, 300000));

		st = resourceAllocator.query("NetworkCapacity", true, null, service1, "end-point", null, ctx);

		Assert.assertTrue(st == QueryStatus.SUCCESS);
		Assert.assertEquals(ctx.getAttribute("end-point.subinterface-id"), "100");
		Assert.assertEquals(ctx.getAttribute("end-point.vlan-id-inner"), "2");
		Assert.assertEquals(ctx.getAttribute("end-point.vpe-name"), "mtanjrsv126");
		Assert.assertEquals(ctx.getAttribute("end-point.affinity-link"), "1");
	}
}
