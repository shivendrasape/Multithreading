package com.practice.multithreading.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Compares the three most common standard thread pool factories:
 * 1. SingleThreadExecutor - Guarantees sequential (FIFO) execution.
 * 2. FixedThreadPool - Fixed number of workers with an unbounded queue.
 * 3. CachedThreadPool - Dynamically grows/shrinks threads for bursty workloads.
 */
public class ThreadPoolTypesDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=================================================");
        System.out.println("1. SINGLE THREAD EXECUTOR (Sequential Guarantee)");
        System.out.println("=================================================");
        demonstrateSingleThreadExecutor();

        System.out.println("\n=================================================");
        System.out.println("2. FIXED THREAD POOL (Bounded Concurrency)");
        System.out.println("=================================================");
        demonstrateFixedThreadPool();

        System.out.println("\n=================================================");
        System.out.println("3. CACHED THREAD POOL (Elastic Worker Creation)");
        System.out.println("=================================================");
        demonstrateCachedThreadPool();
    }

    /**
     * SingleThreadExecutor:
     * - Uses exactly 1 worker thread.
     * - Tasks are guaranteed to execute sequentially in submission order.
     * - If the thread crashes, the executor automatically creates a new one to replace it.
     */
    private static void demonstrateSingleThreadExecutor() throws InterruptedException {
        ExecutorService single = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            single.execute(() -> {
                System.out.println("[Single] Task " + taskId + " executed on: " 
                        + Thread.currentThread().getName());
                sleep(200);
            });
        }

        single.shutdown();
        single.awaitTermination(3, TimeUnit.SECONDS);
    }

    /**
     * FixedThreadPool:
     * - Keeps a fixed number of threads (e.g. 2).
     * - Excess tasks wait in an internal FIFO queue (LinkedBlockingQueue).
     * - Good when you want predictable resource usage.
     */
    private static void demonstrateFixedThreadPool() throws InterruptedException {
        ExecutorService fixed = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 4; i++) {
            final int taskId = i;
            fixed.execute(() -> {
                System.out.println("[Fixed]  Task " + taskId + " executed on: " 
                        + Thread.currentThread().getName());
                sleep(200);
            });
        }

        fixed.shutdown();
        fixed.awaitTermination(3, TimeUnit.SECONDS);
    }

    /**
     * CachedThreadPool:
     * - Creates new threads on-demand if all existing threads are busy.
     * - Reuses threads that are idle.
     * - Terminates threads that have been idle for 60 seconds.
     * - Good for many short-lived tasks; dangerous for long-running blocking tasks.
     */
    private static void demonstrateCachedThreadPool() throws InterruptedException {
        ExecutorService cached = Executors.newCachedThreadPool();

        // Submit 4 tasks simultaneously; cached pool will spin up up to 4 threads
        for (int i = 1; i <= 4; i++) {
            final int taskId = i;
            cached.execute(() -> {
                System.out.println("[Cached] Task " + taskId + " executed on: " 
                        + Thread.currentThread().getName());
                sleep(200);
            });
        }

        cached.shutdown();
        cached.awaitTermination(3, TimeUnit.SECONDS);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
