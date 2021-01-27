package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.locks.components;

import net.thevpc.scholar.hadrumaths.concurrent.AppLock;

import java.util.Date;

class AppLockInfo {
    AppLock lock;
    long hits;
    Date time;
}
