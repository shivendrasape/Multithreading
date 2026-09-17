package com.practice.multithreading.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates the core concepts of the ExecutorService:
 * 1. Worker Thread Reuse (3 threads processing 6 tasks).
 * 2. Task Queueing (tasks wait in a queue until a worker is free).
 * 3. Thread Pool Lifecycle (shutdown and graceful termination).
 */
public class BasicThreadPoolDemo {

    public static void main(String[] args) {
        System.out.println("Main thread started: " + Thread.currentThread().getName());

        // Step 1: Create a fixed pool with exactly 3 worker threads.
        // Even if we submit 100 tasks, at most 3 OS threads run concurrently.
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Step 2: Submit 6 Runnable tasks using execute()
        for (int i = 1; i <= 6; i++) {
            final int taskId = i;
            executor.execute(() -> {

                String workerName = Thread.currentThread().getName();
                System.out.println("[START] Task " + taskId + " running on " + workerName);

                sleep(800);

                System.out.println("[DONE]  Task " + taskId + " completed by " + workerName);
            });
        }

        System.out.println("\nAll 6 tasks submitted to the executor queue.");

        // Step 3: Proper Graceful Shutdown
        // IMPORTANT: Worker threads in a pool are non-daemon by default.
        // If you don't call shutdown(), the JVM process will never exit!
        System.out.println("Initiating graceful shutdown...");
        executor.shutdown(); // Rejects new submissions; continues existing/queued tasks

        try {
            // Block main thread until all submitted tasks finish or timeout expires
            System.out.println("Waiting for tasks to finish (timeout: 5 seconds)...");
            boolean allFinished = executor.awaitTermination(5, TimeUnit.SECONDS);

            if (allFinished) {
                System.out.println("All tasks finished successfully within timeout.");
            } else {
                System.err.println("Timeout reached! Forcing shutdown...");
                executor.shutdownNow(); // Interrupts currently running tasks
            }
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted while waiting.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Main thread finished.");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
