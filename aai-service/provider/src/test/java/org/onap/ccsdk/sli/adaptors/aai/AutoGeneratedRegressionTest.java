/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights
 *                         reserved.
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

package org.onap.ccsdk.sli.adaptors.aai;

import static org.junit.Assert.assertTrue;
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
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.onap.ccsdk.sli.adaptors.aai.data.AAIDatum;
import org.onap.ccsdk.sli.core.sli.SvcLogicContext;
import org.onap.ccsdk.sli.core.sli.SvcLogicResource.QueryStatus;
import org.onap.aai.inventory.v21.GenericVnf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AutoGeneratedRegressionTest {

    static {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO");
        System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, String.format("AutoGeneratedRegressionTest-%d.txt", System.currentTimeMillis()));
    }
    private static final Logger LOG = LoggerFactory.getLogger(AutoGeneratedRegressionTest.class);


    protected static AAIClient client;

    protected Map<String, String> cache = new HashMap<String, String>();

    @BeforeClass
    public static void setUp() throws Exception {
        URL url = AAIService.class.getResource(AAIService.AAICLIENT_PROPERTIES);
        client = new AAIService(url);
        LOG.info("----------------------- aicAAIResourceTest.setUp -----------------------");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client = null;
        LOG.info("----------------------- AAIResourceTest.tearDown -----------------------");
    }


    @Test
    public void mainLoadTest ()
    {
        try
        {
            String currentDir = System.getProperty("user.dir");
            File dir = new File(currentDir);
            if(!dir.exists()) {
                System.exit(1);
            }
            dir = new File(dir, "src/main/resources");
            if(!dir.exists()) {
                System.exit(1);
            }
            // parse the document
            File file = new File(dir, "aai_schema_v11.xsd");
            if(!file.exists()) {
                assert(false);
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            Stack<String> stack = new Stack<String>();
            List<String> commands = new ArrayList<String>();
            listSubNodes("network", doc, stack, commands);
            listSubNodes("cloud-infrastructure", doc, stack, commands);
            listSubNodes("service-design-and-creation", doc, stack, commands);
            listSubNodes("license-management", doc, stack, commands);
            listSubNodes("business", doc, stack, commands);

            String[] requestDefinition = commands.toArray(new String[0]);
            LOG.info("\n----------------------------");

            executeTests(requestDefinition);
        }
        catch (Throwable e)
        {
            LOG.error("", e);
        }
    }

    @Test
    public void testFromFile() {
        String filename = "testCommands.txt";
        List<String> lines = new ArrayList<String>();
        Scanner scanner = null;

        try {
            File testFile = new File(filename);
            if(!testFile.exists())
                return;
            scanner = new Scanner(testFile);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (Exception exc) {

        } finally {
            if(scanner != null)
                scanner.close();
        }

        String[] requestDefinition = lines.toArray(new String[0]);
        executeTests(requestDefinition);
    }


    @Test
    public void test01AutoGeneratedRequest() {

        String[] requestDefinition = {
                "save|generic-vnf|generic-vnf.vnf-id:assign:value:adb28ac0-a260-4b7d-9ce5-adf15733c458",
                "update|generic-vnf|generic-vnf.vnf-id:assign:value:adb28ac0-a260-4b7d-9ce5-adf15733c458",
                "query|generic-vnf|generic-vnf.vnf-id:assign:value:adb28ac0-a260-4b7d-9ce5-adf15733c458",
                "delete|generic-vnf|generic-vnf.vnf-id:assign:value:adb28ac0-a260-4b7d-9ce5-adf15733c458"
        };

        executeTests(requestDefinition);
    }

    public void executeTests(String[] requestDefinition) {

        for(String line : requestDefinition){
            LOG.info("Executing: " + line);

            // parse request line resource | key structure
            String[] segments = line.split("\\|");
            String action = segments[0];
            String resource = segments[1];
            String[] tmpKeys = segments[2].split("&");
            // String array keyStructure can be tmpKey.
            // options    :assign:uuid:cache
            //            :cached
            //            :query:random


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
                } else if("nodes-query".equals(resource)) {
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

            List<String> x = Arrays.asList(localId.split("\\."));
            switch(action){
            case "save":
                testAutoGeneratedSaveRequest(resource, keys, x.get(x.size() - 1), cache.get(localId));
                break;
            case "update":
                testAutoGeneratedUpdateRequest(resource, keys, x.get(x.size() - 1), cache.get(localId));
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
                switch(type){
                case "bool":
                case "boolean":
                    type = Boolean.class.getName();
                    break;
                case "int":
                    type = Integer.class.getName();
                    break;
                case "long":
                    type = Long.class.getName();
                    break;

                }
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
                                if("model-version-id".equals(name)) {
                                    continue;
                                }
                                if("model-invariant-id".equals(name)) {
                                    continue;
                                }
                                if("link-type".equals(name)){
                                    data.put(name, "roadmTail");
                                    continue;
                                }
                                if("operational-status".equals(name)){
                                    data.put(name, "available");
                                    continue;
                                }
                                if(name.equals(identifier)) {
                                    data.put(name, idValue);
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

            //(String resource, boolean force, boolean localOnly, String key, Map<String, String> parms, String prefix,    SvcLogicContext ctx)
            resp = client.save(resource, false, false, StringUtils.join(requestKeys, " AND "), data, "aaidata", ctx);
            if(resp == QueryStatus.SUCCESS) {
                LOG.info(String.format("Save %s successfull", resource));
            } else {
                LOG.info(String.format("Save %s failed due to : %s", resource, ctx.getAttribute("aaidata.error.message")));
            }
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
            if(response == QueryStatus.SUCCESS) {
                LOG.info(String.format("Query %s successfull", resource));
                Set<String> tokens = ctx.getAttributeKeySet();
                Map<String, String> reponseData = new HashMap<String, String>();

                String responsePrefix = String.format("%s", "aaiTest");

                for(String token : tokens) {
                    if(token.startsWith(responsePrefix)){
                        reponseData.put(token, ctx.getAttribute(token));
                    } else {
                        LOG.info(String.format("%s = ", token, ctx.getAttribute(token)));
                    }
                }

                LOG.info("AAIResponse: " + response.toString());
                assertTrue("AAIRequest:"+resource, reponseData.size() > 0);
            } else {
                LOG.info(String.format("Query %s failed due to : %s", resource, ctx.getAttribute("aaidata.error.message")));
                assert(false);
            }
        }
        catch (Throwable e)
        {
            LOG.error("Caught exception", e);
        }
    }


    public void test03AutoGeneratedDeleteRequest(String resource, List<String> requestKeys) {
        LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

        try
        {
            SvcLogicContext ctx = new SvcLogicContext();

            QueryStatus response = null;

            response = client.delete(resource, StringUtils.join(requestKeys, " AND "),  ctx);
            if(response == QueryStatus.SUCCESS) {
                LOG.info(String.format("Delete %s successfull", resource));
            } else {
                LOG.info(String.format("Delete %s failed due to : %s", resource, ctx.getAttribute("aaiDelete.error.message")));
            }
        }
        catch (Throwable e)
        {
            LOG.error("Caught exception", e);
            fail("Caught exception");
        }
    }

    public void testAutoGeneratedUpdateRequest(String resource, List<String> requestKeys, String identifier, String idValue) {
        LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

        try
        {
            Map<String, String> nameValues = keyToHashMap(StringUtils.join(requestKeys, " AND "), new SvcLogicContext());
            AAIRequest request = AAIRequest.createRequest(resource, nameValues);
            Class<AAIDatum> resourceClass = (Class<AAIDatum>) (request==null ? GenericVnf.class : request.getModelClass());

            Map<String, String> data = new HashMap<String, String>();

            boolean skipFirst = true;
            boolean breakLoop = false;
            for(Field field : resourceClass.getDeclaredFields()) {
                if(skipFirst){
                    skipFirst = false;
                    continue;
                }
                if(breakLoop){
                    break;
                }
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
                            breakLoop = true;
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
                                breakLoop = true;
                            }
                    }
                }
            }

            SvcLogicContext ctx = new SvcLogicContext();

            data.remove("resource-version");

            QueryStatus resp = null;

            //client.update("ipsec-configuration", "ipsec-configuration.ipsec-configuration-id = 'testConfigurationId01'", data, "aaiTest",  ctx);
            resp = client.update(resource, StringUtils.join(requestKeys, " AND "), data, "aaidata", ctx);
            if(resp == QueryStatus.SUCCESS) {
                LOG.info(String.format("Update %s successfull", resource));
            } else {
                LOG.info(String.format("Update %s failed due to : %s", resource, ctx.getAttribute("aaidata.error.message")));
            }

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

    protected HashMap<String,String> keyToHashMap(String key,    SvcLogicContext ctx) {
        if (key == null) {
            return (null);
        }

        LOG.debug("Converting key [" + key + "] to where clause");

        if (key.startsWith("'") && key.endsWith("'")) {
            key = key.substring(1, key.length() - 1);

            LOG.debug("Stripped outer single quotes - key is now [" + key + "]");
        }

        String[] keyTerms = key.split("\\s+");

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

//    @Test
    public void test90QueryTenantRequest()
    {
        LOG.info("----------------------- Test: " + new Object(){}.getClass().getEnclosingMethod().getName() + " -----------------------");

        URL url;

        try {
            url = client.requestVserverURLNodeQuery("bpsx0001vm001bps001");
            url = new URL("https://mtanjv9aaas03.aic.cip.att.com:8443/aai/v4/cloud-infrastructure/tenants/tenant/6b012c07bdf1427190ae58f794a86344/vservers/vserver/5acfe828-82e9-swgk092815-13-4d2c-85bb-9c2c1fafcce6");
            client.getTenantIdFromVserverUrl(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    private static void listSubNodes(String parent, Document doc, Stack<String> stack, List<String> commands) throws Exception {
        XPathFactory xFactory = XPathFactory.newInstance();
        XPath xPath = xFactory.newXPath();
        String format = "/schema/element[@name='%s']/complexType/sequence/element[@ref]";

        String path = String.format(format, parent);

//        XPathExpression exp = xPath.compile("/schema/element[@name='generic-vnf']/complexType/sequence/element[@ref]");
        XPathExpression exp = xPath.compile(path);

        NodeList nl = (NodeList)exp.evaluate(doc.getFirstChild(), XPathConstants.NODESET);
        LOG.debug("Nodes to process : "+nl.getLength());
        for (int index = 0; index < nl.getLength(); index++) {

            Node node = nl.item(index);

            if(!(node instanceof Element))
                continue;

            String classAlias = null;

            if(node.hasAttributes())
            {
                String nm = ((Element)node).getAttribute("ref");
                if(nm != null && !nm.isEmpty()) {
                    String[] split = nm.split(":");
                    classAlias = split[split.length - 1];
                    if("relationship-list".equals(classAlias))
                        continue;
                    if("metadata".equals(classAlias))
                        continue;
                    if("classes-of-service".equals(classAlias)) {
                        classAlias = "class-of-service";
                    } else if("l3-interface-ipv4-address-list".equals(classAlias)) {
                        ;
                    } else if("l3-interface-ipv6-address-list".equals(classAlias)) {
                        ;
                    } else if("cvlan-tags".equals(classAlias)) {
                        classAlias = "cvlan-tag-entry";
                    } else if("network-policies".equals(classAlias)) {
                        classAlias = "network-policy";
                    } else if("complexes".equals(classAlias)) {
                        classAlias = "complex";
                    } else if("dvs-switches".equals(classAlias)) {
                        classAlias = "dvs-switch";
                    } else if("service-capabilities".equals(classAlias)) {
                        classAlias = "service-capability";
                    } else {
                        classAlias = classAlias.substring(0, classAlias.length() -1);
                    }
                    AAIRequest request = AAIRequest.createRequest(classAlias, new HashMap<String, String>());
                    if(request != null) {
                        Class<?> clazz = request.getModelClass();
                        Field[] fieldz = clazz.getDeclaredFields();
                        Field field = fieldz[0];
                        String fieldName = field.getName();
                        XmlElement annotation = field.getAnnotation(XmlElement.class);

                        Map<String, Object> map = AnnotationUtils.getAnnotationAttributes(annotation);
                        String id = (String)map.get("name");
                        if("##default".equals(id)) {
                            id = fieldName;
                        }

                        if("cloud-region".equals(classAlias)) {
                            String keystring = "cloud-region.cloud-owner:assign:value:att-aic&cloud-region.cloud-region-id:assign:value:AAIAIC25";
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);
                            listSubNodes(classAlias, doc, stack, commands);
                            stack.pop();
                        } else if("entitlement".equals(classAlias)) {
                            String keystring = "entitlement.group-uuid:assign:value:"+UUID.randomUUID()+"&entitlement.resource-uuid:assign:value:"+UUID.randomUUID();
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);
                            listSubNodes(classAlias, doc, stack, commands);
                            stack.pop();
                        } else if("license".equals(classAlias)) {
                            String keystring = "license.group-uuid:assign:value:"+UUID.randomUUID()+"&license.resource-uuid:assign:value:"+UUID.randomUUID();
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);
                            listSubNodes(classAlias, doc, stack, commands);
                            stack.pop();
                        } else if("route-target".equals(classAlias)) {
                            String keystring = "route-target.global-route-target:assign:value:"+UUID.randomUUID()+"&route-target.route-target-role:assign:value:"+UUID.randomUUID();
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);
                            listSubNodes(classAlias, doc, stack, commands);
                            stack.pop();
                        } else if("service-capability".equals(classAlias)) {
                            String keystring = "service-capability.service-type:assign:value:"+UUID.randomUUID()+"&service-capability.vnf-type:assign:value:"+UUID.randomUUID();
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);
                            listSubNodes(classAlias, doc, stack, commands);
                            stack.pop();
                        } else if("ctag-pool".equals(classAlias)) {
                            String keystring = "ctag-pool.target-pe:assign:value:"+UUID.randomUUID()+"&ctag-pool.availability-zone-name:assign:value:"+UUID.randomUUID();
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);
                            listSubNodes(classAlias, doc, stack, commands);
                            stack.pop();
                        } else {
                            String keystring = String.format("%s.%s:assign:value:%s", classAlias, id, UUID.randomUUID());
                            stack.push(keystring);
                            String[] array = stack.toArray(new String[0]);
                            String key = StringUtils.join(array, "&");

                            String save = String.format("save|%s|%s", classAlias, key);
                            commands.add(save);

                            String query = String.format("query|%s|%s", classAlias, key);
                            commands.add(query);

                            String update = String.format("update|%s|%s", classAlias, key);
                            commands.add(update);

                            if(!parent.equals(classAlias) && !containsCircular(classAlias, id, stack)) {
                                listSubNodes(classAlias, doc, stack, commands);
                            }
                            String delete = String.format("delete|%s|%s", classAlias, key);
                            commands.add(delete);
                            stack.pop();
                        }
                    }
                }
            }
        }
    }

    public static boolean containsCircular(String classAlias, String id, Stack<String> stack) {
        String keystring = String.format("%s.%s", classAlias, id);

        Stack<String> localStack = new Stack<String>();
        localStack.addAll(stack);

        localStack.pop();

        while(!localStack.isEmpty()) {
            String instruction = localStack.pop();
            if(instruction.contains(keystring)) {
                return true;
            }
        }

        return false;
    }
}
