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

package org.openecomp.sdnc.sli.aai.data.v1507;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "flavorId",
    "flavorName",
    "flavorLink"
})
public class Flavor {

    @JsonProperty("flavorId")
    private String flavorId;
    @JsonProperty("flavorName")
    private String flavorName;
    @JsonProperty("flavorLink")
    private String flavorLink;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The flavorId
     */
    @JsonProperty("flavorId")
    public String getFlavorId() {
        return flavorId;
    }

    /**
     * 
     * @param flavorId
     *     The flavorId
     */
    @JsonProperty("flavorId")
    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId;
    }

    /**
     * 
     * @return
     *     The flavorName
     */
    @JsonProperty("flavorName")
    public String getFlavorName() {
        return flavorName;
    }

    /**
     * 
     * @param flavorName
     *     The flavorName
     */
    @JsonProperty("flavorName")
    public void setFlavorName(String flavorName) {
        this.flavorName = flavorName;
    }

    /**
     * 
     * @return
     *     The flavorLink
     */
    @JsonProperty("flavorLink")
    public String getFlavorLink() {
        return flavorLink;
    }

    /**
     * 
     * @param flavorLink
     *     The flavorLink
     */
    @JsonProperty("flavorLink")
    public void setFlavorLink(String flavorLink) {
        this.flavorLink = flavorLink;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
