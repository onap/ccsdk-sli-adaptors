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

package org.onap.ccsdk.sli.adaptors.rm.util;

import org.onap.ccsdk.sli.adaptors.rm.data.AllocationItem;
import org.onap.ccsdk.sli.adaptors.rm.data.LabelResource;
import org.onap.ccsdk.sli.adaptors.rm.data.LimitResource;
import org.onap.ccsdk.sli.adaptors.rm.data.RangeResource;
import org.onap.ccsdk.sli.adaptors.rm.data.Resource;
import org.onap.ccsdk.sli.adaptors.rm.data.ResourceType;

public class ResourceUtil {

    public static AllocationItem getAllocationItem(Resource r, String resourceSetId) {
        if (r.allocationItems != null) {
            for (AllocationItem ai : r.allocationItems) {
                if (ai.resourceSetId != null && ai.resourceSetId.equals(resourceSetId)) {
                    return ai;
                }
            }
        }
        return null;
    }

    public static void recalculate(Resource r) {
        if (r == null) {
            return;
        }

        if (r.resourceType == ResourceType.Limit) {
            LimitUtil.recalculate((LimitResource) r);
        } else if (r.resourceType == ResourceType.Range) {
            RangeUtil.recalculate((RangeResource) r);
        } else if (r.resourceType == ResourceType.Label) {
            LabelUtil.recalculate((LabelResource) r);
        }
    }
}
