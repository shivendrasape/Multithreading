package com.practice.multithreading.synchronization;

public class TicketBookingRaceConditionDemo {

    static int availableTickets = 1; // Last ticket!

    static void bookTicket(String user) {
        // This entire block is NOT atomic — thread can be interrupted
        // between the check and the decrement

        // giving some time so that both threads can reach the critical section at the same time
        sleep(100);

        if (availableTickets > 0) {
            // ← DANGER ZONE: Thread can be paused RIGHT HERE by the OS
            // Another thread sneaks in, also sees availableTickets > 0
            // Both threads pass the check!

            availableTickets--;
            System.out.println(user + " successfully booked! Remaining: "
                                + availableTickets);
        } else {
            System.out.println(user + " — Sorry, sold out!");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Raj and Priya book at the same time
        Thread raj   = new Thread(() -> bookTicket("Raj"),   "Raj-Thread");
        Thread priya = new Thread(() -> bookTicket("Priya"), "Priya-Thread");

        raj.start();
        priya.start();

        raj.join();
        priya.join();
    }

    static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { }
    }
}
