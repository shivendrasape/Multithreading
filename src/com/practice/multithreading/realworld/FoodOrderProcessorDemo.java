package com.practice.multithreading.realworld;

public class FoodOrderProcessorDemo {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        System.out.println("Order received! Firing all tasks in parallel...\n");

        // Define all 4 tasks
        Thread t1 = new Thread(() -> {
            sleep(500);
            System.out.println("[DB] Order saved.              (500ms)");
        });

        Thread t2 = new Thread(() -> {
            sleep(300);
            System.out.println("[EMAIL] Confirmation sent.     (300ms)");
        });

        Thread t3 = new Thread(() -> {
            sleep(400);
            System.out.println("[DELIVERY] Partner notified.   (400ms)");
        });

        Thread t4 = new Thread(() -> {
            sleep(200);
            System.out.println("[DASHBOARD] Restaurant updated.(200ms)");
        });

        // Fire all threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Wait for ALL of them to finish before proceeding
        // join() = "main thread, PAUSE here until this thread finishes"
        t1.join();
        t2.join();
        t3.join();
        t4.join();

        long endTime = System.currentTimeMillis();
        System.out.println("\nAll tasks done! Total time: " 
                            + (endTime - startTime) + "ms");
        // ~500ms instead of ~1400ms 🎉
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }
}
