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

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.onap.ccsdk.sli.adaptors.rm.data.AllocationItem;
import org.onap.ccsdk.sli.adaptors.rm.data.Range;
import org.onap.ccsdk.sli.adaptors.rm.data.RangeAllocationItem;
import org.onap.ccsdk.sli.adaptors.rm.data.RangeAllocationRequest;
import org.onap.ccsdk.sli.adaptors.rm.data.RangeResource;
import org.onap.ccsdk.sli.adaptors.rm.data.ResourceKey;
import org.onap.ccsdk.sli.adaptors.rm.data.ResourceType;

public class RangeUtil {

    public static void recalculate(RangeResource r) {
        r.used = new TreeSet<>();
        if (r.allocationItems != null) {
            for (AllocationItem ai : r.allocationItems) {
                RangeAllocationItem rai = (RangeAllocationItem) ai;
                if (rai.used != null) {
                    r.used.addAll(rai.used);
                }
            }
        }
    }

    public static boolean checkRange(RangeResource r, RangeAllocationRequest req, int num) {
        if (req.excludeNumbers != null && req.excludeNumbers.contains(num)) {
            return false;
        }

        if (req.rangeList != null && !req.rangeList.isEmpty()) {
            boolean good = false;
            for (Range range : req.rangeList) {
                if (num < range.min || num > range.max) {
                    continue;
                }

                boolean found = false;
                if (r.allocationItems != null) {
                    for (AllocationItem ai : r.allocationItems) {
                        RangeAllocationItem rai = (RangeAllocationItem) ai;
                        if (!eq(req.resourceUnionId, rai.resourceUnionId) && rai.used != null
                                && rai.used.contains(num)) {
                            if (!overlap(rai.resourceShareGroupList, req.resourceShareGroupList)) {
                                found = true;
                                break;
                            }
                        }
                        if (!req.replace && eq(req.resourceSetId, rai.resourceSetId) && rai.used != null
                                && rai.used.contains(num)) {
                            found = true;
                            break;
                        }
                        if (req.forceNewNumbers && rai.used.contains(num)) {
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    good = true;
                    break;
                }
            }

            return good;
        }

        return true;
    }

    public static SortedSet<Integer> getUsed(RangeResource r, String resourceUnionId) {
        SortedSet<Integer> used = new TreeSet<>();
        if (r.allocationItems != null) {
            for (AllocationItem ai : r.allocationItems) {
                RangeAllocationItem rai = (RangeAllocationItem) ai;
                if (eq(resourceUnionId, rai.resourceUnionId) && rai.used != null) {
                    used.addAll(rai.used);
                }
            }
        }
        return used;
    }

    public static void allocateRange(RangeResource rr, SortedSet<Integer> requestedNumbers,
            RangeAllocationRequest req) {
        if (!req.allocate) {
            return;
        }

        RangeAllocationItem rai = (RangeAllocationItem) ResourceUtil.getAllocationItem(rr, req.resourceSetId);
        if (rai == null) {
            rai = new RangeAllocationItem();
            rai.resourceType = ResourceType.Range;
            rai.resourceKey = new ResourceKey();
            rai.resourceKey.assetId = req.assetId;
            rai.resourceKey.resourceName = req.resourceName;
            rai.applicationId = req.applicationId;
            rai.resourceSetId = req.resourceSetId;
            rai.resourceUnionId = req.resourceUnionId;
            rai.resourceShareGroupList = req.resourceShareGroupList;
            rai.used = requestedNumbers;

            if (rr.allocationItems == null) {
                rr.allocationItems = new ArrayList<>();
            }
            rr.allocationItems.add(rai);
        } else if (req.replace) {
            rai.used = requestedNumbers;
        } else {
            rai.used.addAll(requestedNumbers);
        }

        rai.allocationTime = new Date();

        recalculate(rr);
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    private static boolean overlap(Set<String> s1, Set<String> s2) {
        if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()) {
            return false;
        }
        for (String ss1 : s1) {
            if (s2.contains(ss1)) {
                return true;
            }
        }
        return false;
    }
}
