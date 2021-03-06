/*-
 * ============LICENSE_START=======================================================
 * openECOMP : SDN-C
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights
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

package org.onap.ccsdk.sli.adaptors.ra.comp;

import java.util.List;

public interface EndPointAllocator {

    List<ResourceData> allocateResources(String serviceModel, ResourceEntity resourceEntity,
            ResourceTarget resourceTarget, ResourceRequest resourceRequest, boolean checkOnly, boolean change);

    List<ResourceData> getResourcesForEntity(String resourceEntityType, String resourceEntityId,
            String resourceEntityVersion);

    ResourceData getResource(String resourceTargetType, String resourceTargetId, String resourceName,
            String resourceEntityTypeFilter, String resourceEntityIdFilter, String resourceShareGroupFilter);

    List<ResourceData> getResourcesForTarget(String resourceTargetTypeFilter, String resourceTargetIdFilter,
            String resourceName);
}
