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

package org.onap.ccsdk.sli.adaptors.lock.comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.onap.ccsdk.sli.adaptors.lock.dao.ResourceLockDao;
import org.onap.ccsdk.sli.adaptors.lock.data.ResourceLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockHelperImpl implements LockHelper {

    private static final Logger log = LoggerFactory.getLogger(LockHelperImpl.class);

    private ResourceLockDao resourceLockDao;
    private int retryCount = 10;
    private int lockWait = 5; // Seconds

    @Override
    public void lock(String resourceName, String lockRequester, int lockTimeout /* Seconds */) {
        lock(resourceName, lockRequester, lockTimeout, lockWait, retryCount);
    }

    @Override
    public void lock(String resourceName, String lockRequester, int lockTimeout /* Seconds */,
            int lockWait /* Seconds */, int retryCount) {
        lock(Collections.singleton(resourceName), lockRequester, lockTimeout, lockWait, retryCount);
    }

    @Override
    public void unlock(String resourceName, boolean force) {
        unlock(Collections.singleton(resourceName), force);
    }

    @Override
    public void lock(Collection<String> resourceNameList, String lockRequester, int lockTimeout /* Seconds */) {
        lock(resourceNameList, lockRequester, lockTimeout, lockWait, retryCount);
    }

    @Override
    public void lock(Collection<String> resourceNameList, String lockRequester, int lockTimeout /* Seconds */,
            int lockWait /* Seconds */, int retryCount) {
        for (int i = 0; true; i++) {
            try {
                tryLock(resourceNameList, lockRequester, lockTimeout);
                log.info("Resources locked: " + resourceNameList);
                return;
            } catch (ResourceLockedException e) {
                if (i >= retryCount) {
                    throw e;
                }
                try {
                    Thread.sleep(lockWait * 1000);
                } catch (InterruptedException ex) {
                    log.error("Interrupted Exception", ex);
                }
            }
        }
    }

    @Override
    public void unlock(Collection<String> lockNames, boolean force) {
        if (lockNames == null || lockNames.isEmpty()) {
            return;
        }

        try {
            for (String name : lockNames) {
                ResourceLock l = resourceLockDao.getByResourceName(name);
                if (l != null) {
                    if (force || l.lockCount == 1) {
                        resourceLockDao.delete(l.id);
                    } else {
                        resourceLockDao.decrementLockCount(l.id);
                    }
                }
            }

            resourceLockDao.commit();

            log.info("Resources unlocked: " + lockNames);
        } finally {
            resourceLockDao.rollback();
        }
    }

    public void tryLock(Collection<String> resourceNameList, String lockRequester, int lockTimeout /* Seconds */) {
        if (resourceNameList == null || resourceNameList.isEmpty()) {
            return;
        }

        lockRequester = generateLockRequester(lockRequester, 100);

        // First check if all requested records are available to lock

        Date now = new Date();

        try {
            List<ResourceLock> dbLockList = new ArrayList<>();
            List<String> insertLockNameList = new ArrayList<>();
            for (String name : resourceNameList) {
                ResourceLock l = resourceLockDao.getByResourceName(name);

                boolean canLock = l == null || now.getTime() > l.expirationTime.getTime() ||
                        lockRequester != null && lockRequester.equals(l.lockHolder) || l.lockCount <= 0;
                if (!canLock) {
                    throw new ResourceLockedException(l.resourceName, l.lockHolder, lockRequester);
                }

                if (l != null) {
                    if (now.getTime() > l.expirationTime.getTime() || l.lockCount <= 0) {
                        l.lockCount = 0;
                    }
                    dbLockList.add(l);
                } else {
                    insertLockNameList.add(name);
                }
            }

            // Update the lock info in DB
            for (ResourceLock l : dbLockList) {
                resourceLockDao.update(l.id, lockRequester, now, new Date(now.getTime() + lockTimeout * 1000), l.lockCount + 1);
            }

            // Insert records for those that are not yet there
            for (String lockName : insertLockNameList) {
                ResourceLock l = new ResourceLock();
                l.resourceName = lockName;
                l.lockHolder = lockRequester;
                l.lockTime = now;
                l.expirationTime = new Date(now.getTime() + lockTimeout * 1000);
                l.lockCount = 1;

                try {
                    resourceLockDao.add(l);
                } catch (Exception e) {
                    log.info("Failed to insert lock record: " + lockName);
                    throw new ResourceLockedException(l.resourceName, "unknown", lockRequester);
                }
            }

            resourceLockDao.commit();

        }finally

        {
            resourceLockDao.rollback();
        }
    }

    private static String generateLockRequester(String name, int maxLength) {
        if (name == null) {
            name = "";
        }
        int l1 = name.length();
        String tname = Thread.currentThread().getName();
        int l2 = tname.length();
        if (l1 + l2 + 1 > maxLength) {
            int maxl1 = maxLength / 2;
            if (l1 > maxl1) {
                name = name.substring(0, maxl1);
                l1 = maxl1;
            }
            int maxl2 = maxLength - l1 - 1;
            if (l2 > maxl2) {
                tname = tname.substring(0, 6) + "..." + tname.substring(l2 - maxl2 + 9);
            }
        }
        return tname + '-' + name;
    }

    public void setResourceLockDao(ResourceLockDao resourceLockDao) {
        this.resourceLockDao = resourceLockDao;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public void setLockWait(int lockWait /* Seconds */) {
        this.lockWait = lockWait;
    }
}
