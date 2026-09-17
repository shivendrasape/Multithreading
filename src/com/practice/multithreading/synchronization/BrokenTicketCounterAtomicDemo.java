package com.practice.multithreading.synchronization;

import java.util.concurrent.atomic.AtomicInteger;

public class BrokenTicketCounterAtomicDemo {

    // Shared variable — both threads will touch this
    static AtomicInteger availableTickets = new AtomicInteger(10000);

    public static void main(String[] args) throws InterruptedException {

        // Simulate 2 booking agents selling tickets simultaneously
        Thread agent1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                availableTickets.getAndDecrement(); // Agent 1 sells 5000 tickets
            }
        }, "Agent-1");

        Thread agent2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                availableTickets.getAndDecrement(); // Agent 2 sells 5000 tickets
            }
        }, "Agent-2");

        agent1.start();
        agent2.start();

        agent1.join();
        agent2.join();

        // Expected: 10000 - 5000 - 5000 = 0
        System.out.println("Remaining tickets: " + availableTickets);
    }
}
