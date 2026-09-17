package com.practice.multithreading.synchronization;

public class BrokenTicketCounterSynchronizedDemo {

    // Shared variable — both threads will touch this
    static int availableTickets = 10000;

    public static synchronized void sellTicket() {
        if (availableTickets > 0) {
            availableTickets--;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            for (int i = 0; i < 5000; i++) {
                sellTicket();
            }
        };

        // Simulate 2 booking agents selling tickets simultaneously
        Thread agent1 = new Thread(task, "Agent-1");
        Thread agent2 = new Thread(task, "Agent-2");

        agent1.start();
        agent2.start();

        agent1.join();
        agent2.join();

        // Expected: 10000 - 5000 - 5000 = 0
        System.out.println("Remaining tickets: " + availableTickets);
    }
}
