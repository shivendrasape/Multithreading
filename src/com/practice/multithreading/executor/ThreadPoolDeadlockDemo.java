package com.practice.multithreading.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Demonstrates Thread Starvation Deadlock:
 * A pool has only 1 thread.
 * The Parent task takes that thread and waits for the Child task.
 * But the Child task needs that same thread to run -> DEADLOCK!
 */
public class ThreadPoolDeadlockDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        pool.execute(() -> {
            System.out.println("1. Parent task started on the only worker thread.");
            System.out.println("2. Parent task submits a Child task to the SAME pool...");

            // Submit child task to the same single-thread pool
            Future<?> childTask = pool.submit(() -> {
                System.out.println("Child task running!");
            });

            System.out.println("3. Parent waits for Child to finish (with 5-second timeout)...");
            try {
                //DEADLOCK! Parent will wait forever unless timed out
                childTask.get(5, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                System.out.println("\n DEADLOCK OCCURRED!");
                System.out.println("-> Parent is occupying the only thread, waiting for Child.");
                System.out.println("-> Child is waiting in the queue for that exact same thread.");
                System.out.println("-> Neither can ever finish!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Wait for demo to complete, then shut down
        Thread.sleep(3000);
        pool.shutdownNow();
    }
}
