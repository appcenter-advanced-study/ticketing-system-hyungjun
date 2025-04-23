package com.appcenter.wnt.repository.namedlock;

import java.util.function.Supplier;

public interface NamedLockRepository {
    <T> T executeWithLock(String key, Supplier<T> supplier);
}