package com.practice.multithreading.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Real-world comparison: Refactoring FoodOrderProcessorDemo to use ExecutorService.
 * 
 * In the raw-threads version (FoodOrderProcessorDemo):
 * - We had to manually instantiate 4 separate Thread objects.
 * - We had to manually call t.start() on each.
 * - We had to manually call t.join() on each to coordinate completion.
 * - Each order execution spawned and threw away 4 OS threads!
 * 
 * In this Executor version:
 * - A fixed pool of reusable workers handles all order tasks.
 * - Tasks are decoupled from thread management.
 * - Shutdown and synchronization are handled cleanly via the framework.
 */
public class FoodOrderProcessorExecutorDemo {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        System.out.println("Order received! Submitting tasks to ExecutorService pool...\n");

        // Reusable pool of 4 worker threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Submit tasks directly to the pool
        executor.execute(() -> {
            sleep(500);
            System.out.printf("[%s] [DB] Order saved.               (500ms)%n", 
                    Thread.currentThread().getName());
        });

        executor.execute(() -> {
            sleep(300);
            System.out.printf("[%s] [EMAIL] Confirmation sent.      (300ms)%n", 
                    Thread.currentThread().getName());
        });

        executor.execute(() -> {
            sleep(400);
            System.out.printf("[%s] [DELIVERY] Partner notified.    (400ms)%n", 
                    Thread.currentThread().getName());
        });

        executor.execute(() -> {
            sleep(200);
            System.out.printf("[%s] [DASHBOARD] Restaurant updated. (200ms)%n", 
                    Thread.currentThread().getName());
        });

        // Gracefully shut down and wait for all tasks to finish (replacing manual join() calls)
        executor.shutdown();

        try {
            boolean completed = executor.awaitTermination(5, TimeUnit.SECONDS);
            if (!completed) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nAll tasks completed! Total time: " 
                + (endTime - startTime) + "ms");
        System.out.println("(Processed in ~500ms concurrently without manual thread management)");
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
