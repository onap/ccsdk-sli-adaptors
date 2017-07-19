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

package org.openecomp.sdnc.sli.aai.r1607;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicResource.QueryStatus;
import org.openecomp.aai.inventory.v10.GenericVnf;
import org.openecomp.aai.inventory.v10.InventoryResponseItems;
import org.openecomp.sdnc.sli.aai.AAIClient;
import org.openecomp.sdnc.sli.aai.AAIDeclarations;
import org.openecomp.sdnc.sli.aai.AAIRequest;
import org.openecomp.sdnc.sli.aai.AAIService;
import org.openecomp.sdnc.sli.aai.data.AAIDatum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R1607AutoGeneratedTest {

	private static final Logger LOG = LoggerFactory.getLogger(R1607AutoGeneratedTest.class);

	protected static AAIClient client;

	protected Map<String, String> cache = new HashMap<String, String>();

	@BeforeClass
	public static void setUp() throws Exception {
//		super.setUp();
		URL url = AAIService.class.getResource(AAIService.AAICLIENT_PROPERTIES);
		client = new AAIService(url);
		LOG.info("\nTaicAAIResourceTest.setUp\n");
	}

	@AfterClass
	public static void tearDown() throws Exception {
//		super.tearDown();
		client = null;
		LOG.info("----------------------- AAIResourceTest.tearDown -----------------------");
	}


//	@Test
	public void test01AutoGeneratedRequest() {

		String[] requestDefinition = {
				"query|generic-vnf|generic-vnf.vnf-id:assign:value:bnfm0001v-1147"
		};

		for(String line : requestDefinition){
			// parse request line resource | key structure
			String[] segments = line.split("\\|");
			String action = segments[0];
			String resource = segments[1];
			String[] tmpKeys = segments[2].split("&");


			String localId = null;

			List<String> keys = new ArrayList<String>();
			String keyLine = null;

			for(String instruction : tmpKeys) {
				String[] parts = instruction.split(":");
				String identifier = parts[0];
				String method = parts[2];

				if(identifier.startsWith(resource)) {
					localId = identifier;
				} else if(identifier.startsWith("l-interface") && "l2-bridge-bgf".equals(resource)) {
					localId = identifier;
				} else if(identifier.startsWith("l-interface") && "l2-bridge-sbg".equals(resource)) {
					localId = identifier;
				}

				switch(parts[1]) {
				case "assign":
					String postProcesss = parts[3];
					keyLine = processAssign(identifier, method, postProcesss);
					if(keyLine != null && !keyLine.trim().isEmpty()) {
						keys.add(keyLine);
					}
					break;
				case "cached":
					keyLine = processCached(identifier, method);
					if(keyLine != null && !keyLine.trim().isEmpty()) {
						keys.add(keyLine);
					}
					break;
				}

			}

			switch(action){
			case "save":
				List<String> x = Arrays.asList(localId.split("\\."));

				testAutoGeneratedSaveRequest(resource, keys, x.get(x.size() - 1), cache.get(localId));
				break;
			case "query":
				test03AutoGeneratedQueryRequest(resource, keys);
				break;
			case "delete":
				test03AutoGeneratedDeleteRequest(resource, keys);
				break;
			}
		}

		LOG.info("done");
	}


	public void testAutoGeneratedSaveRequest(String resource, List<String> requestKeys, String identifier, String idValue) {
		LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

		try
		{
			Map<String, String> nameValues = keyToHashMap(StringUtils.join(requestKeys, " AND "), new SvcLogicContext());
			AAIRequest request = AAIRequest.createRequest(resource, nameValues);
			Class<AAIDatum> resourceClass = (Class<AAIDatum>) (request==null ? GenericVnf.class : request.getModelClass());

			Map<String, String> data = new HashMap<String, String>();

			for(Field field : resourceClass.getDeclaredFields()) {
				String type = field.getType().getName();
				if(type.startsWith("java.lang.")){
					Annotation[] fieldAnnotations = field.getAnnotations();
					for(int i = 0; i < fieldAnnotations.length; i++) {
						Annotation a = fieldAnnotations[i];
						if(a instanceof JsonProperty){
							JsonProperty pa = (JsonProperty)a;
							String name = pa.value();
							String value;
							switch(type) {
							case "java.lang.Integer":
							case "java.lang.Long":
								value = RandomStringUtils.random(6, false, true);
								break;
							case "java.lang.Boolean":
								value = "false";
								break;
							default:
								if(name.equals(identifier)) {
									value = idValue;
								} else {
									value = RandomStringUtils.random(10, true, false);
								}
							}
							data.put(name, value);
						} else
							if(a instanceof javax.xml.bind.annotation.XmlElement) {
								XmlElement xe = (XmlElement)a;
								String name = xe.name();
								if("link-type".equals(name)){
									data.put(name, "roadmTail");
									continue;
								}
								if("operational-status".equals(name)){
									data.put(name, "available");
									continue;
								}
								String value;
								switch(type) {
								case "java.lang.Integer":
								case "java.lang.Long":
									value = RandomStringUtils.random(6, false, true);
									break;
								case "java.lang.Boolean":
									value = "false";
									break;
								default:
									if(name.equals(identifier)) {
										value = idValue;
									} else {
										value = RandomStringUtils.random(10, true, false);
									}
								}
								data.put(name, value);
							}
					}
				}
			}

			SvcLogicContext ctx = new SvcLogicContext();

			data.remove("resource-version");

			QueryStatus resp = null;

			//(String resource, boolean force, boolean localOnly, String key, Map<String, String> parms, String prefix,	SvcLogicContext ctx)
			resp = client.save(resource, false, false, StringUtils.join(requestKeys, " AND "), data, "aaidata", ctx);
		}
		catch (Throwable e)
		{
			LOG.error("Caught exception", e);
			fail("Caught exception");
		}
	}

	public void test03AutoGeneratedQueryRequest(String resource, List<String> requestKeys) {
		LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

		SvcLogicContext ctx = new SvcLogicContext();
		try
		{

			QueryStatus response = null;

			response = client.query(resource,  false, null, StringUtils.join(requestKeys, " AND "), "aaiTest", null, ctx);
		}
		catch (Throwable e)
		{
			LOG.error("Caught exception", e);
			fail("Caught exception");
		}
	}


	public void test03AutoGeneratedDeleteRequest(String resource, List<String> requestKeys) {
		LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

		try
		{
			SvcLogicContext ctx = new SvcLogicContext();

			QueryStatus response = null;

			response = client.delete(resource, StringUtils.join(requestKeys, " AND "),  ctx);
		}
		catch (Throwable e)
		{
			LOG.error("Caught exception", e);
			fail("Caught exception");
		}
	}

	static ArrayList<Method> findSetters(Class<?> c) {
	   ArrayList<Method> list = new ArrayList<Method>();
	   Method[] methods = c.getDeclaredMethods();
	   for (Method method : methods)
	      if (isGetter(method))
	         list.add(method);
	   return list;
	}


	public static boolean isGetter(Method method) {
	   if (Modifier.isPublic(method.getModifiers()) &&
	      method.getParameterTypes().length == 0) {
	         if (method.getName().matches("^get[A-Z].*") &&
	            !method.getReturnType().equals(void.class))
	               return true;
	         if (method.getName().matches("^is[A-Z].*") &&
	            method.getReturnType().equals(boolean.class))
	               return true;
	   }
	   return false;
	}

	public static boolean isSetter(Method method) {
	   return Modifier.isPublic(method.getModifiers()) &&
	      method.getReturnType().equals(void.class) &&
	         method.getParameterTypes().length == 1 &&
	            method.getName().matches("^set[A-Z].*");
	}

	private String processAssign(String identifier, String method, String postProcess) {
		String value = null;
		if("uuid".equals(method)) {
			value = UUID.randomUUID().toString();
		}

		if("cache".equals(postProcess)) {
			cache.put(identifier, value);
		}

		if("value".equals(method)) {
			cache.put(identifier, postProcess);
			value = postProcess;
		}

		String key = String.format("%s = '%s'", identifier, value);
		return key;
	}

	private String processCached(String identifier, String method) {
		String value = cache.get(identifier);

		String key = String.format("%s = '%s'", identifier, value);
		return key;
	}

	protected HashMap<String,String> keyToHashMap(String key,	SvcLogicContext ctx) {
		if (key == null) {
			return (null);
		}

		LOG.debug("Converting key [" + key + "] to where clause");

		if (key.startsWith("'") && key.endsWith("'")) {
			key = key.substring(1, key.length() - 1);

			LOG.debug("Stripped outer single quotes - key is now [" + key + "]");
		}

		String[] keyTerms = key.split("\\s+");

		StringBuffer whereBuff = new StringBuffer();
		String term1 = null;
		String op = null;
		String term2 = null;
		HashMap<String, String> results = new HashMap<String, String>();

		for (int i = 0; i < keyTerms.length; i++) {
			if (term1 == null) {
				if ("and".equalsIgnoreCase(keyTerms[i])
						|| "or".equalsIgnoreCase(keyTerms[i])) {
					// Skip over ADD/OR
				} else {
					term1 = resolveTerm(keyTerms[i], ctx);
				}
			} else if (op == null) {
				if ("==".equals(keyTerms[i])) {
					op = "=";
				} else {
					op = keyTerms[i];
				}
			} else {
				term2 = resolveTerm(keyTerms[i], ctx);
				term2 = term2.trim().replace("'", "").replace("$", "").replace("'", "");
				results.put(term1,  term2);

				term1 = null;
				op = null;
				term2 = null;
			}
		}

		return (results);
	}

	private String resolveTerm(String term, SvcLogicContext ctx) {
		if (term == null) {
			return (null);
		}

		LOG.debug("resolveTerm: term is " + term);

		if (term.startsWith("$") && (ctx != null)) {
			// Resolve any index variables.

			return ("'" + resolveCtxVariable(term.substring(1), ctx) + "'");
		} else if (term.startsWith("'") || term.startsWith("\"")) {
			return (term);
		} else {
			return (term.replaceAll("-", "_"));

		}

	}

	private String resolveCtxVariable(String ctxVarName, SvcLogicContext ctx) {

		if (ctxVarName.indexOf('[') == -1) {
			// Ctx variable contains no arrays
			return (ctx.getAttribute(ctxVarName));
		}

		// Resolve any array references
		StringBuffer sbuff = new StringBuffer();
		String[] ctxVarParts = ctxVarName.split("\\[");
		sbuff.append(ctxVarParts[0]);
		for (int i = 1; i < ctxVarParts.length; i++) {
			if (ctxVarParts[i].startsWith("$")) {
				int endBracketLoc = ctxVarParts[i].indexOf("]");
				if (endBracketLoc == -1) {
					// Missing end bracket ... give up parsing
					LOG.warn("Variable reference " + ctxVarName
							+ " seems to be missing a ']'");
					return (ctx.getAttribute(ctxVarName));
				}

				String idxVarName = ctxVarParts[i].substring(1, endBracketLoc);
				String remainder = ctxVarParts[i].substring(endBracketLoc);

				sbuff.append("[");
				sbuff.append(ctx.getAttribute(idxVarName));
				sbuff.append(remainder);

			} else {
				// Index is not a variable reference
				sbuff.append("[");
				sbuff.append(ctxVarParts[i]);
			}
		}

		return (ctx.getAttribute(sbuff.toString()));
	}

//	@Test
	public void test04VceDataPost() {
		LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

		try
		{
			URL resource = this.getClass().getResource("/json/tails4.json");

			LOG.info("Resource is " + resource.getFile());
			File requestFile = new File(resource.getFile());
			if(!requestFile.exists()) {
				fail("Test file does not exist");
			}
			SvcLogicContext ctx = new SvcLogicContext();
		    ObjectMapper mapper = AAIService.getObjectMapper();
		    InventoryResponseItems request = mapper.readValue(requestFile, InventoryResponseItems.class);
		    Map<String, Object> subnetsList = mapper.convertValue(request, Map.class);
		    AAIDeclarations.class.cast(client).writeMap(subnetsList,  "aaiTmp", ctx);
		    assertNotNull(request);

		}
		catch (Exception e)
		{
			LOG.error("Caught exception", e);
			fail("Caught exception");
		}
	}

static class MySchemaOutputResolver extends SchemaOutputResolver {

	    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
	        File file = new File(suggestedFileName);
	        StreamResult result = new StreamResult(file);
	        result.setSystemId(file.getAbsolutePath());
	        return result;
	    }

	}
}
