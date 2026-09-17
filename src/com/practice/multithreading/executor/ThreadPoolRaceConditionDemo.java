package com.practice.multithreading.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Proves that Executors DO NOT prevent race conditions on shared data.
 * 
 * Both counters receive 1,000 increments across 4 worker threads:
 * - unsafeCount++: Corrupts data because raw integer operations are not atomic.
 * - safeCount.incrementAndGet(): Stays accurate because AtomicInteger handles synchronization.
 */
public class ThreadPoolRaceConditionDemo {

    private static int unsafeCount = 0;
    private static final AtomicInteger safeCount = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 1000; i++) {
            pool.execute(() -> {
                unsafeCount++;               //  RACE CONDITION! Multiple threads read/write at once
                safeCount.incrementAndGet(); //  THREAD-SAFE! Atomic operation
            });
        }

        pool.shutdown();
        pool.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("Expected Count: 1000");
        System.out.println("Unsafe Count:   " + unsafeCount + " (Corrupted! Lost updates due to race condition)");
        System.out.println("Safe Count:     " + safeCount.get() + " (Accurate! Protected by AtomicInteger)");
    }
}
